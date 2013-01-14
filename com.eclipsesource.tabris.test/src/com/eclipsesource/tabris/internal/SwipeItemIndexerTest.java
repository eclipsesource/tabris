/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class SwipeItemIndexerTest {

  private SwipeItemIndexer indexer;

  @Before
  public void setUp() {
    indexer = new SwipeItemIndexer();
  }

  @Test
  public void testDefaultValues() {
    assertEquals( 0, indexer.getPrevious().length );
    assertEquals( -1, indexer.getCurrent() );
    assertEquals( 0, indexer.getNext().length );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetCurrentMustBePositive() {
    indexer.setCurrent( -1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetRangeFailsWithNegativeRange() {
    indexer.setRange( -1 );
  }

  @Test
  public void testReset() {
    indexer.setCurrent( 23 );

    indexer.reset();

    assertEquals( -1, indexer.getCurrent() );
  }

  @Test
  public void testSetCurrent() {
    indexer.setCurrent( 23 );

    assertEquals( 23, indexer.getCurrent() );
  }

  @Test
  public void testCurrentIs_0() {
    indexer.setCurrent( 0 );

    int next = indexer.getNext()[ 0 ];
    int[] previous = indexer.getPrevious();

    assertEquals( 0, previous.length );
    assertEquals( 1, next );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
  }

  @Test
  public void testCurrentIs_0_RangeIs_3() {
    indexer.setRange( 3 );
    indexer.setCurrent( 0 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();

    assertEquals( 0, previous.length );
    assertEquals( 1, next[ 0 ] );
    assertEquals( 2, next[ 1 ] );
    assertEquals( 3, next[ 2 ] );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
    assertEquals( -1, indexer.getOld() );
  }

  @Test
  public void testCurrentIs_0_RangeIs_0() {
    indexer.setRange( 0 );
    indexer.setCurrent( 0 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();

    assertEquals( 0, previous.length );
    assertEquals( 0, next.length );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
  }

  @Test
  public void testCurrentIs_0_then1_RangeIs_0() {
    indexer.setRange( 0 );
    indexer.setCurrent( 0 );
    indexer.setCurrent( 1 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();

    assertEquals( 0, previous.length );
    assertEquals( 0, next.length );
    assertArrayEquals( new int[] { 0 }, indexer.popOutOfRangeIndexes() );
  }

  @Test
  public void testCurrentIs_1() {
    indexer.setCurrent( 1 );

    int next = indexer.getNext()[ 0 ];
    int previous = indexer.getPrevious()[ 0 ];

    assertEquals( 0, previous );
    assertEquals( 2, next );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
  }

  @Test
  public void testCurrentIs_1_RangeIs_2() {
    indexer.setRange( 2 );
    indexer.setCurrent( 1 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();

    assertEquals( 0, previous[ 0 ] );
    assertEquals( 2, next[ 0 ] );
    assertEquals( 3, next[ 1 ] );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
  }

  @Test
  public void testCurrentIs_0then1() {
    indexer.setCurrent( 0 );
    indexer.setCurrent( 1 );

    int next = indexer.getNext()[ 0 ];
    int previous = indexer.getPrevious()[ 0 ];

    assertEquals( 0, previous );
    assertEquals( 2, next );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
    assertEquals( 0, indexer.getOld() );
  }

  @Test
  public void testCurrentIs_1then0() {
    indexer.setCurrent( 1 );
    indexer.setCurrent( 0 );

    int[] next = indexer.getNext();
    int previous = indexer.getPrevious()[ 0 ];

    assertEquals( 1, previous );
    assertEquals( 0, next.length );
    assertEquals( 2, indexer.popOutOfRangeIndexes()[ 0 ] );
  }

  @Test
  public void testCurrentIs_1then2() {
    indexer.setCurrent( 1 );
    indexer.setCurrent( 2 );

    int next = indexer.getNext()[ 0 ];
    int previous = indexer.getPrevious()[ 0 ];

    assertEquals( 1, previous );
    assertEquals( 3, next );
    assertEquals( 0, indexer.popOutOfRangeIndexes()[ 0 ] );
  }

  @Test
  public void testCurrentIs_1then2_RangeIs3() {
    indexer.setRange( 3 );
    indexer.setCurrent( 1 );
    indexer.setCurrent( 2 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();

    assertEquals( 0, previous[ 0 ] );
    assertEquals( 1, previous[ 1 ] );
    assertEquals( 3, next[ 0 ] );
    assertEquals( 4, next[ 1 ] );
    assertEquals( 5, next[ 2 ] );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
  }

  @Test
  public void testCurrentIs_1then5_RangeIs3() {
    indexer.setRange( 3 );
    indexer.setCurrent( 1 );
    indexer.setCurrent( 5 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();
    int[] outOfRangeIndexes = indexer.popOutOfRangeIndexes();

    assertEquals( 2, previous[ 0 ] );
    assertEquals( 3, previous[ 1 ] );
    assertEquals( 4, previous[ 2 ] );
    assertEquals( 6, next[ 0 ] );
    assertEquals( 7, next[ 1 ] );
    assertEquals( 8, next[ 2 ] );
    assertEquals( 0, outOfRangeIndexes[ 0 ] );
    assertEquals( 1, outOfRangeIndexes[ 1 ] );
  }

  @Test
  public void testCurrentIs_2then1() {
    indexer.setCurrent( 2 );
    indexer.setCurrent( 1 );

    int next = indexer.getNext()[ 0 ];
    int previous = indexer.getPrevious()[ 0 ];

    assertEquals( 2, previous );
    assertEquals( 0, next );
    assertEquals( 3, indexer.popOutOfRangeIndexes()[ 0 ] );
  }

  @Test
  public void testCurrentIs_2then5() {
    indexer.setCurrent( 2 );
    indexer.setCurrent( 5 );

    int next = indexer.getNext()[ 0 ];
    int previous = indexer.getPrevious()[ 0 ];

    assertEquals( 4, previous );
    assertEquals( 6, next );
    assertArrayEquals( new int[] { 1, 2, 3 }, indexer.popOutOfRangeIndexes() );
  }

  @Test
  public void testCurrentIs_2then5_RangeIs2() {
    indexer.setRange( 2 );
    indexer.setCurrent( 2 );
    indexer.setCurrent( 5 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();

    assertEquals( 3, previous[ 0 ] );
    assertEquals( 4, previous[ 1 ] );
    assertEquals( 6, next[ 0 ] );
    assertEquals( 7, next[ 1 ] );
    assertArrayEquals( new int[] { 0, 1, 2 }, indexer.popOutOfRangeIndexes() );
  }

  @Test
  public void testCurrentIs_3then2_RangeIs2() {
    indexer.setRange( 2 );
    indexer.setCurrent( 3 );
    indexer.setCurrent( 2 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();

    assertEquals( 3, previous[ 0 ] );
    assertEquals( 4, previous[ 1 ] );
    assertEquals( 0, next[ 0 ] );
    assertEquals( 1, next[ 1 ] );
    assertArrayEquals( new int[] { 5 }, indexer.popOutOfRangeIndexes() );
  }

  @Test
  public void testOutOfRangeIsEmptyWithFirstCurrentIndex() {
    indexer.setCurrent( 0 );

    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
  }

  @Test
  public void testCurrentIs_5then2() {
    indexer.setCurrent( 5 );
    indexer.setCurrent( 2 );

    int next = indexer.getNext()[ 0 ];
    int previous = indexer.getPrevious()[ 0 ];

    assertEquals( 1, previous );
    assertEquals( 3, next );
    assertArrayEquals( new int[] { 4, 5, 6 }, indexer.popOutOfRangeIndexes() );
  }

  @Test
  public void testCurrentIs_1then2_DoesNotHas0AsPrevious() {
    indexer.setCurrent( 1 );
    indexer.setCurrent( 2 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();

    assertEquals( 1, previous[ 0 ] );
    assertEquals( 1, previous.length );
    assertEquals( 3, next[ 0 ] );
    assertEquals( 1, next.length );
    assertArrayEquals( new int[] { 0 }, indexer.popOutOfRangeIndexes() );
  }

  @Test
  public void testCurrentIs_5then2_RangeIs2() {
    indexer.setRange( 2 );
    indexer.setCurrent( 5 );
    indexer.setCurrent( 2 );

    int[] next = indexer.getNext();
    int[] previous = indexer.getPrevious();

    assertEquals( 0, previous[ 0 ] );
    assertEquals( 1, previous[ 1 ] );
    assertEquals( 3, next[ 0 ] );
    assertEquals( 4, next[ 1 ] );
    assertArrayEquals( new int[] { 5, 6, 7 }, indexer.popOutOfRangeIndexes() );
  }

  @Test
  public void testOutOfRangePops() {
    indexer.setRange( 2 );
    indexer.setCurrent( 5 );
    indexer.setCurrent( 2 );

    assertArrayEquals( new int[] { 5, 6, 7 }, indexer.popOutOfRangeIndexes() );

    int[] outOfRangeIndexes = indexer.popOutOfRangeIndexes();

    assertEquals( 0, outOfRangeIndexes.length );
  }

  @Test
  public void testDoesNotPopOutOfRangeWithFirstSetCurrent() {
    indexer.setCurrent( 5 );

    int[] outOfRangeIndexes = indexer.popOutOfRangeIndexes();

    assertEquals( 0, outOfRangeIndexes.length );
  }

}

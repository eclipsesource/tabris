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
    assertEquals( -1, indexer.getPrevious() );
    assertEquals( -1, indexer.getCurrent() );
    assertEquals( -1, indexer.getNext() );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testSetCurrentMustBePositive() {
    indexer.setCurrent( -1 );
  }
  
  @Test
  public void testSetCurrent() {
    indexer.setCurrent( 23 );
    
    assertEquals( 23, indexer.getCurrent() );
  }
  
  @Test
  public void testCurrentIs_0() {
    indexer.setCurrent( 0 );
    
    int next = indexer.getNext();
    int previous = indexer.getPrevious();
    
    assertEquals( -1, previous );
    assertEquals( 1, next );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
  }
  
  @Test
  public void testCurrentIs_1() {
    indexer.setCurrent( 1 );
    
    int next = indexer.getNext();
    int previous = indexer.getPrevious();
    
    assertEquals( 0, previous );
    assertEquals( 2, next );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
  }
  
  @Test
  public void testCurrentIs_0then1() {
    indexer.setCurrent( 0 );
    indexer.setCurrent( 1 );
    
    int next = indexer.getNext();
    int previous = indexer.getPrevious();
    
    assertEquals( 0, previous );
    assertEquals( 2, next );
    assertEquals( 0, indexer.popOutOfRangeIndexes().length );
  }
  
  @Test
  public void testCurrentIs_1then0() {
    indexer.setCurrent( 1 );
    indexer.setCurrent( 0 );
    
    int next = indexer.getNext();
    int previous = indexer.getPrevious();
    
    assertEquals( 1, previous );
    assertEquals( -1, next );
    assertEquals( 2, indexer.popOutOfRangeIndexes()[ 0 ] );
  }
  
  @Test
  public void testCurrentIs_1then2() {
    indexer.setCurrent( 1 );
    indexer.setCurrent( 2 );
    
    int next = indexer.getNext();
    int previous = indexer.getPrevious();
    
    assertEquals( 1, previous );
    assertEquals( 3, next );
    assertEquals( 0, indexer.popOutOfRangeIndexes()[ 0 ] );
  }
  
  @Test
  public void testCurrentIs_2then1() {
    indexer.setCurrent( 2 );
    indexer.setCurrent( 1 );
    
    int next = indexer.getNext();
    int previous = indexer.getPrevious();
    
    assertEquals( 2, previous );
    assertEquals( 0, next );
    assertEquals( 3, indexer.popOutOfRangeIndexes()[ 0 ] );
  }
  
  @Test
  public void testCurrentIs_2then5() {
    indexer.setCurrent( 2 );
    indexer.setCurrent( 5 );
    
    int next = indexer.getNext();
    int previous = indexer.getPrevious();
    
    assertEquals( 4, previous );
    assertEquals( 6, next );
    assertArrayEquals( new int[] { 1, 2, 3 }, indexer.popOutOfRangeIndexes() );
  }
  
  @Test
  public void testCurrentIs_5then2() {
    indexer.setCurrent( 5 );
    indexer.setCurrent( 2 );
    
    int next = indexer.getNext();
    int previous = indexer.getPrevious();
    
    assertEquals( 1, previous );
    assertEquals( 3, next );
    assertArrayEquals( new int[] { 4, 5, 6 }, indexer.popOutOfRangeIndexes() );
  }
  
}

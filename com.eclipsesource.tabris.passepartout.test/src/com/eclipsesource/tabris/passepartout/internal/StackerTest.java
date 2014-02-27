/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.passepartout.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Bounds;


public class StackerTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBounds() {
    new Stacker( null );
  }

  @Test
  public void testStacksTwoRectanglesHorizontal() {
    Stacker stacker = new Stacker( new Bounds( 0, 0, 200, 600 ) );

    Bounds bounds1 = stacker.stack( 100, 100 );
    Bounds bounds2 = stacker.stack( 100, 100 );

    assertPosition( bounds1, 0, 0 );
    assertPosition( bounds2, 100, 0 );
  }

  @Test
  public void testStacksThreeRectanglesWithLineBreak() {
    Stacker stacker = new Stacker( new Bounds( 0, 0, 200, 600 ) );

    Bounds bounds1 = stacker.stack( 100, 100 );
    Bounds bounds2 = stacker.stack( 100, 100 );
    Bounds bounds3 = stacker.stack( 100, 100 );

    assertPosition( bounds1, 0, 0 );
    assertPosition( bounds2, 100, 0 );
    assertPosition( bounds3, 0, 100 );
  }

  @Test
  public void testStacksFourRectanglesWithLineBreak() {
    Stacker stacker = new Stacker( new Bounds( 0, 0, 200, 600 ) );

    Bounds bounds1 = stacker.stack( 100, 100 );
    Bounds bounds2 = stacker.stack( 100, 100 );
    Bounds bounds3 = stacker.stack( 100, 100 );
    Bounds bounds4 = stacker.stack( 100, 100 );

    assertPosition( bounds1, 0, 0 );
    assertPosition( bounds2, 100, 0 );
    assertPosition( bounds3, 0, 100 );
    assertPosition( bounds4, 100, 100 );
  }

  @Test
  public void testStacksFourRectanglesWithLineBreakAndUsesBiggestHeightAsY() {
    Stacker stacker = new Stacker( new Bounds( 0, 0, 200, 600 ) );

    Bounds bounds1 = stacker.stack( 100, 200 );
    Bounds bounds2 = stacker.stack( 100, 100 );
    Bounds bounds3 = stacker.stack( 100, 100 );
    Bounds bounds4 = stacker.stack( 100, 100 );

    assertPosition( bounds1, 0, 0 );
    assertPosition( bounds2, 100, 0 );
    assertPosition( bounds3, 0, 200 );
    assertPosition( bounds4, 100, 200 );
  }

  @Test
  public void testCorrectsWidthUpLeftOverPixel() {
    Stacker stacker = new Stacker( new Bounds( 0, 0, 604, 300 ) );

    Bounds bounds1 = stacker.stack( 201, 100 );
    Bounds bounds2 = stacker.stack( 201, 100 );
    Bounds bounds3 = stacker.stack( 201, 100 );

    assertPosition( bounds1, 0, 0 );
    assertPosition( bounds2, 201, 0 );
    assertBounds( bounds3, 402, 0, 202, 100 );
  }

  @Test
  public void testCorrectsWidthDownLeftOverPixel() {
    Stacker stacker = new Stacker( new Bounds( 0, 0, 604, 300 ) );

    Bounds bounds1 = stacker.stack( 202, 100 );
    Bounds bounds2 = stacker.stack( 202, 100 );
    Bounds bounds3 = stacker.stack( 202, 100 );

    assertPosition( bounds1, 0, 0 );
    assertPosition( bounds2, 202, 0 );
    assertBounds( bounds3, 404, 0, 200, 100 );
  }

  private void assertPosition( Bounds bounds, int expectedX, int expectedY ) {
    assertEquals( "X", expectedX, bounds.getX() );
    assertEquals( "Y", expectedY, bounds.getY() );
  }

  private void assertBounds( Bounds bounds, int expectedX, int expectedY, int expectedWidth, int expectedHeight ) {
    assertEquals( "X", expectedX, bounds.getX() );
    assertEquals( "Y", expectedY, bounds.getY() );
    assertEquals( "Width", expectedWidth, bounds.getWidth() );
    assertEquals( "Height", expectedHeight, bounds.getHeight() );
  }
}

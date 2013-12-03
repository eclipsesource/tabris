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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.Test;


public class ScrollingCompositeUtilTest {

  @Test
  public void testIsRevealedWhenInMiddleOfClientArea() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 50, 50, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertTrue( isRevealed );
  }

  @Test
  public void testIsRevealedWhenOnTopLeftEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 0, 0, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertTrue( isRevealed );
  }

  @Test
  public void testIsRevealedWhenOnTopRightEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 90, 0, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertTrue( isRevealed );
  }

  @Test
  public void testIsRevealedWhenOnLeftBottomEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 0, 90, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertTrue( isRevealed );
  }

  @Test
  public void testIsRevealedWhenOnRightBottomEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 90, 90, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertTrue( isRevealed );
  }

  @Test
  public void testIsNotRevealedWhenPartiallyOutOfTopLeftEdge() {
    Point origin = new Point( 0, 5 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 0, 0, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertFalse( isRevealed );
  }

  @Test
  public void testIsNotRevealedWhenPartiallyOutOfTopRightEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 95, 0, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertFalse( isRevealed );
  }

  @Test
  public void testIsNotRevealedWhenPartiallyOutOfLeftBottomEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 0, 95, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertFalse( isRevealed );
  }

  @Test
  public void testIsNotRevealedWhenPartiallyOutOfRightBottomEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 95, 95, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertFalse( isRevealed );
  }

  @Test
  public void testIsNotRevealedWhenFullyOutOfTopLeftEdge() {
    Point origin = new Point( 20, 20 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 0, 0, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertFalse( isRevealed );
  }

  @Test
  public void testIsNotRevealedWhenFullOutOfTopRightEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 120, 0, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertFalse( isRevealed );
  }

  @Test
  public void testIsNotRevealedWhenFullyOutOfLeftBottomEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 0, 120, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertFalse( isRevealed );
  }

  @Test
  public void testIsNotRevealedWhenFullyOutOfRightBottomEdge() {
    Point origin = new Point( 0, 0 );
    Rectangle clientArea = createRectangle( 0, 0, 100, 100 );
    Rectangle controlBounds = createRectangle( 120, 120, 10, 10 );

    boolean isRevealed = ScrollingCompositeUtil.isRevealed( origin, clientArea, controlBounds );

    assertFalse( isRevealed );
  }

  private Rectangle createRectangle( int x, int y, int width, int height ) {
    return new Rectangle( x, y, width, height );
  }
}

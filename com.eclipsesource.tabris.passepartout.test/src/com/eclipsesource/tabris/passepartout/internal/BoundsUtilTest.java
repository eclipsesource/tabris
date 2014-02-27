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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Bounds;


public class BoundsUtilTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullRectangle() {
    BoundsUtil.getBounds( ( Rectangle )null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullPoint() {
    BoundsUtil.getBounds( ( Point )null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBounds() {
    BoundsUtil.getRectangle( null );
  }

  @Test
  public void testConvertsRectangleToBounds() {
    Rectangle rectangle = new Rectangle( 10, 20, 30, 40 );

    Bounds bounds = BoundsUtil.getBounds( rectangle );

    assertEquals( 10, bounds.getX() );
    assertEquals( 20, bounds.getY() );
    assertEquals( 30, bounds.getWidth() );
    assertEquals( 40, bounds.getHeight() );
  }

  @Test
  public void testConvertsBoundsToRectangle() {
    Bounds bounds = new Bounds( 10, 20, 30, 40 );

    Rectangle rectangle = BoundsUtil.getRectangle( bounds );

    assertEquals( 10, rectangle.x );
    assertEquals( 20, rectangle.y );
    assertEquals( 30, rectangle.width );
    assertEquals( 40, rectangle.height );
  }

  @Test
  public void testConvertsPointToBounds() {
    Point point = new Point( 10, 20 );

    Bounds bounds = BoundsUtil.getBounds( point );

    assertEquals( 0, bounds.getX() );
    assertEquals( 0, bounds.getY() );
    assertEquals( 10, bounds.getWidth() );
    assertEquals( 20, bounds.getHeight() );
  }
}

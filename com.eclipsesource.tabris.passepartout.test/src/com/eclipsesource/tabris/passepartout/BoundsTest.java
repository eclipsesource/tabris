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
package com.eclipsesource.tabris.passepartout;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class BoundsTest {

  @Test
  public void testHasX() {
    Bounds bounds = new Bounds( 23, 42, 123, 142 );

    int x = bounds.getX();

    assertEquals( 23, x );
  }

  @Test
  public void testHasY() {
    Bounds bounds = new Bounds( 23, 42, 123, 142 );

    int y = bounds.getY();

    assertEquals( 42, y );
  }

  @Test
  public void testHasWidth() {
    Bounds bounds = new Bounds( 23, 42, 123, 142 );

    int width = bounds.getWidth();

    assertEquals( 123, width );
  }

  @Test
  public void testHasHeight() {
    Bounds bounds = new Bounds( 23, 42, 123, 142 );

    int height = bounds.getHeight();

    assertEquals( 142, height );
  }

  @Test
  public void testImplementsEquals() {
    EqualsTester<Bounds> tester = EqualsTester.newInstance( new Bounds( 23, 42, 123, 142 ) );
    tester.assertEqual( new Bounds( 23, 42, 123, 142 ), new Bounds( 23, 42, 123, 142 ) );
    tester.assertNotEqual( new Bounds( 33, 42, 123, 142 ), new Bounds( 23, 42, 123, 142 ) );
    tester.assertNotEqual( new Bounds( 23, 43, 123, 142 ), new Bounds( 23, 42, 123, 142 ) );
    tester.assertNotEqual( new Bounds( 23, 42, 23, 142 ), new Bounds( 23, 42, 123, 142 ) );
    tester.assertNotEqual( new Bounds( 23, 42, 123, 42 ), new Bounds( 23, 42, 123, 142 ) );
    tester.assertImplementsEqualsAndHashCode();
  }

  @Test
  public void testImplementsToString() {
    Bounds bounds = new Bounds( 23, 42, 123, 142 );

    assertEquals( "Bounds {23, 42, 123, 142}", bounds.toString() );
  }
}

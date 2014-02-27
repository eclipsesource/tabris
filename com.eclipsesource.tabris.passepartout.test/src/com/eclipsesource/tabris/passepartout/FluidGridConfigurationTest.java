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
import static org.junit.Assert.assertSame;

import org.junit.Test;


public class FluidGridConfigurationTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullMode() {
    new FluidGridConfiguration( null, 100, 200 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeEightColumnWidth() {
    new FluidGridConfiguration( LayoutMode.AUTO, -100, 200 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithZeroEightColumnWidth() {
    new FluidGridConfiguration( LayoutMode.AUTO, 0, 200 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeSixteenColumnWidth() {
    new FluidGridConfiguration( LayoutMode.AUTO, 100, -200 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithZeroSixteenColumnWidth() {
    new FluidGridConfiguration( LayoutMode.AUTO, 100, 0 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithSixteenColumnWidthSmallerEightColumnWidth() {
    new FluidGridConfiguration( LayoutMode.AUTO, 100, 99 );
  }

  @Test
  public void testHasMode() {
    FluidGridConfiguration configuration = new FluidGridConfiguration( LayoutMode.AUTO, 100, 200 );

    LayoutMode mode = configuration.getMode();

    assertSame( LayoutMode.AUTO, mode );
  }

  @Test
  public void testHasEightColumnWidth() {
    FluidGridConfiguration configuration = new FluidGridConfiguration( LayoutMode.AUTO, 100, 200 );

    int eightColumnWidth = configuration.getEightColumnWidth();

    assertEquals( eightColumnWidth, 100 );
  }

  @Test
  public void testHasSixteenColumnWidth() {
    FluidGridConfiguration configuration = new FluidGridConfiguration( LayoutMode.AUTO, 100, 200 );

    int sixteenColumnWidth = configuration.getSixteenColumnWidth();

    assertEquals( sixteenColumnWidth, 200 );
  }
}

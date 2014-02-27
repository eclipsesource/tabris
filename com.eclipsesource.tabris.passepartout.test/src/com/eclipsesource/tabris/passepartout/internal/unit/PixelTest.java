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
package com.eclipsesource.tabris.passepartout.internal.unit;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;


public class PixelTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativePixel() {
    new Pixel( -1 );
  }

  @Test
  public void testHasPixelValue() {
    Pixel pixel = new Pixel( 23 );

    BigDecimal value = pixel.getValue();

    assertEquals( BigDecimal.valueOf( 23 ), value );
  }
}

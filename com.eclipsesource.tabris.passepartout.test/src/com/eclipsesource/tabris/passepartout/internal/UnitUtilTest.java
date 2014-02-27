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

import java.math.BigDecimal;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;
import com.eclipsesource.tabris.passepartout.internal.unit.Pixel;


public class UnitUtilTest {

  @Test
  public void testCalculatesPercentage() {
    int pixel = UnitUtil.percentageToPixel( new Percentage( BigDecimal.TEN ), 100 );

    assertEquals( 10, pixel );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testCalculatesPercentageFailsWithNegativePercent() {
    UnitUtil.percentageToPixel( new Percentage( BigDecimal.TEN ), -100 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testCalculatesPercentageFailsWithWrongUnit() {
    UnitUtil.percentageToPixel( new Pixel( 23 ), 100 );
  }

  @Test
  public void testCalculatesEm() {
    int pixel = UnitUtil.emToPixel( new Em( BigDecimal.TEN ), 10 );

    assertEquals( 100, pixel );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testCalculatesEmFailsWithNegativeFontSize() {
    UnitUtil.emToPixel( new Em( BigDecimal.TEN ), -10 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testCalculatesEmFailsWithWrongUnit() {
    UnitUtil.emToPixel( new Percentage( BigDecimal.TEN ), 10 );
  }
}

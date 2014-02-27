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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.when;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.eclipsesource.tabris.passepartout.Unit;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;


public class UnitUtil {

  public static int emToPixel( Unit unit, int parentFontSize ) {
    when( !( unit instanceof Em ) ).throwIllegalArgument( "Unsupported Unit" );
    when( parentFontSize <= 0 ).throwIllegalArgument( "Parent FontSize must be > 0 but was " + parentFontSize );
    return unit.getValue()
               .multiply( BigDecimal.valueOf( parentFontSize ) )
               .setScale( 0, RoundingMode.HALF_UP )
               .intValue();
  }

  public static int percentageToPixel( Unit unit, int hundretPercent ) {
    when( !( unit instanceof Percentage ) ).throwIllegalArgument( "Unsupported Unit" );
    when( hundretPercent <= 0 ).throwIllegalArgument( "Percentage must be > 0 but was " + hundretPercent );
    return BigDecimal.valueOf( hundretPercent)
                     .divide( BigDecimal.valueOf( 100 ), MathContext.DECIMAL32 )
                     .multiply( unit.getValue() )
                     .setScale( 0, RoundingMode.HALF_UP )
                     .intValue();
  }

  private UnitUtil() {
    // prevent instantiations
  }
}

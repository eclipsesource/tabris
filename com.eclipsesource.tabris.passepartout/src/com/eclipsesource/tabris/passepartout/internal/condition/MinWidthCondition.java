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
package com.eclipsesource.tabris.passepartout.internal.condition;

import static com.eclipsesource.tabris.passepartout.internal.Clauses.when;
import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.passepartout.internal.UnitUtil.emToPixel;
import static com.eclipsesource.tabris.passepartout.internal.UnitUtil.percentageToPixel;

import java.math.BigDecimal;

import com.eclipsesource.tabris.passepartout.Condition;
import com.eclipsesource.tabris.passepartout.UIEnvironment;
import com.eclipsesource.tabris.passepartout.Unit;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;
import com.eclipsesource.tabris.passepartout.internal.unit.Pixel;


public class MinWidthCondition implements Condition {

  private final Unit minWidth;

  public MinWidthCondition( Unit minWidth ) {
    whenNull( minWidth ).throwIllegalArgument( "minWidth must not be null" );
    when( minWidth.getValue().compareTo( BigDecimal.ZERO ) < 0 )
      .throwIllegalArgument( "minWidth must be positive but was " + minWidth );
    this.minWidth = minWidth;
  }

  @Override
  public boolean compliesWith( UIEnvironment environment ) {
    int actualWidth = environment.getParentBounds().getWidth();
    if( minWidth instanceof Pixel ) {
      return actualWidth >= minWidth.getValue().intValue();
    } else if( minWidth instanceof Em ) {
      return actualWidth >= emToPixel( minWidth, environment.getParentFontSize() );
    } else if( minWidth instanceof Percentage ) {
      return actualWidth >= percentageToPixel( minWidth, environment.getReferenceBounds().getWidth() );
    }
    return false;
  }
}

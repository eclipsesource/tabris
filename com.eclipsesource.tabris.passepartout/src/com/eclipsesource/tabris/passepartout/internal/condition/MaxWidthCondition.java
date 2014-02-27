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
import static com.eclipsesource.tabris.passepartout.internal.UnitUtil.percentageToPixel;

import java.math.BigDecimal;

import com.eclipsesource.tabris.passepartout.Condition;
import com.eclipsesource.tabris.passepartout.UIEnvironment;
import com.eclipsesource.tabris.passepartout.Unit;
import com.eclipsesource.tabris.passepartout.internal.UnitUtil;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;
import com.eclipsesource.tabris.passepartout.internal.unit.Pixel;


public class MaxWidthCondition implements Condition {

  private final Unit maxWidth;

  public MaxWidthCondition( Unit maxWidth ) {
    whenNull( maxWidth ).throwIllegalArgument( "maxWidth must not be null" );
    when( maxWidth.getValue().compareTo( BigDecimal.ZERO ) <= 0 ).throwIllegalArgument( "maxWidth must be > 0" );
    this.maxWidth = maxWidth;
  }

  @Override
  public boolean compliesWith( UIEnvironment environment ) {
    int actualWidth = environment.getParentBounds().getWidth();
    if( maxWidth instanceof Percentage ) {
      return actualWidth <= percentageToPixel( maxWidth, environment.getReferenceBounds().getWidth() );
    } else if( maxWidth instanceof Em ) {
      return actualWidth <= UnitUtil.emToPixel( maxWidth, environment.getParentFontSize() );
    } else if( maxWidth instanceof Pixel ) {
      return actualWidth <= maxWidth.getValue().intValue();
    }
    return false;
  }
}

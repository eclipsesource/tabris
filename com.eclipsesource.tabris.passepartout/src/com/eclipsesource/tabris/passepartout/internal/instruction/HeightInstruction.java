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
package com.eclipsesource.tabris.passepartout.internal.instruction;

import static com.eclipsesource.tabris.passepartout.internal.Clauses.when;
import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.passepartout.internal.UnitUtil.emToPixel;
import static com.eclipsesource.tabris.passepartout.internal.UnitUtil.percentageToPixel;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.Instruction;
import com.eclipsesource.tabris.passepartout.Unit;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;
import com.eclipsesource.tabris.passepartout.internal.unit.Pixel;


public class HeightInstruction implements Instruction {

  private final Unit unit;

  public HeightInstruction( Unit unit ) {
    whenNull( unit ).throwIllegalArgument( "Unit must not be null" );
    this.unit = unit;
  }

  public int computeHeight( Bounds parentBounds, int parentFontSize ) {
    whenNull( parentBounds ).throwIllegalArgument( "ParentBounds must not be null" );
    when( parentFontSize <= 0 ).throwIllegalArgument( "Parent Font Size must be > 0 but was " + parentFontSize );
    return doComputeHeight( parentBounds, parentFontSize );
  }

  private int doComputeHeight( Bounds parentBounds, int parentFontSize ) {
    if( unit instanceof Pixel ) {
      return ( ( Pixel )unit ).getValue().intValue();
    } else if( unit instanceof Percentage ) {
      return percentageToPixel( unit, parentBounds.getHeight() );
    } else if( unit instanceof Em ) {
      return emToPixel( unit, parentFontSize );
    }
    return 0;
  }

}

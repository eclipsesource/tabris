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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.FluidGridConfiguration;
import com.eclipsesource.tabris.passepartout.Instruction;
import com.eclipsesource.tabris.passepartout.UIEnvironment;
import com.eclipsesource.tabris.passepartout.internal.instruction.ColumnsInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ExcludeInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.HeightInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.MarginInstruction;


public class Layouter {

  private final UIEnvironment environment;
  private final FluidGridConfiguration configuration;
  private final Stacker stacker;

  public Layouter( UIEnvironment environment, FluidGridConfiguration configuration ) {
    whenNull( environment ).throwIllegalArgument( "UIEnvironment must not be null" );
    whenNull( configuration ).throwIllegalArgument( "FluidGridConfiguration must not be null" );
    this.environment = environment;
    this.configuration = configuration;
    this.stacker = new Stacker( this.environment.getParentBounds() );
  }

  public Bounds computeBounds( Bounds preferedBounds, List<Instruction> instructions ) {
    if( isExcluded( instructions ) ) {
      return new Bounds( 0, 0, 0, 0 );
    }
    return calculateBounds( preferedBounds, instructions );
  }

  private boolean isExcluded( List<Instruction> instructions ) {
    for( Instruction instruction : instructions ) {
      if( instruction instanceof ExcludeInstruction ) {
        return true;
      }
    }
    return false;
  }

  private Bounds calculateBounds( Bounds preferedBounds, List<Instruction> instructions ) {
    int width = computeWidth( instructions );
    int height = computeHeight( preferedBounds, instructions );
    Bounds bounds = stacker.stack( width, height );
    return applyMargins( bounds, instructions );
  }

  private int computeHeight( Bounds preferedBounds, List<Instruction> instructions ) {
    int height = preferedBounds.getHeight();
    for( Instruction instruction : instructions ) {
      if( instruction instanceof HeightInstruction ) {
        height = ( ( HeightInstruction )instruction ).computeHeight( environment.getParentBounds(),
                                                                     environment.getParentFontSize() );
      }
    }
    return height;
  }

  private int computeWidth( List<Instruction> instructions ) {
    int occupiedColumns = 1;
    for( Instruction instruction : instructions ) {
      if( instruction instanceof ColumnsInstruction ) {
        occupiedColumns = ( ( ColumnsInstruction )instruction ).getColumns();
      }
    }
    BigDecimal width = BigDecimal.valueOf( environment.getParentBounds().getWidth() )
                                 .divide( BigDecimal.valueOf( getColumns() ), MathContext.DECIMAL32 )
                                 .multiply( BigDecimal.valueOf( occupiedColumns ) )
                                 .setScale( 0, RoundingMode.HALF_UP );
    return width.intValue();
  }

  public int getColumns() {
    int columns = 4;
    if( environment.getParentBounds().getWidth() >= configuration.getSixteenColumnWidth() ) {
      columns = 16;
    } else if( environment.getParentBounds().getWidth() >= configuration.getEightColumnWidth() ) {
      columns = 8;
    }
    return columns;
  }

  private Bounds applyMargins( Bounds bounds, List<Instruction> instructions ) {
    Bounds result = bounds;
    for( Instruction instruction : instructions ) {
      if( instruction instanceof MarginInstruction ) {
        result = ( ( MarginInstruction )instruction ).computeBounds( result, environment.getParentFontSize() );
      }
    }
    return result;
  }
}

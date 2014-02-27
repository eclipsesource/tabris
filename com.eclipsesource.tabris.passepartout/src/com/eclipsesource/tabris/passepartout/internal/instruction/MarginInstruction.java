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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.passepartout.internal.UnitUtil.emToPixel;
import static com.eclipsesource.tabris.passepartout.internal.UnitUtil.percentageToPixel;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.Instruction;
import com.eclipsesource.tabris.passepartout.Unit;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;
import com.eclipsesource.tabris.passepartout.internal.unit.Pixel;


public class MarginInstruction implements Instruction {

  private final Unit top;
  private final Unit right;
  private final Unit bottom;
  private final Unit left;

  public MarginInstruction( Unit top, Unit right, Unit bottom, Unit left ) {
    validateMargins( top, right, bottom, left );
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
  }

  private void validateMargins( Unit top, Unit right, Unit bottom, Unit left ) {
    whenNull( top ).throwIllegalArgument( "Top must not be null" );
    whenNull( right ).throwIllegalArgument( "Right must not be null" );
    whenNull( bottom ).throwIllegalArgument( "Bottom must not be null" );
    whenNull( left ).throwIllegalArgument( "Left must not be null" );
  }

  public Bounds computeBounds( Bounds originalBounds, int parentFontSize ) {
    int x = getX( originalBounds, parentFontSize );
    int y = getY( originalBounds, parentFontSize );
    int width = getWidth( originalBounds, parentFontSize );
    int height = getHeight( originalBounds, parentFontSize );
    return new Bounds( x, y, width, height );
  }

  private int getWidth( Bounds bounds, int parentFontSize ) {
    if( right instanceof Pixel ) {
      return bounds.getWidth() - ( getX( bounds, parentFontSize ) - bounds.getX() ) - right.getValue().intValue();
    } else if( right instanceof Em ) {
      return bounds.getWidth() - ( getX( bounds, parentFontSize ) - bounds.getX() ) - emToPixel( right, parentFontSize );
    } else if( right instanceof Percentage ) {
      return bounds.getWidth()
             - ( getX( bounds, parentFontSize ) - bounds.getX() )
             - percentageToPixel( right, bounds.getWidth() );
    }
    return bounds.getWidth();
  }

  private int getHeight( Bounds bounds, int parentFontSize ) {
    if( bottom instanceof Pixel ) {
      return bounds.getHeight() - ( getY( bounds, parentFontSize ) - bounds.getY() ) - bottom.getValue().intValue();
    } else if( bottom instanceof Em ) {
      return bounds.getHeight()
             - ( getY( bounds, parentFontSize ) - bounds.getY() )
             - emToPixel( bottom, parentFontSize );
    } else if( bottom instanceof Percentage ) {
      return bounds.getHeight()
             - ( getY( bounds, parentFontSize ) - bounds.getY() )
             - percentageToPixel( bottom, bounds.getHeight() );
    }
    return bounds.getHeight();
  }

  private int getX( Bounds bounds, int parentFontSize ) {
    if( left instanceof Pixel ) {
      return bounds.getX() + left.getValue().intValue();
    } else if( left instanceof Em ) {
      return bounds.getX() + emToPixel( left, parentFontSize );
    } else if( left instanceof Percentage ) {
      return bounds.getX() + percentageToPixel( left, bounds.getWidth() );
    }
    return bounds.getX();
  }

  private int getY( Bounds bounds, int parentFontSize ) {
    if( top instanceof Pixel ) {
      return bounds.getY() + top.getValue().intValue();
    } else if( top instanceof Em ) {
      return bounds.getY() + emToPixel( top, parentFontSize );
    } else if( top instanceof Percentage ) {
      return bounds.getY() + percentageToPixel( top, bounds.getHeight() );
    }
    return bounds.getY();
  }

}

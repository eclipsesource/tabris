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

import com.eclipsesource.tabris.passepartout.Bounds;


public class Stacker {

  private static final int MAX_COLUMNS = 16;

  private final Bounds bounds;
  private int rowX;
  private int nowY;
  private int rowHeight;

  public Stacker( Bounds bounds ) {
    whenNull( bounds ).throwIllegalArgument( "Bounds must not be null" );
    this.bounds = bounds;
    this.rowX = 0;
    this.nowY = 0;
    this.rowHeight = 0;
  }

  public Bounds stack( int width, int height ) {
    int cellWidth = adjustCellWidth( width );
    handleLineBreak( cellWidth );
    adjustRowHeight( height );
    Bounds cellBounds = new Bounds( rowX, nowY, cellWidth, height );
    assignNewX( cellWidth );
    return cellBounds;
  }

  private int adjustCellWidth( int width ) {
    int rightMax = bounds.getWidth() + MAX_COLUMNS;
    int leftMax = bounds.getWidth() - MAX_COLUMNS;
    int newRight = rowX + width;
    if( newRight > bounds.getWidth() && newRight <= rightMax ) {
      return width - ( newRight - bounds.getWidth() );
    } else if( newRight < bounds.getWidth() && newRight >= leftMax ) {
      return width + ( bounds.getWidth() - newRight );
    } else {
      return width;
    }
  }

  private void handleLineBreak( int cellWidth ) {
    if( rowX + cellWidth > bounds.getWidth() ) {
      rowX = 0;
      nowY += rowHeight;
      rowHeight = 0;
    }
  }

  private void adjustRowHeight( int height ) {
    if( height > rowHeight ) {
      rowHeight = height;
    }
  }

  private void assignNewX( int cellWidth ) {
    rowX += cellWidth;
  }

}

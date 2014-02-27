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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.when;
import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;


public class FluidGridConfiguration {

  private final LayoutMode mode;
  private final int eightColumnWidth;
  private final int sixteenColumnWidth;

  public FluidGridConfiguration( LayoutMode mode, int eightColumnWidth, int sixteenColumnWidth ) {
    whenNull( mode ).throwIllegalArgument( "LayoutMode must not be null" );
    when( eightColumnWidth <= 0 ).throwIllegalArgument( "eightColumnWith must be > 0" );
    when( sixteenColumnWidth <= 0 ).throwIllegalArgument( "sixteenColumnWidth must be > 0" );
    when( sixteenColumnWidth <= eightColumnWidth ).throwIllegalArgument( "sixteenColumnWidth must be > eightColumnWidth" );
    this.mode = mode;
    this.eightColumnWidth = eightColumnWidth;
    this.sixteenColumnWidth = sixteenColumnWidth;
  }

  public LayoutMode getMode() {
    return mode;
  }

  public int getEightColumnWidth() {
    return eightColumnWidth;
  }

  public int getSixteenColumnWidth() {
    return sixteenColumnWidth;
  }

}

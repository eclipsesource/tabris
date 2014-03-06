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


/**
 * <p>
 * A {@link FluidGridConfiguration} holds the configuration for {@link FluidGridLayout}s. The {@link FluidGridLayout}
 * implements a concept called column folding. It adjusts the number of the available columns to the screen size:
 * <ul>
 *   <li>Small Screen Sizes = 4 columns</li>
 *   <li>Medium Screen Sizes = 8 columns</li>
 *   <li>Big Screen Sizes = 16 columns</li>
 * </ul>
 * With the {@link FluidGridConfiguration} you can define the values for these switches.
 * </p>
 * <p>
 * Another option you can define is the {@link LayoutMode} used for the {@link FluidGridLayout}.
 * </p>
 *
 * @since 0.9
 */
public class FluidGridConfiguration {

  private final LayoutMode mode;
  private final int eightColumnWidth;
  private final int sixteenColumnWidth;

  /**
   * <p>
   * Creates a {@link FluidGridConfiguration} with the defined mode and switch values.
   * </p>
   *
   * @param mode the {@link LayoutMode} to use with the {@link FluidGridLayout}. Must not be <code>null</code>.
   * @param eightColumnWidth the pixel value that defines the threshold for the use of 8 instead of 4 columns.
   * @param sixteenColumnWidth the pixel value that defines the threshold for the use of 16 instead of 8 columns.
   *
   * @throws IllegalArgumentException when eightColumnWidth >= sixteenColumnWidth
   */
  public FluidGridConfiguration( LayoutMode mode, int eightColumnWidth, int sixteenColumnWidth ) {
    whenNull( mode ).throwIllegalArgument( "LayoutMode must not be null" );
    when( eightColumnWidth <= 0 ).throwIllegalArgument( "eightColumnWith must be > 0" );
    when( sixteenColumnWidth <= 0 ).throwIllegalArgument( "sixteenColumnWidth must be > 0" );
    when( sixteenColumnWidth <= eightColumnWidth ).throwIllegalArgument( "sixteenColumnWidth must be > eightColumnWidth" );
    this.mode = mode;
    this.eightColumnWidth = eightColumnWidth;
    this.sixteenColumnWidth = sixteenColumnWidth;
  }

  /**
   * <p>
   * Returns the defined {@link LayoutMode}
   * </p>
   */
  public LayoutMode getMode() {
    return mode;
  }

  /**
   * <p>
   * Returns the pixel value when to use 8 columns in a {@link FluidGridLayout}.
   * </p>
   */
  public int getEightColumnWidth() {
    return eightColumnWidth;
  }

  /**
   * <p>
   * Returns the pixel value when to use 16 columns in a {@link FluidGridLayout}.
   * </p>
   */
  public int getSixteenColumnWidth() {
    return sixteenColumnWidth;
  }

}

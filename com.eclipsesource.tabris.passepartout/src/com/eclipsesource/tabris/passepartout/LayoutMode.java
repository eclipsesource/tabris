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


/**
 * <p>
 * A {@link LayoutMode} is used in the configuration of a {@link FluidGridLayout} to influence it's behavior.
 * </p>
 *
 * @see FluidGridConfiguration
 *
 * @since 0.9
 */
public enum LayoutMode {

  /**
   * <p>
   * When AUTO is used in a {@link FluidGridConfiguration} the {@link FluidGridLayout} will take care about relayouting
   * when the parent's size changes.
   * </p>
   */
  AUTO,

  /**
   * <p>
   * If you have e.g. a global resize listener that relayouts your application you don't need the
   * {@link FluidGridLayout} to do a relayout on it's own. For this reason you can use NONE and the auto relayouting
   * will be disabled.
   * </p>
   */
  NONE
}

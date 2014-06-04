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
package com.eclipsesource.tabris.ui;

import java.io.Serializable;


/**
 * <p>
 * An {@link ActionListener} can be used to get notified when the user executes an {@link Action} in the UI.
 * {@link ActionListener} object are usually added to a {@link UIConfiguration}.
 * method.
 * </p>
 *
 * @see UIConfiguration
 * @see Action
 *
 * @since 1.4
 */
public interface ActionListener extends Serializable {

  /**
   * <p>
   * Will be called after an {@link Action} was executed.
   * </p>
   *
   * @param ui the {@link UI} the action lives in.
   * @param action the action that was executed.
   */
  void executed( UI ui, Action action );

}

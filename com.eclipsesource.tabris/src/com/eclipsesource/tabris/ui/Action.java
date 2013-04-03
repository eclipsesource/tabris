/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.ui;


/**
 * <p>
 * An {@link Action} is the abstraction of a user's interaction within a Tabris UI. Actions can have different visual
 * representations rendered differently by each platform. As it is with {@link Page} types they need to be registered
 * with their type and will be created by the framework. Therefore an implementation needs to provide a no-argument
 * constructor.
 * </p>
 * <p>
 * Usually this interface will not be implemented directly. Mostly an {@link AbstractAction} can be used which provides
 * methods for handling the UI.
 * </p>
 *
 * @see ActionConfiguration
 * @see AbstractAction
 *
 * @since 0.11
 */
public interface Action {

  /**
   * <p>
   * Will be called by the Tabris UI when the user executes this action.
   * </p>
   *
   * @param ui the ui to control the application flow and share data.
   *
   * @since 1.0
   */
  void execute( UI ui );

}

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
 * An {@link Action} is the abstraction of a user's interaction with a Tabris UI. Actions can have different visual
 * representations rendered differently by each platform. As it is with {@link Page} types they need to be registered
 * with their type and will be created by the framework. Therefore an implementation needs to provide a no-argument
 * constructor.
 * </p>
 * <p>
 * During runtime you can influence an {@link Action} by changing it's visibility or it's enablement using the
 * {@link ActionManager}.
 * </p>
 *
 * @see ActionConfiguration
 * @see ActionManager
 *
 * @since 0.11
 */
public interface Action {

  /**
   * <p>
   * Will be called by the Tabris UI when a User presses the visual representation of this action.
   * </p>
   *
   * @param context the context to control the application flow and share data.
   */
  void execute( UIContext context );

}

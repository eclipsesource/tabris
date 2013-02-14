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

import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.Store;


/**
 * <p>
 * The {@link UIContext} is a shared object that you will find all over the Tabris UI API. It's your control for the
 * whole UI once it was created. You can use it to store global data for using it in a {@link Page} or to navigate
 * through your application.
 * </p>
 *
 * @since 0.11
 */
public interface UIContext {

  /**
   * <p>
   * Returns the {@link Display} object for the accessing user. You will need this to create e.g. images within the
   * {@link UIConfiguration#configure(UI, UIContext)} method.
   * </p>
   */
  Display getDisplay();

  /**
   * <p>
   * Sets the title for the current displayed UI.
   * </p>
   */
  void setTitle( String title );

  /**
   * <p>
   * Returns the {@link PageManager} for the current session. With this manage you can do all the navigation.
   * </p>
   */
  PageManager getPageManager();

  /**
   * <p>
   * Returns the {@link ActionManager} for the current session. With this you can manage the state of {@link Action}
   * objects.
   * </p>
   */
  ActionManager getActionManager();

  /**
   * <p>
   * Returns the global store for the current session. You can store data e.g in
   * {@link UIConfiguration#configure(UI, UIContext)} and access it form here anywhere in the UI.
   * </p>
   */
  Store getGlobalStore();
}

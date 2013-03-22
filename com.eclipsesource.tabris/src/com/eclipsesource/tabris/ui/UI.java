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


/**
 * <p>
 * The {@link UI} is a shared object that you will find all over the Tabris UI API. It's your control for the
 * whole UI once it was created. You can use it to store global data for using it in a {@link Page} or to navigate
 * through your application.
 * </p>
 *
 * @since 0.11
 */
public interface UI {

  /**
   * <p>
   * Returns the {@link Display} object for the accessing user.
   * </p>
   *
   * @since 1.0
   */
  Display getDisplay();

  /**
   * <p>
   * Returns the {@link PageOperator} for the current session. With this manage you can do all the navigation.
   * </p>
   *
   * @since 1.0
   */
  PageOperator getPageOperator();

  /**
   * <p>
   * Returns the {@link ActionOperator} for the current session. With this you can manage the state of {@link Action}
   * objects.
   * </p>
   *
   * @since 1.0
   */
  ActionOperator getActionOperator();

}

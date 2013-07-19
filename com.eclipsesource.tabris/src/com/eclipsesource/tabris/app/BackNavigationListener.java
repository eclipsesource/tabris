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
package com.eclipsesource.tabris.app;

import java.io.Serializable;


/**
 * <p>
 * A {@link BackNavigationListener} is registered using
 * {@link App#addBackNavigationListener(BackNavigationListener)} and will be notified when the user presses
 * the back button if available on the device.
 * </p>
 *
 * @see App
 *
 * @since 1.0
 */
public interface BackNavigationListener extends Serializable {

  /**
   * <p>
   * Gets called when the user pressed the back button.
   * </p>
   */
  void navigatedBack();
}

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
 * An enum to define the placement priority of Tabris UI elements like actions. The {@link PlacementPriority} will be
 * interpreted in a platform typical way or ignored of the client platform does not know anything about priorities.
 * </p>
 *
 * <p>
 * A typical example for actions would be that {@link PlacementPriority#LOW} actions will go into a menu instead to be
 * always visible in a toolbar.
 * </p>
 *
 * @since 1.2
 */
public enum PlacementPriority {

  /**
   * <p>
   * High priority means that the element should be placed at a significant position in the UI.
   * </p>
   */
  HIGH,

  /**
   * <p>
   * An element with a low placement priority will be placed at a non-significant position in the UI.
   * </p>
   */
  LOW
}

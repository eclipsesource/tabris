/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.app;

import java.awt.Event;
import java.io.Serializable;


/**
 * <p>
 * An {@link AppListener} will be called when an {@link AppEvent} was fired within the App. Usually an
 * {@link AppListener} will be registered with the {@link App#addEventListener(EventType, AppListener)} method.
 * </p>
 *
 * @see App
 *
 * @since 0.10
 */
public interface AppListener extends Serializable {

  /**
   * <p>
   * Will be called when an event was fired within the App.
   * </p>
   *
   * @param event the specific {@link Event} that was fired.
   */
  void handleEvent( AppEvent event );
}

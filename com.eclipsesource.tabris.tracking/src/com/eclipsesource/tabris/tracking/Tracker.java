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
package com.eclipsesource.tabris.tracking;

import java.io.Serializable;


/**
 * <p>
 * A {@link Tracker} is responsible to process {@link TrackingEvent}s. A {@link Tracker} usually will be added to a
 * {@link Tracking} constructor.
 * </p>
 *
 * @see Tracking
 *
 * @since 1.4
 */
public interface Tracker extends Serializable {

  /**
   * <p>
   * Will be called by a {@link Tracking} asynchronously. Implementations should handle the {@link TrackingEvent}
   * properly e.g. submitting the details to a service.
   * </p>
   *
   * @param event the {@link TrackingEvent} to track. Will never be <code>null</code>.
   */
  void handleEvent( TrackingEvent event );

}

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
package com.eclipsesource.tabris.geolocation;

/**
 * <p>
 * The {@link GeolocationListener} is used to notify the server side about location updates from the mobile device.
 * It will be called in case of success and error. Typically a listener object will be added to a {@link Geolocation}
 * instance.
 * </p>
 *
 * @see Geolocation
 * @since 1.0
 */
public interface GeolocationListener {

  /**
   * <p>
   * Gets called when the client updates it's location.
   * </p>
   *
   * @see Position
   */
  public void positionReceived( Position position );

  /**
   * <p>
   * Gets called when there was an error during determining the location on the mobile device.
   * </p>
   *
   * @see PositionError
   */
  public void errorReceived( PositionError error );
}

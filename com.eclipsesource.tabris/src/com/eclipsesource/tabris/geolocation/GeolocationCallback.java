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
 * The <code>geolocationCallback</code> is used to notify the server side about location updates from the mobile device.
 * It will be called in case of success and error. Typically a callback object is passed as a parameter within to
 * <code>Geolocation#getCurrentPosition</code> or <code>Geolocation#watchPosition</code>.
 * </p>
 * 
 * @see Geolocation
 * @since 0.6
 */
public interface GeolocationCallback {
  
  /**
   * <p>
   * Gets called when the client updates it's location.
   * </p>
   * 
   * @see Position
   */
  public void onSuccess( Position position );
  
  /**
   * <p>
   * Gets called when there was an error during determining the location on the mobile device.
   * </p>
   * 
   * @see PositionError
   */
  public void onError( PositionError error );
}

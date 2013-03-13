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
 * The <code>GeolocationOptions</code> will be used as configuration for client devices to determine the location. 
 * Instances are usually passed to <code>Geolocation#getCurrentPosition</code> or 
 * <code>Geolocation#watchPosition</code>.
 * </p>
 * 
 * @see Geolocation
 * @since 0.6
 */
public class GeolocationOptions {
  
  private int maximumAge;
  private int frequency;
  private boolean enableHighAccuracy;
  
  public GeolocationOptions() {
    enableHighAccuracy = false;
    maximumAge = -1;
    frequency = 10000;
  }
  
  /**
   * <p>
   * Instructs the client how old the location to send can be maximum.   
   * </p>
   * 
   * @param maximumAge The maximum age of a location in milli seconds. Must be a positive value.
   */
  public GeolocationOptions setMaximumAge( int maximumAge ) {
    this.maximumAge = maximumAge;
    return this;
  }
  
  /**
   * <p>
   * When used in <code>Geolocation#watchPosition</code> this instructs the client how frequently location updates
   * should be sent to the server.
   * </p>
   * 
   * @param frequency The interval duration in milli seconds. Must be a positive value.
   */
  public GeolocationOptions setFrequency( int frequency ) {
    this.frequency = frequency;
    return this;
  }
  
  /**
   * <p>
   * Enables high accuracy on a client device. E.g. use configures the GPS to determine the position more accurate.
   * </p>
   */
  public GeolocationOptions enableHighAccuracy() {
    this.enableHighAccuracy = true;
    return this;
  }
  
  /**
   * <p>
   * Returns the configured maximum age allowed for client locations.
   * </p>
   */
  public int getMaximumAge() {
    return maximumAge;
  }

  /**
   * <p>
   * Returns the configured frequency for periodic location updates.
   * </p>
   */
  public int getFrequency() {
    return frequency;
  }

  /**
   * <p>
   * Returns if high accuracy is enabled or not.
   * </p>
   */
  public boolean isHighAccuracyEnabled() {
    return enableHighAccuracy;
  }
  
}

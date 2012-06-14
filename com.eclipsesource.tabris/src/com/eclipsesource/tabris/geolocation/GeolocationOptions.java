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


public class GeolocationOptions {
  
  private int maximumAge;
  private int frequency;
  private boolean enableHighAccuracy;
  
  
  public GeolocationOptions() {
    enableHighAccuracy = false;
    maximumAge = -1;
    frequency = 10000;
  }
  
  // ms
  public GeolocationOptions setMaximumAge( int maximumAge ) {
    this.maximumAge = maximumAge;
    return this;
  }
  
  // ms
  public GeolocationOptions setFrequency( int frequency ) {
    this.frequency = frequency;
    return this;
  }
  
  public GeolocationOptions enableHighAccuracy() {
    this.enableHighAccuracy = true;
    return this;
  }
  
  public int getMaximumAge() {
    return maximumAge;
  }

  public int getFrequency() {
    return frequency;
  }

  public boolean isEnableHighAccuracy() {
    return enableHighAccuracy;
  }
  
}

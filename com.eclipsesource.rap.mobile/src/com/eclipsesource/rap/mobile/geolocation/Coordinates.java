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
package com.eclipsesource.rap.mobile.geolocation;


public class Coordinates {
  
  private final double latitude;
  private final double longitude;
  private final double altitude;
  private final double accuracy;
  private final double altitudeAccuracy;
  private final double heading;
  private final double speed;
  
  public Coordinates( double latitude, 
                      double longitude, 
                      double altitude, 
                      double accuracy,
                      double altitudeAccuracy, 
                      double heading, 
                      double speed ) 
  {
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
    this.accuracy = accuracy;
    this.altitudeAccuracy = altitudeAccuracy;
    this.heading = heading;
    this.speed = speed;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getAltitude() {
    return altitude;
  }
  
  public double getAccuracy() {
    return accuracy;
  }

  public double getAltitudeAccuracy() {
    return altitudeAccuracy;
  }
  
  public double getHeading() {
    return heading;
  }
  
  public double getSpeed() {
    return speed;
  }
  
}

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
import java.util.Locale;

import org.eclipse.swt.graphics.Point;

import com.eclipsesource.tabris.device.ClientDevice.Platform;


/**
 * @since 1.4
 */
public class TrackingInfo implements Serializable {

  private String clientId;
  private Platform platform;
  private Point screenResolution;
  private String deviceVendor;
  private String deviceModel;
  private String deviceOsVersion;
  private int clientTimezoneOffset;
  private Locale clientLocale;
  private float scaleFactor;
  private String appId;
  private String appVersion;
  private String tabrisVersion;
  private String searchQuery;
  private String userAgent;
  private String clientIp;

  public String getClientId() {
    return clientId;
  }

  public void setClientId( String clientId ) {
    this.clientId = clientId;
  }

  public Platform getPlatform() {
    return platform;
  }

  public void setPlatform( Platform platform ) {
    this.platform = platform;
  }

  public Point getScreenResolution() {
    return screenResolution;
  }

  public void setScreenResolution( Point screenResolution ) {
    this.screenResolution = screenResolution;
  }

  public String getDeviceVendor() {
    return deviceVendor;
  }

  public void setDeviceVendor( String deviceVendor ) {
    this.deviceVendor = deviceVendor;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setDeviceModel( String deviceModel ) {
    this.deviceModel = deviceModel;
  }

  public String getDeviceOsVersion() {
    return deviceOsVersion;
  }

  public void setDeviceOsVersion( String deviceOsVersion ) {
    this.deviceOsVersion = deviceOsVersion;
  }

  public int getClientTimezoneOffset() {
    return clientTimezoneOffset;
  }

  public void setClientTimezoneOffset( int clientTimezoneOffset ) {
    this.clientTimezoneOffset = clientTimezoneOffset;
  }

  public Locale getClientLocale() {
    return clientLocale;
  }

  public void setClientLocale( Locale clientLocale ) {
    this.clientLocale = clientLocale;
  }

  public float getScaleFactor() {
    return scaleFactor;
  }

  public void setScaleFactor( float scaleFactor ) {
    this.scaleFactor = scaleFactor;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId( String appId ) {
    this.appId = appId;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion( String appVersion ) {
    this.appVersion = appVersion;
  }

  public String getTabrisVersion() {
    return tabrisVersion;
  }

  public void setTabrisVersion( String tabrisVersion ) {
    this.tabrisVersion = tabrisVersion;
  }

  public String getSearchQuery() {
    return searchQuery;
  }

  public void setSearchQuery( String searchQuery ) {
    this.searchQuery = searchQuery;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent( String userAgent ) {
    this.userAgent = userAgent;
  }

  public String getClientIp() {
    return clientIp;
  }

  public void setClientIp( String clientIp ) {
    this.clientIp = clientIp;
  }

}

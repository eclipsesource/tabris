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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Locale;

import org.eclipse.swt.graphics.Point;
import org.junit.Test;

import com.eclipsesource.tabris.device.ClientDevice.Platform;


public class TrackingInfoTest {

  @Test
  public void testDefaultClientIdIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String clientId = trackingInfo.getClientId();

    assertNull( clientId );
  }

  @Test
  public void testCanSetClientId() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setClientId( "foo" );

    String clientId = trackingInfo.getClientId();
    assertEquals( "foo", clientId );
  }

  @Test
  public void testDefaultPlatformIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    Platform platform = trackingInfo.getPlatform();

    assertNull( platform );
  }

  @Test
  public void testCanSetPlatform() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setPlatform( Platform.ANDROID );

    Platform platform = trackingInfo.getPlatform();
    assertSame( Platform.ANDROID, platform );
  }

  @Test
  public void testDefaultScreenResolutionIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    Point screenResolution = trackingInfo.getScreenResolution();

    assertNull( screenResolution );
  }

  @Test
  public void testCanSetScreenResolution() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setScreenResolution( new Point( 100, 100 ) );

    Point screenResolution = trackingInfo.getScreenResolution();
    assertEquals( new Point( 100, 100 ), screenResolution );
  }

  @Test
  public void testDefaultVendorIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String deviceVendor = trackingInfo.getDeviceVendor();

    assertNull( deviceVendor );
  }

  @Test
  public void testCanSetVendor() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setDeviceVendor( "foo" );

    String deviceVendor = trackingInfo.getDeviceVendor();
    assertEquals( "foo", deviceVendor );
  }

  @Test
  public void testDefaultModelIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String model = trackingInfo.getDeviceModel();

    assertNull( model );
  }

  @Test
  public void testCanSetModel() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setDeviceModel( "foo" );

    String deviceModel = trackingInfo.getDeviceModel();
    assertEquals( "foo", deviceModel );
  }

  @Test
  public void testDefaultOsVersionIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String version = trackingInfo.getDeviceOsVersion();

    assertNull( version );
  }

  @Test
  public void testCanSetOsVersion() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setDeviceOsVersion( "foo" );

    String version = trackingInfo.getDeviceOsVersion();
    assertEquals( "foo", version );
  }

  @Test
  public void testDefaultTimezoneOffsetIsZero() {
    TrackingInfo trackingInfo = new TrackingInfo();

    int offset = trackingInfo.getClientTimezoneOffset();

    assertEquals( 0, offset );
  }

  @Test
  public void testCanSetTimezoneOffset() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setClientTimezoneOffset( 23 );

    int offset = trackingInfo.getClientTimezoneOffset();
    assertEquals( 23, offset );
  }

  @Test
  public void testDefaultScaleFactorIsZero() {
    TrackingInfo trackingInfo = new TrackingInfo();

    float scaleFactor = trackingInfo.getScaleFactor();

    assertEquals( 0, scaleFactor, 0 );
  }

  @Test
  public void testCanSetScaleFactor() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setScaleFactor( 23.23F );

    float scaleFactor = trackingInfo.getScaleFactor();
    assertEquals( 23.23F, scaleFactor, 0 );
  }

  @Test
  public void testDefaultLocaleIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    Locale locale = trackingInfo.getClientLocale();

    assertNull( locale );
  }

  @Test
  public void testCanSetLocale() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setClientLocale( Locale.CANADA );

    Locale locale = trackingInfo.getClientLocale();
    assertSame( Locale.CANADA, locale );
  }

  @Test
  public void testDefaultAppIdNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String appId = trackingInfo.getAppId();

    assertNull( appId );
  }

  @Test
  public void testCanSetAppId() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setAppId( "foo" );

    String appId = trackingInfo.getAppId();
    assertSame( "foo", appId );
  }

  @Test
  public void testDefaultAppVersionNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String appVersion = trackingInfo.getAppVersion();

    assertNull( appVersion );
  }

  @Test
  public void testCanSetAppVersion() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setAppVersion( "foo" );

    String appVersion = trackingInfo.getAppVersion();
    assertSame( "foo", appVersion );
  }

  @Test
  public void testDefaultTabrisVersionNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String tabrisVersion = trackingInfo.getTabrisVersion();

    assertNull( tabrisVersion );
  }

  @Test
  public void testCanSetTabrisVersion() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setTabrisVersion( "foo" );

    String tabrisVersion = trackingInfo.getTabrisVersion();
    assertSame( "foo", tabrisVersion );
  }

  @Test
  public void testDefaultSearchQueryIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String query = trackingInfo.getSearchQuery();

    assertNull( query );
  }

  @Test
  public void testCanSetSearchQuery() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setSearchQuery( "foo" );

    String query = trackingInfo.getSearchQuery();
    assertSame( "foo", query );
  }

  @Test
  public void testDefaultUserAgentIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String userAgent = trackingInfo.getUserAgent();

    assertNull( userAgent );
  }

  @Test
  public void testCanSetUserAgent() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setUserAgent( "foo" );

    String userAgent = trackingInfo.getUserAgent();
    assertSame( "foo", userAgent );
  }

  @Test
  public void testDefaultClientIpIsNull() {
    TrackingInfo trackingInfo = new TrackingInfo();

    String ip = trackingInfo.getClientIp();

    assertNull( ip );
  }

  @Test
  public void testCanSetClientIp() {
    TrackingInfo trackingInfo = new TrackingInfo();

    trackingInfo.setClientIp( "foo" );

    String ip = trackingInfo.getClientIp();
    assertSame( "foo", ip );
  }

}

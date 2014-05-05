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
package com.eclipsesource.tabris.tracking.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.UUID;

import org.eclipse.rap.rwt.client.Client;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.ClientStore;
import com.eclipsesource.tabris.app.App;
import com.eclipsesource.tabris.device.ClientDevice;
import com.eclipsesource.tabris.device.ClientDevice.Platform;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.test.util.TabrisRequest;
import com.eclipsesource.tabris.tracking.TrackingInfo;
import com.eclipsesource.tabris.ui.UI;


public class TrackingInfoFactoryTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private UI ui;
  private ClientStore clientStore;
  private TabrisRequest request;

  @Before
  public void setUp() {
    Client client = mock( Client.class );
    mockApp( client );
    mockDevice( client );
    mockClientStore( client );
    environment.setClient( client );
    request = environment.newRequest();
    mockUi();
  }

  private void mockUi() {
    Display display = new Display();
    ui = mock( UI.class );
    when( ui.getDisplay() ).thenReturn( display );
  }

  private void mockApp( Client client ) {
    App app = mock( App.class );
    when( app.getId() ).thenReturn( "appId" );
    when( app.getTabrisVersion() ).thenReturn( "tabrisVersion" );
    when( app.getVersion() ).thenReturn( "appVersion" );
    when( client.getService( App.class ) ).thenReturn( app );
  }

  private void mockDevice( Client client ) {
    ClientDevice device = mock( ClientDevice.class );
    when( device.getLocale() ).thenReturn( Locale.CANADA );
    when( device.getModel() ).thenReturn( "model" );
    when( device.getOSVersion() ).thenReturn( "osVersion" );
    when( device.getPlatform() ).thenReturn( Platform.ANDROID );
    when( device.getVendor() ).thenReturn( "vendor" );
    doReturn( Integer.valueOf( 23 ) ).when( device ).getTimezoneOffset();
    doReturn( Float.valueOf( 23F ) ).when( device ).getScaleFactor();
    when( client.getService( ClientDevice.class ) ).thenReturn( device );
  }

  private void mockClientStore( Client client ) {
    clientStore = mock( ClientStore.class );
    when( client.getService( ClientStore.class ) ).thenReturn( clientStore );
  }

  @Test
  public void testCreatesNewClientIdOnFirstAccess() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String clientId = info.getClientId();

    assertNotNull( UUID.fromString( clientId ) );
  }

  @Test
  public void testStoresNewClientIdInClientStore() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String clientId = info.getClientId();

    verify( clientStore ).add( TrackingInfoFactory.PROPERTY_ID, clientId );
  }

  @Test
  public void testUsesClientIdFromClientStore() {
    String id = UUID.randomUUID().toString();
    when( clientStore.get( TrackingInfoFactory.PROPERTY_ID ) ).thenReturn( id );
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String clientId = info.getClientId();

    assertEquals( id, clientId );
  }

  @Test
  public void testObtainsAppId() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String appId = info.getAppId();

    assertEquals( "appId", appId );
  }

  @Test
  public void testObtainsTabrisVersion() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String tabrisVersion = info.getTabrisVersion();

    assertEquals( "tabrisVersion", tabrisVersion );
  }

  @Test
  public void testObtainsAppVersion() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String appVersion = info.getAppVersion();

    assertEquals( "appVersion", appVersion );
  }

  @Test
  public void testObtainsLocale() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    Locale clientLocale = info.getClientLocale();

    assertSame( Locale.CANADA, clientLocale );
  }

  @Test
  public void testObtainsModel() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String deviceModel = info.getDeviceModel();

    assertEquals( "model", deviceModel );
  }

  @Test
  public void testObtainsOsVersion() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String deviceOsVersion = info.getDeviceOsVersion();

    assertEquals( "osVersion", deviceOsVersion );
  }

  @Test
  public void testObtainsVendor() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String deviceVendor = info.getDeviceVendor();

    assertEquals( "vendor", deviceVendor );
  }

  @Test
  public void testObtainsPlatform() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    Platform platform = info.getPlatform();

    assertSame( Platform.ANDROID, platform );
  }

  @Test
  public void testObtainsTimezoneOffset() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    int clientTimezoneOffset = info.getClientTimezoneOffset();

    assertEquals( 23, clientTimezoneOffset );
  }

  @Test
  public void testObtainsScaleFactor() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    float scaleFactor = info.getScaleFactor();

    assertEquals( 23F, scaleFactor, 0 );
  }

  @Test
  public void testObtainsScreenResolution() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    Point screenResolution = info.getScreenResolution();

    Rectangle bounds = ui.getDisplay().getBounds();
    Point expectedResolution = new Point( bounds.width, bounds.height );
    assertEquals( expectedResolution, screenResolution );
  }

  @Test
  public void testObtainsUserAgent() {
    request.setHeader( "User-Agent", "foo" );
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String userAgent = info.getUserAgent();

    assertEquals( "foo", userAgent );
  }

  @Test
  public void testObtainsClientIpFrom_X_Forwarded_For() {
    request.setHeader( "X-Forwarded-For", "12.34.56.78" );
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String ip = info.getClientIp();

    assertEquals( "12.34.56.78", ip );
  }

  @Test
  public void testObtainsClientIpFrom_Proxy_Client_IP() {
    request.setHeader( "Proxy-Client-IP", "12.34.56.78" );
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String ip = info.getClientIp();

    assertEquals( "12.34.56.78", ip );
  }

  @Test
  public void testObtainsClientIpFrom_WL_Proxy_Client_IP() {
    request.setHeader( "WL-Proxy-Client-IP", "12.34.56.78" );
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String ip = info.getClientIp();

    assertEquals( "12.34.56.78", ip );
  }

  @Test
  public void testObtainsClientIpFrom_HTTP_CLIENT_IP() {
    request.setHeader( "HTTP_CLIENT_IP", "12.34.56.78" );
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String ip = info.getClientIp();

    assertEquals( "12.34.56.78", ip );
  }

  @Test
  public void testObtainsClientIpFrom_HTTP_X_FORWARDED_FOR() {
    request.setHeader( "HTTP_X_FORWARDED_FOR", "12.34.56.78" );
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String ip = info.getClientIp();

    assertEquals( "12.34.56.78", ip );
  }

  @Test
  public void testObtainsClientIpFromRemoteAddressWithoutHeaders() {
    TrackingInfo info = TrackingInfoFactory.createInfo( ui );

    String ip = info.getClientIp();

    assertNull( ip );
  }
}

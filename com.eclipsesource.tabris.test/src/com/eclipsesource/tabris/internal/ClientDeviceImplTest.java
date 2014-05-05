/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.Locale;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.device.ClientDevice;
import com.eclipsesource.tabris.device.ClientDevice.Capability;
import com.eclipsesource.tabris.device.ClientDevice.ConnectionType;
import com.eclipsesource.tabris.device.ClientDevice.Orientation;
import com.eclipsesource.tabris.device.ClientDevice.Platform;
import com.eclipsesource.tabris.device.ClientDeviceListener;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.test.util.TabrisRequest;


public class ClientDeviceImplTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( ClientDeviceImpl.class ) );
  }

  @Test
  public void testClientDeviceListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( ClientDeviceListener.class ) );
  }

  @Test
  public void testGetPlatformIsAndroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android" );
    ClientDevice device = new ClientDeviceImpl();

    Platform platform = device.getPlatform();

    assertSame( Platform.ANDROID, platform );
  }

  @Test
  public void testGetPlatformIsIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios" );
    ClientDevice device = new ClientDeviceImpl();

    Platform platform = device.getPlatform();

    assertSame( Platform.IOS, platform );
  }

  @Test
  public void testGetPlatformIsSWT() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.swt" );
    ClientDevice device = new ClientDeviceImpl();

    Platform platform = device.getPlatform();

    assertSame( Platform.SWT, platform );
  }

  @Test
  public void testGetPlatformIsWebByDefault() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "Mozilla/bla" );
    ClientDevice device = new ClientDeviceImpl();

    Platform platform = device.getPlatform();

    assertSame( Platform.WEB, platform );
  }

  @Test
  public void testGetLocaleReturnsNullWhenLocaleNotSet() {
    environment.newGetRequest();
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();

    Locale locale = deviceImpl.getLocale();

    assertNull( locale );
  }

  @Test
  public void testGetLocalesReturnsEmptyArrayWhenLocaleNotSet() {
    environment.newGetRequest();
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();

    Locale[] locales = deviceImpl.getLocales();

    assertEquals( 0, locales.length );
  }

  @Test
  public void testGetLocaleReadsLocaleFromRequest() {
    TabrisRequest request = environment.newGetRequest();
    request.setHeader( "Accept-Language", "anything" );
    request.setLocales( new Locale( "en-US" ) );
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();

    Locale locale = deviceImpl.getLocale();

    assertEquals( new Locale( "en-US" ), locale );
  }

  @Test
  public void testGetLocalesReadsLocalesFromRequest() {
    TabrisRequest request = environment.newGetRequest();
    request.setHeader( "Accept-Language", "anything" );
    request.setLocales( new Locale( "en-US" ), new Locale( "de-DE" ) );
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();

    Locale[] locales = deviceImpl.getLocales();

    assertEquals( 2, locales.length );
    assertEquals( new Locale( "en-US" ), locales[ 0 ] );
    assertEquals( new Locale( "de-DE" ), locales[ 1 ] );
  }

  @Test
  public void testReturnsSaveLocalesCopy() {
    TabrisRequest request = environment.newGetRequest();
    request.setHeader( "Accept-Language", "anything" );
    request.setLocales( new Locale( "en-US" ) );
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();

    deviceImpl.getLocales()[ 0 ] = new Locale( "de-DE" );

    Locale[] locales = deviceImpl.getLocales();
    assertEquals( new Locale( "en-US" ), locales[ 0 ] );
  }

  @Test( expected = IllegalStateException.class )
  public void testGetTimezoneOffsetFailsWhenTimezoneOffsetNotSet() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();

    deviceImpl.getTimezoneOffset();
  }

  @Test
  public void testGetTimezoneOffset_readsTimezoneOffsetFromHandler() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();
    JsonObject parameters = new JsonObject();
    parameters.add( "timezoneOffset", -90 );

    environment.dispatchSetOnServiceObject( parameters );
    int timezoneOffset = deviceImpl.getTimezoneOffset();

    assertEquals( -90, timezoneOffset );
  }

  @Test
  public void testDetectsPortraitMode() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    properties.add( "orientation", "PORTRAIT" );
    environment.dispatchSetOnServiceObject( properties );

    Orientation orientation = deviceImpl.getOrientation();

    assertSame( Orientation.PORTRAIT, orientation );
  }

  @Test
  public void testDetectsLandscapeMode() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    properties.add( "orientation", "LANDSCAPE" );
    environment.dispatchSetOnServiceObject( properties );

    Orientation orientation = deviceImpl.getOrientation();

    assertSame( Orientation.LANDSCAPE, orientation );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutConnectionType() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();

    deviceImpl.getConnectionType();
  }

  @Test
  public void testKnowsConnectionTypeWifi() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    properties.add( "connectionType", "WIFI" );

    environment.dispatchSetOnServiceObject( properties );
    ConnectionType connectionType = deviceImpl.getConnectionType();

    assertSame( ConnectionType.WIFI, connectionType );
  }

  @Test
  public void testKnowsConnectionTypeCelular() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    properties.add( "connectionType", "CELLULAR" );

    environment.dispatchSetOnServiceObject( properties );
    ConnectionType connectionType = deviceImpl.getConnectionType();

    assertSame( ConnectionType.CELLULAR, connectionType );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutCapabilities() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();

    deviceImpl.hasCapability( Capability.LOCATION );
  }

  @Test
  public void testCanFindCapabilities() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add( "CAMERA" );
    jsonArray.add( "LOCATION" );
    properties.add( "capabilities", jsonArray );

    environment.dispatchSetOnServiceObject( properties );

    assertTrue( deviceImpl.hasCapability( Capability.LOCATION ) );
    assertTrue( deviceImpl.hasCapability( Capability.CAMERA ) );
    assertFalse( deviceImpl.hasCapability( Capability.MAPS ) );
    assertFalse( deviceImpl.hasCapability( Capability.MESSAGE ) );
    assertFalse( deviceImpl.hasCapability( Capability.PHONE ) );
  }

  @Test
  public void testCanFindAllCapabilities() {
    ClientDeviceImpl deviceImpl = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add( "CAMERA" );
    jsonArray.add( "LOCATION" );
    jsonArray.add( "MAPS" );
    jsonArray.add( "MESSAGE" );
    jsonArray.add( "PHONE" );
    properties.add( "capabilities", jsonArray );

    environment.dispatchSetOnServiceObject( properties );

    assertTrue( deviceImpl.hasCapability( Capability.LOCATION ) );
    assertTrue( deviceImpl.hasCapability( Capability.CAMERA ) );
    assertTrue( deviceImpl.hasCapability( Capability.MAPS ) );
    assertTrue( deviceImpl.hasCapability( Capability.MESSAGE ) );
    assertTrue( deviceImpl.hasCapability( Capability.PHONE ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddListenerFailsWithNull() {
    ClientDevice device = new ClientDeviceImpl();

    device.addClientDeviceListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveListenerFailsWithNull() {
    ClientDevice device = new ClientDeviceImpl();

    device.removeClientDeviceListener( null );
  }

  @Test
  public void testOrientationChangeNotifiesListener() {
    ClientDeviceImpl device = new ClientDeviceImpl();
    ClientDeviceListener listener = mock( ClientDeviceListener.class );
    device.addClientDeviceListener( listener );
    JsonObject properties = new JsonObject();
    properties.add( "orientation", "PORTRAIT" );

    environment.dispatchSetOnServiceObject( properties );

    verify( listener ).orientationChange( Orientation.PORTRAIT );
  }

  @Test
  public void testConnectionTypeChangeNotifiesListener() {
    ClientDeviceImpl device = new ClientDeviceImpl();
    ClientDeviceListener listener = mock( ClientDeviceListener.class );
    device.addClientDeviceListener( listener );
    JsonObject properties = new JsonObject();
    properties.add( "connectionType", "WIFI" );

    environment.dispatchSetOnServiceObject( properties );

    verify( listener ).connectionTypeChanged( ConnectionType.WIFI );
  }

  @Test
  public void testSetsVendor() {
    ClientDeviceImpl device = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    properties.add( "vendor", "fooBar" );

    device.handleSet( properties );

    String vendor = device.getVendor();
    assertEquals( "fooBar", vendor );
  }

  @Test
  public void testSetsModel() {
    ClientDeviceImpl device = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    properties.add( "model", "blub" );

    device.handleSet( properties );

    String model = device.getModel();
    assertEquals( "blub", model );
  }

  @Test
  public void testSetsOSVersion() {
    ClientDeviceImpl device = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    properties.add( "osVersion", "7.1.0" );

    device.handleSet( properties );

    String version = device.getOSVersion();
    assertEquals( "7.1.0", version );
  }

  @Test
  public void testSetsScaleFactor() {
    ClientDeviceImpl device = new ClientDeviceImpl();
    JsonObject properties = new JsonObject();
    properties.add( "scaleFactor", 2.13F );

    device.handleSet( properties );

    float scaleFactor = device.getScaleFactor();
    assertEquals( 2.13F, scaleFactor, 0F );
  }
}

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

import static com.eclipsesource.tabris.test.TabrisTestUtil.mockRemoteObject;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.geolocation.PositionError.PositionErrorCode;


@SuppressWarnings("restriction")
public class GeolocationTest {
  
  @Before
  public void setUp() {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testInitialFlavor() {
    RemoteObject remoteObject = mockRemoteObject();
    
    new Geolocation();
    
    verify( remoteObject ).set( "needsPosition", "NEVER" );
  }
  
  @Test
  public void testSetsOptionsWhenGetLocation() {
    RemoteObject remoteObject = mockRemoteObject();
    Geolocation geolocation = new Geolocation();
    GeolocationCallback callback = mock( GeolocationCallback.class );
    GeolocationOptions options = new GeolocationOptions();
    
    geolocation.getCurrentPosition( callback, options );
    
    verify( remoteObject ).set( "needsPosition", "ONCE" );
    verify( remoteObject ).set( "frequency", options.getFrequency() );
    verify( remoteObject ).set( "maximumAge", options.getMaximumAge() );
    verify( remoteObject ).set( "enableHighAccuracy", options.isEnableHighAccuracy() );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testGetLocationFailsWithNullOptions() {
    Geolocation geolocation = new Geolocation();
    
    geolocation.getCurrentPosition( mock( GeolocationCallback.class ), null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testGetLocationFailsWithNullCallback() {
    Geolocation geolocation = new Geolocation();
    
    geolocation.getCurrentPosition( null, new GeolocationOptions() );
  }
  
  @Test
  public void testSetsOptionsWhenWatch() {
    RemoteObject remoteObject = mockRemoteObject();
    Geolocation geolocation = new Geolocation();
    GeolocationCallback callback = mock( GeolocationCallback.class );
    GeolocationOptions options = new GeolocationOptions();
    
    geolocation.watchPosition( callback, options );
    
    verify( remoteObject ).set( "needsPosition", "CONTINUOUS" );
    verify( remoteObject ).set( "frequency", options.getFrequency() );
    verify( remoteObject ).set( "maximumAge", options.getMaximumAge() );
    verify( remoteObject ).set( "enableHighAccuracy", options.isEnableHighAccuracy() );
  }
  
  
  @Test( expected = IllegalArgumentException.class )
  public void testWatchFailsWithNullOptions() {
    Geolocation geolocation = new Geolocation();
    
    geolocation.watchPosition( mock( GeolocationCallback.class ), null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testWatchFailsWithNullCallback() {
    Geolocation geolocation = new Geolocation();
    
    geolocation.watchPosition( null, new GeolocationOptions() );
  }
  
  @Test
  public void testSetsNeverWhenClearWatch() {
    RemoteObject remoteObject = mockRemoteObject();
    Geolocation geolocation = new Geolocation();
    
    geolocation.clearWatch();
    
    verify( remoteObject, times( 2 ) ).set( "needsPosition", "NEVER" );
  }
  
  @Test
  public void testDisposeRendersDestroy() {
    RemoteObject remoteObject = mockRemoteObject();
    Geolocation geolocation = new Geolocation();
    
    geolocation.dispose();
    
    verify( remoteObject ).destroy();
  }
  
  @Test
  public void testDelegatesPositionWhenGetPosition() throws ParseException {
    GeolocationCallback callback = mock( GeolocationCallback.class );
    Geolocation geolocation = new Geolocation();
    geolocation.getCurrentPosition( callback, new GeolocationOptions() );
    Map<String, Object> properties = createPositionData();
    
    Fixture.dispatchNotify( geolocation.getRemoteObject(), "LocationUpdate", properties );
    
    ArgumentCaptor<Position> captor = ArgumentCaptor.forClass( Position.class );
    verify( callback ).onSuccess( captor.capture() );
    assertPositionData( captor.getValue() );
  }
  
  @Test
  public void testDelegatesPositionWhenWatch() throws ParseException {
    GeolocationCallback callback = mock( GeolocationCallback.class );
    Geolocation geolocation = new Geolocation();
    geolocation.watchPosition( callback, new GeolocationOptions() );
    Map<String, Object> properties = createPositionData();
    
    Fixture.dispatchNotify( geolocation.getRemoteObject(), "LocationUpdate", properties );
    
    ArgumentCaptor<Position> captor = ArgumentCaptor.forClass( Position.class );
    verify( callback ).onSuccess( captor.capture() );
    assertPositionData( captor.getValue() );
  }

  private Map<String, Object> createPositionData() {
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( "timestamp", "2012-03-12T10:40:13+0100" );
    properties.put( "latitude", Double.valueOf( 101.1 ) );
    properties.put( "longitude", Double.valueOf( 43.1 ) );
    properties.put( "altitude", Integer.valueOf( 3 ) );
    properties.put( "accuracy", Double.valueOf( 5.1 ) );
    properties.put( "altitudeAccuracy", Double.valueOf( 1.1 ) );
    properties.put( "heading", Double.valueOf( 21.1 ) );
    properties.put( "speed", Double.valueOf( 216.1 ) );
    return properties;
  }

  private void assertPositionData( Position position ) throws ParseException {
    assertEquals( position.getTimestamp(), parseDate() );
    assertEquals( position.getCoords().getAccuracy(), 5.1, 0 );
    assertEquals( position.getCoords().getAltitude(), 3, 0 );
    assertEquals( position.getCoords().getAltitudeAccuracy(), 1.1, 0 );
    assertEquals( position.getCoords().getHeading(), 21.1, 0 );
    assertEquals( position.getCoords().getLatitude(), 101.1, 0 );
    assertEquals( position.getCoords().getLongitude(), 43.1, 0 );
    assertEquals( position.getCoords().getSpeed(), 216.1, 0 );
  }

  private Date parseDate() throws ParseException {
    return new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" ).parse( "2012-03-12T10:40:13+0100" );
  }
  
  @Test
  public void testDelegatesPositionErrorWhenGetPosition() {
    GeolocationCallback callback = mock( GeolocationCallback.class );
    Geolocation geolocation = new Geolocation();
    geolocation.getCurrentPosition( callback, new GeolocationOptions() );
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( "errorCode", "UNKNOWN" );
    properties.put( "errorMessage", "A Message" );
    
    Fixture.dispatchNotify( geolocation.getRemoteObject(), "LocationUpdateError", properties );
    
    ArgumentCaptor<PositionError> captor = ArgumentCaptor.forClass( PositionError.class );
    verify( callback ).onError( captor.capture() );
    assertEquals( PositionErrorCode.UNKNOWN, captor.getValue().getCode() );
    assertEquals( "A Message", captor.getValue().getMessage() );
  }
  
  @Test
  public void testDelegatesPositionErrorWhenWatch() {
    GeolocationCallback callback = mock( GeolocationCallback.class );
    Geolocation geolocation = new Geolocation();
    geolocation.watchPosition( callback, new GeolocationOptions() );
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( "errorCode", "UNKNOWN" );
    properties.put( "errorMessage", "A Message" );
    
    Fixture.dispatchNotify( geolocation.getRemoteObject(), "LocationUpdateError", properties );
    
    ArgumentCaptor<PositionError> captor = ArgumentCaptor.forClass( PositionError.class );
    verify( callback ).onError( captor.capture() );
    assertEquals( PositionErrorCode.UNKNOWN, captor.getValue().getCode() );
    assertEquals( "A Message", captor.getValue().getMessage() );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testLocationEventFailsWithoutCallback() {
    Geolocation geolocation = new Geolocation();
    
    Fixture.dispatchNotify( geolocation.getRemoteObject(), "LocationUpdate", null );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testErrorEventFailsWithoutCallback() {
    Geolocation geolocation = new Geolocation();
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( "errorCode", "UNKNOWN" );
    properties.put( "errorMessage", "A Message" );
    
    Fixture.dispatchNotify( geolocation.getRemoteObject(), "LocationUpdateError", properties );
  }
}

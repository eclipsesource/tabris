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
package com.eclipsesource.tabris.internal.geolocation;

import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.LOCATION_UPDATE_ERROR_EVENT;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.LOCATION_UPDATE_EVENT;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_ACCURACY;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_ALTITUDE;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_ALTITUDE_ACCURACY;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_ENABLE_HIGH_ACCURACY;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_ERROR_CODE;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_ERROR_MESSAGE;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_FREQUENCY;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_HEADING;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_LATITUDE;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_LONGITUDE;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_MAXIMUM_AGE;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_NEEDS_POSITION;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_SPEED;
import static com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer.PROP_TIMESTAMP;
import static org.eclipse.rap.rwt.testfixture.Fixture.fakeNotifyOperation;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.protocol.ClientObjectAdapter;
import org.eclipse.rap.rwt.internal.protocol.IClientObject;
import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.geolocation.Geolocation;
import com.eclipsesource.tabris.geolocation.GeolocationCallback;
import com.eclipsesource.tabris.geolocation.GeolocationOptions;
import com.eclipsesource.tabris.geolocation.Position;
import com.eclipsesource.tabris.geolocation.PositionError;
import com.eclipsesource.tabris.internal.geolocation.GeolocationAdapter;
import com.eclipsesource.tabris.internal.geolocation.GeolocationSynchronizer;
import com.eclipsesource.tabris.internal.geolocation.GeolocationAdapter.NeedsPositionFlavor;


@RunWith( MockitoJUnitRunner.class )
@SuppressWarnings("restriction")
public class GeolocationSynchronizerTest {
  
  private static final String ID = "g42";
  
  @Mock private GeolocationAdapter adapter;
  @Mock private Geolocation object;
  private GeolocationSynchronizer synchronizer;

  @Before
  public void setUp() {
    Fixture.setUp();
    when( object.getAdapter( GeolocationAdapter.class ) ).thenReturn( adapter );
    ClientObjectAdapter clientObjectAdapter = mock( ClientObjectAdapter.class );
    when( clientObjectAdapter.getId() ).thenReturn( ID );
    when( object.getAdapter( IClientObjectAdapter.class ) ).thenReturn( clientObjectAdapter );
    synchronizer = spy( new GeolocationSynchronizer( object ) );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testReadData() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( PROP_TIMESTAMP, "2012-03-12T10:40:13+0100" );
    parameters.put( PROP_LATITUDE, "101.1" );
    parameters.put( PROP_LONGITUDE, "43.1" );
    parameters.put( PROP_ALTITUDE, "3.1" );
    parameters.put( PROP_ACCURACY, "5.1" );
    parameters.put( PROP_ALTITUDE_ACCURACY, "1.1" );
    parameters.put( PROP_HEADING, "21.1" );
    parameters.put( PROP_SPEED, "216.1" );
    fakeNotifyOperation( ID, LOCATION_UPDATE_EVENT, parameters );
    
    synchronizer.readData( object );
    
    verify( adapter ).setPosition( any( Position.class ) );
  }
  
  @Test
  public void testReadDataWithError() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( PROP_ERROR_CODE, "UNKNOWN" );
    parameters.put( PROP_ERROR_MESSAGE, "A Message" );
    fakeNotifyOperation( ID, LOCATION_UPDATE_ERROR_EVENT, parameters );
    
    synchronizer.readData( object );
    
    verify( adapter ).setError( any( PositionError.class ) );
  }
  
  @Test
  public void testPreservesValues() {
    Fixture.fakePhase( PhaseId.READ_DATA );
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    synchronizer.preserveValues( object );
    
    assertNotNull( RWT.getServiceStore().getAttribute( ID + "." + PROP_NEEDS_POSITION ) );
  }
  
  @Test
  public void testPreservesOptions() {
    Fixture.fakePhase( PhaseId.READ_DATA );
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    GeolocationOptions options = new GeolocationOptions().setFrequency( 10 ).enableHighAccuracy().setMaximumAge( 10 );
    when( adapter.getOptions() ).thenReturn( options );
    
    synchronizer.preserveValues( object );
    
    assertNotNull( RWT.getServiceStore().getAttribute( ID + "." + PROP_ENABLE_HIGH_ACCURACY ) );
    assertNotNull( RWT.getServiceStore().getAttribute( ID + "." + PROP_FREQUENCY ) );
    assertNotNull( RWT.getServiceStore().getAttribute( ID + "." + PROP_MAXIMUM_AGE ) );
  }
  
  @Test
  public void testRenderInitialization() {
    IClientObject clientObject = mock( IClientObject.class );
    
    synchronizer.renderInitialization( clientObject, object );
    
    verify( clientObject ).create( GeolocationSynchronizer.TYPE );
  }
  
  @Test
  public void testRenderChanges() {
    Fixture.fakePhase( PhaseId.RENDER );
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    GeolocationOptions options = new GeolocationOptions().setFrequency( 10 ).enableHighAccuracy().setMaximumAge( 10 );
    when( adapter.getOptions() ).thenReturn( options );
    
    synchronizer.renderChanges( object );
    
    verify( synchronizer ).renderProperty( eq( PROP_NEEDS_POSITION ), 
                                           any( NeedsPositionFlavor.class ), 
                                           any( NeedsPositionFlavor.class ) );
    verify( synchronizer ).renderProperty( eq( PROP_MAXIMUM_AGE ), 
                                           any( Integer.class ), 
                                           any( Integer.class ) );
    verify( synchronizer ).renderProperty( eq( PROP_FREQUENCY ), 
                                           any( Integer.class ), 
                                           any( Integer.class ) );
  }
  
  @Test
  public void testProcessActionWithOnce() {
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.ONCE );
    Position position = mock( Position.class );
    when( adapter.getPosition() ).thenReturn( position );
    GeolocationCallback callback = mock( GeolocationCallback.class );
    when( adapter.getCallback() ).thenReturn( callback );
    
    synchronizer.processAction( object );
    
    verify( callback ).onSuccess( position );
    verify( adapter ).setPosition( null );
    verify( adapter ).setCallback( null );
    verify( adapter ).setOptions( null );
    verify( adapter ).setFlavor( NeedsPositionFlavor.NEVER );
  }
  
  @Test
  public void testProcessActionWithNever() {
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.NEVER );
    Position position = mock( Position.class );
    when( adapter.getPosition() ).thenReturn( position );
    GeolocationCallback callback = mock( GeolocationCallback.class );
    when( adapter.getCallback() ).thenReturn( callback );
    
    synchronizer.processAction( object );
    
    verify( callback, never() ).onSuccess( position );
    verify( adapter ).setPosition( null );
    verify( adapter, never() ).setCallback( null );
    verify( adapter, never() ).setOptions( null );
    verify( adapter, never() ).setFlavor( NeedsPositionFlavor.NEVER );
  }
  
  @Test
  public void testProcessActionWithContinous() {
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    Position position = mock( Position.class );
    when( adapter.getPosition() ).thenReturn( position );
    GeolocationCallback callback = mock( GeolocationCallback.class );
    when( adapter.getCallback() ).thenReturn( callback );
    
    synchronizer.processAction( object );
    
    verify( callback ).onSuccess( position );
    verify( adapter ).setPosition( null );
    verify( adapter, never() ).setCallback( null );
    verify( adapter, never() ).setOptions( null );
    verify( adapter, never() ).setFlavor( NeedsPositionFlavor.NEVER );
  }
  
  @Test
  public void testProcessActionWithError() {
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    PositionError positionError = mock( PositionError.class );
    when( adapter.getError() ).thenReturn( positionError );
    GeolocationCallback callback = mock( GeolocationCallback.class );
    when( adapter.getCallback() ).thenReturn( callback );
    
    synchronizer.processAction( object );
    
    verify( callback, never() ).onSuccess( any( Position.class ) );
    verify( callback ).onError( positionError );
    verify( adapter ).setPosition( null );
    verify( adapter, never() ).setCallback( null );
    verify( adapter, never() ).setOptions( null );
    verify( adapter, never() ).setFlavor( NeedsPositionFlavor.NEVER );
  }
  
  @Test
  public void testDestroy() {
    Fixture.fakePhase( PhaseId.RENDER );
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    doReturn( Boolean.TRUE ).when( adapter ).isDisposed();
    IClientObject clientObject = mock( IClientObject.class );
    doReturn( clientObject ).when( synchronizer ).getClientObject();
    
    synchronizer.renderChanges( object );
    
    verify( clientObject ).destroy();
  }
  
  @Test
  public void testDestroyedInAdapter() {
    Fixture.fakePhase( PhaseId.RENDER );
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    doReturn( Boolean.TRUE ).when( adapter ).isDisposed();
    IClientObject clientObject = mock( IClientObject.class );
    doReturn( clientObject ).when( synchronizer ).getClientObject();
    
    synchronizer.renderChanges( object );
    
    verify( adapter ).destroy();
  }
  
  @Test( expected = IllegalStateException.class )
  public void testParseDateError() {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( PROP_TIMESTAMP, "2012-12-30-20-15-43" );
    parameters.put( PROP_LATITUDE, "101.1" );
    parameters.put( PROP_LONGITUDE, "43.1" );
    parameters.put( PROP_ALTITUDE, "3.1" );
    parameters.put( PROP_ACCURACY, "5.1" );
    parameters.put( PROP_ALTITUDE_ACCURACY, "1.1" );
    parameters.put( PROP_HEADING, "21.1" );
    parameters.put( PROP_SPEED, "216.1" );
    fakeNotifyOperation( ID, LOCATION_UPDATE_EVENT, parameters );
    
    synchronizer.readData( object );
  }
  
}

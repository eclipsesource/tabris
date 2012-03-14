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
package com.eclipsesource.rap.mobile.geolocation.internal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.protocol.ClientObjectAdapter;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.rap.mobile.geolocation.Geolocation;
import com.eclipsesource.rap.mobile.geolocation.GeolocationCallback;
import com.eclipsesource.rap.mobile.geolocation.GeolocationOptions;
import com.eclipsesource.rap.mobile.geolocation.Position;
import com.eclipsesource.rap.mobile.geolocation.PositionError;
import com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor;


@SuppressWarnings("restriction")
public class GeolocationSynchronizerTest {
  
  private GeolocationSynchronizer synchronizer;
  private Geolocation object;
  private GeolocationAdapter adapter;

  @Before
  public void setUp() {
    Fixture.setUp();
    object = mock( Geolocation.class );
    adapter = mock( GeolocationAdapter.class );
    when( object.getAdapter( GeolocationAdapter.class ) ).thenReturn( adapter );
    when( object.getAdapter( IClientObjectAdapter.class ) ).thenReturn( new ClientObjectAdapter() );
    GeolocationSynchronizer original = new GeolocationSynchronizer( object );
    synchronizer = spy( original );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testReadData() {
    when( synchronizer.readPropertyValue( GeolocationSynchronizer.PROP_LATITUDE ) ).thenReturn( "101.1" );
    String date = "2012-03-12T10:40:13+0100";
    when( synchronizer.readPropertyValue( GeolocationSynchronizer.PROP_TIMESTAMP ) ).thenReturn( date );
    
    synchronizer.readData( object );
    
    verify( adapter ).setPosition( any( Position.class ) );
  }
  
  @Test
  public void testReadDataWithError() {
    when( synchronizer.readPropertyValue( GeolocationSynchronizer.PROP_ERROR_CODE ) ).thenReturn( "UNKNOWN" );
    
    synchronizer.readData( object );
    
    verify( adapter ).setError( any( PositionError.class ) );
  }
  
  @Test
  public void testPreservesValues() {
    Fixture.fakePhase( PhaseId.READ_DATA );
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    synchronizer.preserveValues( object );
    
    assertNotNull( RWT.getServiceStore().getAttribute( synchronizer.getObjectId() 
                                                       + "." 
                                                       + GeolocationSynchronizer.PROP_NEEDS_POSITION ) );
  }
  
  @Test
  public void testPreservesOptions() {
    Fixture.fakePhase( PhaseId.READ_DATA );
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    GeolocationOptions options = new GeolocationOptions().setFrequency( 10 ).enableHighAccuracy().setMaximumAge( 10 );
    when( adapter.getOptions() ).thenReturn( options );
    
    synchronizer.preserveValues( object );
    
    assertNotNull( RWT.getServiceStore().getAttribute( synchronizer.getObjectId() 
                                                       + "." 
                                                       + GeolocationSynchronizer.PROP_ENABLE_HIGH_ACCURACY ) );
    assertNotNull( RWT.getServiceStore().getAttribute( synchronizer.getObjectId() 
                                                       + "." 
                                                       + GeolocationSynchronizer.PROP_FREQUENCY ) );
    assertNotNull( RWT.getServiceStore().getAttribute( synchronizer.getObjectId() 
                                                       + "." 
                                                       + GeolocationSynchronizer.PROP_MAXIMUM_AGE ) );
  }
  
  @Test
  public void testRenderInitialization() {
    IClientObject clientObject = mock( IClientObject.class );
    
    synchronizer.renderInitialization( clientObject, object );
    
    verify( clientObject ).create( GeolocationSynchronizer.TYPE );
  }
  
  @Test
  public void testRenderChanges() {
    Fixture.fakePhase( PhaseId.READ_DATA );
    when( adapter.getFlavor() ).thenReturn( NeedsPositionFlavor.CONTINUOUS );
    GeolocationOptions options = new GeolocationOptions().setFrequency( 10 ).enableHighAccuracy().setMaximumAge( 10 );
    when( adapter.getOptions() ).thenReturn( options );
    
    synchronizer.renderChanges( object );
    
    verify( synchronizer ).renderProperty( eq( GeolocationSynchronizer.PROP_NEEDS_POSITION ), 
                                           any( NeedsPositionFlavor.class ), 
                                           any( NeedsPositionFlavor.class ) );
    verify( synchronizer ).renderProperty( eq( GeolocationSynchronizer.PROP_MAXIMUM_AGE ), 
                                           any( Integer.class ), 
                                           any( Integer.class ) );
    verify( synchronizer ).renderProperty( eq( GeolocationSynchronizer.PROP_FREQUENCY ), 
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
    IClientObject clientObject = mock( IClientObject.class );
    
    synchronizer.destroy( clientObject );
    
    verify( clientObject ).destroy();
  }
  
  @Test( expected = IllegalStateException.class )
  public void testParseError() {
    when( synchronizer.readPropertyValue( GeolocationSynchronizer.PROP_LATITUDE ) ).thenReturn( "101.1" );
    String date = "2012-12-30-20-15-43";
    when( synchronizer.readPropertyValue( GeolocationSynchronizer.PROP_TIMESTAMP ) ).thenReturn( date );
    
    synchronizer.readData( object );
  }
  
}

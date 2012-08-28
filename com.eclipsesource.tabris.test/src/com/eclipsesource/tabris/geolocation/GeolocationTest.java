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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.geolocation.internal.GeolocationAdapter;
import com.eclipsesource.tabris.geolocation.internal.GeolocationAdapter.NeedsPositionFlavor;


public class GeolocationTest {
  
  private Geolocation geolocation;
  private GeolocationAdapter adapter;

  @Before
  public void setUp() {
    Fixture.setUp();
    geolocation = new Geolocation();
    adapter = geolocation.getAdapter( GeolocationAdapter.class );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testInitialFlavor() {
    assertEquals( NeedsPositionFlavor.NEVER, adapter.getFlavor() );
  }
  
  @Test
  public void testGetCurrentPosition() {
    GeolocationCallback callback = mock( GeolocationCallback.class );
    GeolocationOptions options = mock( GeolocationOptions.class );
    
    geolocation.getCurrentPosition( callback, options );
    
    assertEquals( NeedsPositionFlavor.ONCE, adapter.getFlavor() );
    assertEquals( callback, adapter.getCallback() );
    assertEquals( options, adapter.getOptions() );
  }
  
  @Test
  public void testWatch() {
    GeolocationCallback callback = mock( GeolocationCallback.class );
    GeolocationOptions options = mock( GeolocationOptions.class );
    
    geolocation.watchPosition( callback, options );
    
    assertEquals( NeedsPositionFlavor.CONTINUOUS, adapter.getFlavor() );
    assertEquals( callback, adapter.getCallback() );
    assertEquals( options, adapter.getOptions() );
  }
  
  @Test
  public void testClearWatch() {
    GeolocationCallback callback = mock( GeolocationCallback.class );
    GeolocationOptions options = mock( GeolocationOptions.class );
    geolocation.watchPosition( callback, options );
    
    geolocation.clearWatch();
    
    assertEquals( NeedsPositionFlavor.NEVER, adapter.getFlavor() );
    assertNull( adapter.getCallback() );
    assertNull( adapter.getOptions() );
  }
}

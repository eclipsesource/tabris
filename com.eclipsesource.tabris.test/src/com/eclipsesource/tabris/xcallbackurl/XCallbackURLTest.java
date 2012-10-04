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
package com.eclipsesource.tabris.xcallbackurl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.xcallbackurl.internal.XCallbackSyncAdapter;


@SuppressWarnings("restriction")
public class XCallbackURLTest {
  
  @Before
  public void setUp() {
    Fixture.setUp();
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  
  @Test( expected = IllegalArgumentException.class )
  public void testInvalidConfiguration() {
    new XCallbackURL( null );
  }
  
  @Test
  public void testGetConfiguration() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    XCallbackURL xCallbackURL = new XCallbackURL( configuration );
    
    XCallbackConfiguration actualConfiguration = xCallbackURL.getConfigruation();
    
    assertEquals( configuration, actualConfiguration );
  }
  
  @Test
  public void testGetClientAdapter() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    XCallbackURL xCallbackURL = new XCallbackURL( configuration );
    
    assertNotNull( xCallbackURL.getAdapter( IClientObjectAdapter.class ) );
  }
  
  @Test
  public void testGetSyncAdapter() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    XCallbackURL xCallbackURL = new XCallbackURL( configuration );
    
    assertNotNull( xCallbackURL.getAdapter( XCallbackSyncAdapter.class ) );
  }
  
  @Test
  public void testSetsCall() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    XCallbackURL xCallbackURL = new XCallbackURL( configuration );
    XCallbackSyncAdapter adapter = xCallbackURL.getAdapter( XCallbackSyncAdapter.class );
    
    assertFalse( adapter.wantsToCall() );
    xCallbackURL.call();
    assertTrue( adapter.wantsToCall() );
  }
  
  @Test
  public void testSetsCallWithCallback() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    XCallbackURL xCallbackURL = new XCallbackURL( configuration );
    XCallbackSyncAdapter adapter = xCallbackURL.getAdapter( XCallbackSyncAdapter.class );
    XCallback xCallback = mock( XCallback.class );
    
    assertFalse( adapter.wantsToCall() );
    assertNull( adapter.getCallback() );
    xCallbackURL.call( xCallback );
    assertTrue( adapter.wantsToCall() );
    assertNotNull( adapter.getCallback() );
  }
}

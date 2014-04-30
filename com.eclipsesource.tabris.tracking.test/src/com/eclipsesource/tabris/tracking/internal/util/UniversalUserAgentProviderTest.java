/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.eclipsesource.tabris.tracking.TrackingInfo;

public class UniversalUserAgentProviderTest {

  @Test ( expected = IllegalArgumentException.class )
  public void testFailsWithNullTrackingInfo() {
    new UniversalUserAgentProvider().getUserAgent( null );
  }
  
  @Test ( expected = IllegalStateException.class )
  public void testFailsWithNullUserAgent() {
    new UniversalUserAgentProvider().getUserAgent( new TrackingInfo() );
  }
  
  @Test ( expected = IllegalStateException.class )
  public void testFailsWithEmptyUserAgent() {
    TrackingInfo info = new TrackingInfo();
    info.setUserAgent( "" );
    
    new UniversalUserAgentProvider().getUserAgent( info );
  }
  
  @Test
  public void testProvidesUserAgent() throws Exception {
    TrackingInfo info = new TrackingInfo();
    info.setUserAgent( "foo" );
    
    String userAgent = new UniversalUserAgentProvider().getUserAgent( info );
    
    assertEquals( "foo", userAgent );
  }
}

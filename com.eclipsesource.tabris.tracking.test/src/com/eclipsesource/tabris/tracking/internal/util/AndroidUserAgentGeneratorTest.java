/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.util;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

import com.eclipsesource.tabris.tracking.TrackingInfo;

public class AndroidUserAgentGeneratorTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTrackingInfo() {
    new AndroidUserAgentGenerator().getUserAgent( null );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithNullDeviceOsVersion() {
    TrackingInfo info = new TrackingInfo();
    info.setClientLocale( Locale.GERMAN );
    info.setDeviceModel( "foo" );
    new AndroidUserAgentGenerator().getUserAgent( info );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithEmptyDeviceOsVersion() {
    TrackingInfo info = new TrackingInfo();
    info.setClientLocale( Locale.GERMAN );
    info.setDeviceModel( "foo" );
    info.setDeviceOsVersion( "" );
    new AndroidUserAgentGenerator().getUserAgent( info );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithNullLocale() {
    TrackingInfo info = new TrackingInfo();
    info.setDeviceModel( "foo" );
    info.setDeviceOsVersion( "bar" );
    new AndroidUserAgentGenerator().getUserAgent( info );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithNullDeviceModel() {
    TrackingInfo info = new TrackingInfo();
    info.setClientLocale( Locale.GERMAN );
    info.setDeviceOsVersion( "bar" );
    new AndroidUserAgentGenerator().getUserAgent( info );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithEmptyDeviceModel() {
    TrackingInfo info = new TrackingInfo();
    info.setClientLocale( Locale.GERMAN );
    info.setDeviceOsVersion( "foo" );
    info.setDeviceModel( "" );
    new AndroidUserAgentGenerator().getUserAgent( info );
  }

  @Test
  public void testGeneratesAndroidUserAgent() {
    TrackingInfo info = new TrackingInfo();
    info.setClientLocale( Locale.GERMAN );
    info.setDeviceOsVersion( "foo" );
    info.setDeviceModel( "bar" );
    String userAgent = new AndroidUserAgentGenerator().getUserAgent( info );
    assertEquals( "Mozilla/5.0 (Linux; U; Android foo; de; bar) AppleWebKit/533.1 (KHTML, like Gecko) "
                      + "Version/4.0 Mobile Safari/533.1", userAgent );
  }
}

/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.tracker;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Locale;
import java.util.UUID;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.graphics.Point;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.device.ClientDevice.Platform;
import com.eclipsesource.tabris.tracking.TrackingEvent;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.TrackingInfo;
import com.eclipsesource.tabris.tracking.internal.piwik.Piwik;
import com.eclipsesource.tabris.tracking.internal.piwik.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.VisitorInformation;
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.Action;
import com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys;


public class PiwikTrackerTest {

  private static final String fakeTokenAuth = UUID.randomUUID().toString().replace( "-", "" );

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullPiwikUrl() {
    new PiwikTracker( null, "foo", 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyPiwikUrl() {
    new PiwikTracker( "", "foo", 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTokenAuth() {
    new PiwikTracker( "foo", null, 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyTokenAuth() {
    new PiwikTracker( "foo", "", 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeSiteId() {
    new PiwikTracker( "foo", "bar", -1 );
  }

  @Test
  public void testSendsPageView() {
    Piwik piwik = mock( Piwik.class );
    PiwikTracker tracker = new PiwikTracker( piwik, fakeTokenAuth );
    TrackingEvent event = new TrackingEvent( EventType.PAGE_VIEW, createInfo(), "foo", 1 );

    tracker.handleEvent( event );

    ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass( Action.class );
    ArgumentCaptor<AdvancedConfiguration> configCaptor = ArgumentCaptor.forClass( AdvancedConfiguration.class );
    ArgumentCaptor<VisitorInformation> visitorCaptor = ArgumentCaptor.forClass( VisitorInformation.class );
    verify( piwik ).track( actionCaptor.capture(), visitorCaptor.capture(), configCaptor.capture() );
    assertAdvancedConfiguration( configCaptor.getValue() );
    assertVisitorInformation( visitorCaptor.getValue() );
    assertEquals( "http://appId/page/foo",
                  actionCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.ACTION_URL ) ) );
  }

  @Test
  public void testSendsAction() {
    Piwik piwik = mock( Piwik.class );
    PiwikTracker tracker = new PiwikTracker( piwik, fakeTokenAuth );
    TrackingEvent event = new TrackingEvent( EventType.ACTION, createInfo(), "foo", 1 );

    tracker.handleEvent( event );

    ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass( Action.class );
    ArgumentCaptor<AdvancedConfiguration> configCaptor = ArgumentCaptor.forClass( AdvancedConfiguration.class );
    ArgumentCaptor<VisitorInformation> visitorCaptor = ArgumentCaptor.forClass( VisitorInformation.class );
    verify( piwik ).track( actionCaptor.capture(), visitorCaptor.capture(), configCaptor.capture() );
    assertAdvancedConfiguration( configCaptor.getValue() );
    assertVisitorInformation( visitorCaptor.getValue() );
    assertEquals( "http://appId/action/foo",
                  actionCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.ACTION_URL ) ) );
  }

  @Test
  public void testSendsSearchAction() {
    Piwik piwik = mock( Piwik.class );
    PiwikTracker tracker = new PiwikTracker( piwik, fakeTokenAuth );
    TrackingEvent event = new TrackingEvent( EventType.SEARCH, createInfo(), "foo", 1 );

    tracker.handleEvent( event );

    ArgumentCaptor<Action> actionCaptor = ArgumentCaptor.forClass( Action.class );
    ArgumentCaptor<AdvancedConfiguration> configCaptor = ArgumentCaptor.forClass( AdvancedConfiguration.class );
    ArgumentCaptor<VisitorInformation> visitorCaptor = ArgumentCaptor.forClass( VisitorInformation.class );
    verify( piwik ).track( actionCaptor.capture(), visitorCaptor.capture(), configCaptor.capture() );
    assertAdvancedConfiguration( configCaptor.getValue() );
    assertVisitorInformation( visitorCaptor.getValue() );
    assertEquals( "http://appId/action/search/foo",
                  actionCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.ACTION_URL ) ) );
    assertEquals( "query", actionCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.ACTION_SEARCH ) ) );
  }

  private void assertVisitorInformation( VisitorInformation visitorInformation ) {
    assertEquals( "100x200", visitorInformation.getParameter().get( getRequestKey( RequestKeys.VISITOR_RESOLUTION ) ) );
    assertEquals( "clientId", visitorInformation.getParameter().get( getRequestKey( RequestKeys.VISITOR_ID ) ) );
    assertEquals( "userAgent", visitorInformation.getParameter().get( getRequestKey( RequestKeys.VISITOR_USER_AGENT_OVERRIDE ) ) );
    assertEquals( "en", visitorInformation.getParameter().get( getRequestKey( RequestKeys.VISITOR_LANGUAGE_OVERRIDE ) ) );
    assertCustomVariables( visitorInformation );
  }

  private void assertCustomVariables( VisitorInformation visitorInformation ) {
    JsonObject customVariables = JsonObject.readFrom( ( String )visitorInformation.getParameter()
      .get( getRequestKey( RequestKeys.VISITOR_CUSTOM_VARIABLES ) ) );
    assertEquals( "appVersion", customVariables.get( "1" ).asArray().get( 1 ).asString() );
    assertEquals( "en-CA", customVariables.get( "2" ).asArray().get( 1 ).asString() );
    assertEquals( "model", customVariables.get( "3" ).asArray().get( 1 ).asString() );
    assertEquals( "osVersion", customVariables.get( "4" ).asArray().get( 1 ).asString() );
    assertEquals( "vendor", customVariables.get( "5" ).asArray().get( 1 ).asString() );
    assertEquals( "tabrisVersion", customVariables.get( "6" ).asArray().get( 1 ).asString() );
  }

  private void assertAdvancedConfiguration( AdvancedConfiguration configuration ) {
    assertEquals( "ip", configuration.getParameter().get( getRequestKey( RequestKeys.VISITOR_IP_OVERRIDE ) ) );
    assertEquals( fakeTokenAuth, configuration.getParameter().get( getRequestKey( RequestKeys.TOKEN_AUTH ) ) );
  }

  private TrackingInfo createInfo() {
    TrackingInfo info = new TrackingInfo();
    info.setAppVersion( "appVersion" );
    info.setAppId( "appId" );
    info.setClientId( "clientId" );
    info.setClientLocale( Locale.CANADA );
    info.setDeviceModel( "model" );
    info.setDeviceOsVersion( "osVersion" );
    info.setDeviceVendor( "vendor" );
    info.setTabrisVersion( "tabrisVersion" );
    info.setScreenResolution( new Point( 100, 200 ) );
    info.setSearchQuery( "query" );
    info.setUserAgent( "userAgent" );
    info.setClientIp( "ip" );
    info.setPlatform( Platform.WEB );
    return info;
  }
}

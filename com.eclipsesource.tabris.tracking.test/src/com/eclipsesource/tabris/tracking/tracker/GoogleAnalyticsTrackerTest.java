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
package com.eclipsesource.tabris.tracking.tracker;

import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.tracker.GoogleAnalyticsTracker.CATEGORY_ACTION;
import static com.eclipsesource.tabris.tracking.tracker.GoogleAnalyticsTracker.CATEGORY_SEARCH;
import static com.eclipsesource.tabris.tracking.tracker.GoogleAnalyticsTracker.LABEL_EXECUTE;
import static com.eclipsesource.tabris.tracking.tracker.GoogleAnalyticsTracker.LABEL_SEARCH;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.eclipse.swt.graphics.Point;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.device.ClientDevice.Platform;
import com.eclipsesource.tabris.tracking.TrackingEvent;
import com.eclipsesource.tabris.tracking.TrackingEvent.EventType;
import com.eclipsesource.tabris.tracking.TrackingInfo;
import com.eclipsesource.tabris.tracking.internal.analytics.GoogleAnalytics;
import com.eclipsesource.tabris.tracking.internal.analytics.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.Hit;
import com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageConfiguration;


public class GoogleAnalyticsTrackerTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTrackingId() {
    new GoogleAnalyticsTracker( null, "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyTrackingId() {
    new GoogleAnalyticsTracker( "", "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullAppName() {
    new GoogleAnalyticsTracker( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyAppName() {
    new GoogleAnalyticsTracker( "foo", "" );
  }

  @Test
  public void testSendsPageView() {
    GoogleAnalytics analytics = mock( GoogleAnalytics.class );
    GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker( analytics );
    PageConfiguration config = mock( PageConfiguration.class );
    when( config.getId() ).thenReturn( "foo" );
    TrackingEvent event = new TrackingEvent( EventType.PAGE_VIEW, createInfo(), config, 1 );

    tracker.handleEvent( event );

    ArgumentCaptor<Hit> hitCaptor = ArgumentCaptor.forClass( Hit.class );
    ArgumentCaptor<AdvancedConfiguration> configCaptor = ArgumentCaptor.forClass( AdvancedConfiguration.class );
    verify( analytics ).track( hitCaptor.capture(), eq( "clientId" ), configCaptor.capture() );
    assertAdvancedConfiguration( configCaptor.getValue() );
    assertEquals( "foo", hitCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.CONTENT_DESCRIPTION ) ) );
  }

  @Test
  public void testSendsAction() {
    GoogleAnalytics analytics = mock( GoogleAnalytics.class );
    GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker( analytics );
    ActionConfiguration config = mock( ActionConfiguration.class );
    when( config.getId() ).thenReturn( "foo" );
    TrackingEvent event = new TrackingEvent( EventType.ACTION, createInfo(), config, 1 );

    tracker.handleEvent( event );

    ArgumentCaptor<Hit> hitCaptor = ArgumentCaptor.forClass( Hit.class );
    ArgumentCaptor<AdvancedConfiguration> configCaptor = ArgumentCaptor.forClass( AdvancedConfiguration.class );
    verify( analytics ).track( hitCaptor.capture(), eq( "clientId" ), configCaptor.capture() );
    assertAdvancedConfiguration( configCaptor.getValue() );
    assertEquals( "foo", hitCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.EVENT_LABEL ) ) );
    assertEquals( CATEGORY_ACTION, hitCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.EVENT_CATEGORY ) ) );
    assertEquals( LABEL_EXECUTE, hitCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.EVENT_ACTION ) ) );
  }

  @Test
  public void testSendsSearchAction() {
    GoogleAnalytics analytics = mock( GoogleAnalytics.class );
    GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker( analytics );
    ActionConfiguration config = mock( ActionConfiguration.class );
    when( config.getId() ).thenReturn( "foo" );
    TrackingEvent event = new TrackingEvent( EventType.SEARCH, createInfo(), config, 1 );

    tracker.handleEvent( event );

    ArgumentCaptor<Hit> hitCaptor = ArgumentCaptor.forClass( Hit.class );
    ArgumentCaptor<AdvancedConfiguration> configCaptor = ArgumentCaptor.forClass( AdvancedConfiguration.class );
    verify( analytics ).track( hitCaptor.capture(), eq( "clientId" ), configCaptor.capture() );
    assertAdvancedConfiguration( configCaptor.getValue() );
    assertEquals( "foo", hitCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.EVENT_LABEL ) ) );
    assertEquals( CATEGORY_SEARCH, hitCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.EVENT_CATEGORY ) ) );
    assertEquals( LABEL_SEARCH, hitCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.EVENT_ACTION ) ) );
    assertEquals( "query", configCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.CUSTOM_DIMENSION, 1 ) ) );
  }

  @Test
  public void testSendsSearchActionWithConfiguredCustomDimension() {
    GoogleAnalytics analytics = mock( GoogleAnalytics.class );
    GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker( analytics );
    ActionConfiguration config = mock( ActionConfiguration.class );
    when( config.getId() ).thenReturn( "foo" );
    TrackingEvent event = new TrackingEvent( EventType.SEARCH, createInfo(), config, 1 );

    tracker.setSearchCustomDimension( 2 );
    tracker.handleEvent( event );

    ArgumentCaptor<AdvancedConfiguration> configCaptor = ArgumentCaptor.forClass( AdvancedConfiguration.class );
    verify( analytics ).track( any( Hit.class ), eq( "clientId" ), configCaptor.capture() );
    assertAdvancedConfiguration( configCaptor.getValue() );
    assertEquals( "query", configCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.CUSTOM_DIMENSION, 2 ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetZeroSearchIndex() {
    GoogleAnalytics analytics = mock( GoogleAnalytics.class );
    GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker( analytics );

    tracker.setSearchCustomDimension( 0 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeSearchIndex() {
    GoogleAnalytics analytics = mock( GoogleAnalytics.class );
    GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker( analytics );

    tracker.setSearchCustomDimension( -1 );
  }

  private void assertAdvancedConfiguration( AdvancedConfiguration configuration ) {
    assertEquals( "appId", configuration.getParameter().get( getRequestKey( RequestKeys.APP_ID ) ) );
    assertEquals( "appVersion", configuration.getParameter().get( getRequestKey( RequestKeys.APP_VERSION ) ) );
    assertEquals( "100x200", configuration.getParameter().get( getRequestKey( RequestKeys.SCREEN_RESOLUTION ) ) );
    assertEquals( "100x200", configuration.getParameter().get( getRequestKey( RequestKeys.VIEWPORT_SIZE ) ) );
    assertEquals( "ip", configuration.getParameter().get( getRequestKey( RequestKeys.IP_OVERRIDE ) ) );
    assertEquals( "userAgent", configuration.getParameter().get( getRequestKey( RequestKeys.USER_AGENT_OVERRIDE ) ) );
    assertEquals( "en-CA", configuration.getParameter().get( getRequestKey( RequestKeys.USER_LANGUAGE ) ) );
  }

  @Test
  public void testAndroidUserAgent() {
    GoogleAnalytics analytics = mock( GoogleAnalytics.class );
    GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker( analytics );
    ActionConfiguration config = mock( ActionConfiguration.class );
    when( config.getId() ).thenReturn( "foo" );
    TrackingInfo info = createInfo();
    info.setPlatform( Platform.ANDROID );
    info.setDeviceOsVersion( "4.4" );
    TrackingEvent event = new TrackingEvent( EventType.SEARCH, info, config, 1 );

    tracker.handleEvent( event );

    ArgumentCaptor<AdvancedConfiguration> configCaptor = ArgumentCaptor.forClass( AdvancedConfiguration.class );
    verify( analytics ).track( any( Hit.class ), eq( "clientId" ), configCaptor.capture() );
    String userAgent = "Mozilla/5.0 (Linux; U; Android 4.4; " + info.getClientLocale() + "; model) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
    assertEquals( userAgent, configCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.USER_AGENT_OVERRIDE ) ) );
  }

  @Test
  public void testIOSUserAgent() {
    GoogleAnalytics analytics = mock( GoogleAnalytics.class );
    GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker( analytics );
    ActionConfiguration config = mock( ActionConfiguration.class );
    when( config.getId() ).thenReturn( "foo" );
    TrackingInfo info = createInfo();
    info.setPlatform( Platform.IOS );
    info.setDeviceOsVersion( "7.1" );
    TrackingEvent event = new TrackingEvent( EventType.SEARCH, info, config, 1 );

    tracker.handleEvent( event );

    ArgumentCaptor<AdvancedConfiguration> configCaptor = ArgumentCaptor.forClass( AdvancedConfiguration.class );
    verify( analytics ).track( any( Hit.class ), eq( "clientId" ), configCaptor.capture() );
    String userAgent = "Mozilla/5.0 (model; U; CPU iPhone OS 7_1 like Mac OS X; " + info.getClientLocale() + " ) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7";
    assertEquals( userAgent, configCaptor.getValue().getParameter().get( getRequestKey( RequestKeys.USER_AGENT_OVERRIDE ) ) );
  }

  private TrackingInfo createInfo() {
    TrackingInfo info = new TrackingInfo();
    info.setAppId( "appId" );
    info.setAppVersion( "appVersion" );
    info.setClientId( "clientId" );
    info.setClientLocale( Locale.CANADA );
    info.setDeviceModel( "model" );
    info.setScreenResolution( new Point( 100, 200 ) );
    info.setSearchQuery( "query" );
    info.setUserAgent( "userAgent" );
    info.setClientIp( "ip" );
    info.setPlatform( Platform.WEB );
    return info;
  }

}

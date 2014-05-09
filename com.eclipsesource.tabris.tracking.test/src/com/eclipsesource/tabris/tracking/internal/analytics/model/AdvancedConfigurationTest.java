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
package com.eclipsesource.tabris.tracking.internal.analytics.model;

import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.APP_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.APP_VERSION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SCREEN_NAME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.CURRENCY_CODE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.CUSTOM_DIMENSION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.CUSTOM_METRIC;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.DOCUMENT_HOST_NAME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.DOCUMENT_PATH;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.DOCUMENT_TITLE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.IP_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SCREEN_RESOLUTION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.SESSION_CONTROL;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_AGENT_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.USER_LANGUAGE;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.VIEWPORT_SIZE;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class AdvancedConfigurationTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUserId() {
    new AdvancedConfiguration().setUserId( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUserId() {
    new AdvancedConfiguration().setUserId( "" );
  }

  @Test
  public void testSetsUserId() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setUserId( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( USER_ID ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSessionControl() {
    new AdvancedConfiguration().setSessionControl( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptySessionControl() {
    new AdvancedConfiguration().setSessionControl( "" );
  }

  @Test
  public void testSetsSessionControl() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setSessionControl( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( SESSION_CONTROL ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullIpOverride() {
    new AdvancedConfiguration().setIpOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyIpOverride() {
    new AdvancedConfiguration().setIpOverride( "" );
  }

  @Test
  public void testSetsIpOverride() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setIpOverride( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( IP_OVERRIDE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUserAgentOverride() {
    new AdvancedConfiguration().setUserAgentOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUserAgentOverride() {
    new AdvancedConfiguration().setUserAgentOverride( "" );
  }

  @Test
  public void testSetsUserAgentOverride() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setUserAgentOverride( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( USER_AGENT_OVERRIDE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullScreenResolution() {
    new AdvancedConfiguration().setScreenResolution( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyScreenResolution() {
    new AdvancedConfiguration().setScreenResolution( "" );
  }

  @Test
  public void testSetsScreenResolution() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setScreenResolution( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( SCREEN_RESOLUTION ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullViewportSize() {
    new AdvancedConfiguration().setViewportSize( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyViewportSize() {
    new AdvancedConfiguration().setViewportSize( "" );
  }

  @Test
  public void testSetsViewportSize() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setViewportSize( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( VIEWPORT_SIZE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUserLanguage() {
    new AdvancedConfiguration().setUserLanguage( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUserLanguage() {
    new AdvancedConfiguration().setUserLanguage( "" );
  }

  @Test
  public void testSetsUserLanguage() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setUserLanguage( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( USER_LANGUAGE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullAppId() {
    new AdvancedConfiguration().setAppId( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyAppId() {
    new AdvancedConfiguration().setAppId( "" );
  }

  @Test
  public void testSetsAppId() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setAppId( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( APP_ID ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullAppVersion() {
    new AdvancedConfiguration().setAppVersion( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyAppVersion() {
    new AdvancedConfiguration().setAppVersion( "" );
  }

  @Test
  public void testSetsAppVersion() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setAppVersion( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( APP_VERSION ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDocumentHostName() {
    new AdvancedConfiguration().setDocumentHostName( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyDocumentHostName() {
    new AdvancedConfiguration().setDocumentHostName( "" );
  }

  @Test
  public void testSetsDocumentHostName() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setDocumentHostName( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( DOCUMENT_HOST_NAME ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDocumentPath() {
    new AdvancedConfiguration().setDocumentPath( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyDocumentPath() {
    new AdvancedConfiguration().setDocumentPath( "" );
  }

  @Test
  public void testSetsDocumentPath() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setDocumentPath( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( DOCUMENT_PATH ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDocumentSettings() {
    new AdvancedConfiguration().setDocumentTitle( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyDocumentSettings() {
    new AdvancedConfiguration().setDocumentTitle( "" );
  }

  @Test
  public void testSetsDocumentSettings() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setDocumentTitle( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( DOCUMENT_TITLE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullScreenName() {
    new AdvancedConfiguration().setScreenName( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyScreenName() {
    new AdvancedConfiguration().setScreenName( "" );
  }

  @Test
  public void testSetsScreenName() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setScreenName( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( SCREEN_NAME ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCurrencyCode() {
    new AdvancedConfiguration().setCurrencyCode( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCurrencyCode() {
    new AdvancedConfiguration().setCurrencyCode( "" );
  }

  @Test
  public void testSetsCurrencyCode() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setCurrencyCode( "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( CURRENCY_CODE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCustomDimension() {
    new AdvancedConfiguration().setCustomDimension( 2, null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCustomDimension() {
    new AdvancedConfiguration().setCustomDimension( 2, "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCustomDimensionIndexSmallerZero() {
    new AdvancedConfiguration().setCustomDimension( -1, "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCustomDimensionIndexEqualZero() {
    new AdvancedConfiguration().setCustomDimension( 0, "foo" );
  }

  @Test
  public void testSetsCustomDimension() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setCustomDimension( 2, "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( CUSTOM_DIMENSION, 2 ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCustomMetric() {
    new AdvancedConfiguration().setCustomMetric( 2, null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCustomMetric() {
    new AdvancedConfiguration().setCustomMetric( 2, "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCustomMetricIndexSmallerZero() {
    new AdvancedConfiguration().setCustomMetric( -1, "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCustomMetricIndexEqualZero() {
    new AdvancedConfiguration().setCustomMetric( 0, "foo" );
  }

  @Test
  public void testSetsCustomMetric() {
    AdvancedConfiguration configuration = new AdvancedConfiguration().setCustomMetric( 2, "foo" );

    Map<String, Object> parameter = configuration.getParameter();

    assertEquals( "foo", parameter.get( getRequestKey( CUSTOM_METRIC, 2 ) ) );
  }
}

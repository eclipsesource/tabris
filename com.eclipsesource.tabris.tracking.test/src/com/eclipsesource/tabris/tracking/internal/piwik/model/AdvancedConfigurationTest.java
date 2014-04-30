/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.IS_BOT;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.NEW_VISIT;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.TOKEN_AUTH;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_CITY_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_COUNTRY_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_DATETIME_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_ID_ENFORCED;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_IP_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_LATITUDE_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_LONGITUDE_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_REGION_OVERRIDE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;


public class AdvancedConfigurationTest {

  public static final String FAKE_TOKEN = "5nUcfF0yL7JHnpgwCQjFjB1hwwuX2Pvk";

  @Test
  public void testParametersAreNotNull() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    Map<String, Object> parameters = advancedConfiguration.getParameter();

    assertNotNull( parameters );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTokenAuth() {
    new AdvancedConfiguration( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithTokenAuthWithWrongLength() {
    new AdvancedConfiguration( "foo" );
  }

  @Test
  public void testAddsTokenAuthToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    assertEquals( FAKE_TOKEN, advancedConfiguration.getParameter().get( getRequestKey( TOKEN_AUTH ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullVisitorIpOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setVisitorIpOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyVisitorIpOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setVisitorIpOverride( "" );
  }

  @Test
  public void testAddsVisitorIpToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setVisitorIpOverride( "foo" );

    assertEquals( "foo", advancedConfiguration.getParameter().get( getRequestKey( VISITOR_IP_OVERRIDE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDatetimeOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setDatetimeOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyDatetimeOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setDatetimeOverride( "" );
  }

  @Test
  public void testAddsDatetimeToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setDatetimeOverride( "foo" );

    assertEquals( "foo", advancedConfiguration.getParameter().get( getRequestKey( VISITOR_DATETIME_OVERRIDE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullVisitorIdEnforced() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setVisitorIdEnforced( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyVisitorIdEnforced() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setVisitorIdEnforced( "" );
  }

  @Test
  public void testAddsVisitorIdEnforcedToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setVisitorIdEnforced( "foo" );

    assertEquals( "foo", advancedConfiguration.getParameter().get( getRequestKey( VISITOR_ID_ENFORCED ) ) );
  }

  @Test
  public void testAddsNewVisitToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setNewVisit( true );

    assertEquals( Boolean.valueOf( true ), advancedConfiguration.getParameter().get( getRequestKey( NEW_VISIT ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCountryOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setCountryOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCountryOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setCountryOverride( "" );
  }

  @Test
  public void testAddsVisitorCountryOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setCountryOverride( "foo" );

    assertEquals( "foo", advancedConfiguration.getParameter().get( getRequestKey( VISITOR_COUNTRY_OVERRIDE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullRegionOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setRegionOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyRegionOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setRegionOverride( "" );
  }

  @Test
  public void testAddsRegionOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setRegionOverride( "foo" );

    assertEquals( "foo", advancedConfiguration.getParameter().get( getRequestKey( VISITOR_REGION_OVERRIDE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCityOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setCityOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCityOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setCityOverride( "" );
  }

  @Test
  public void testAddsCityOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setCityOverride( "foo" );

    assertEquals( "foo", advancedConfiguration.getParameter().get( getRequestKey( VISITOR_CITY_OVERRIDE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullLatitudeOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setLatitudeOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyLatitudeOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setLatitudeOverride( "" );
  }

  @Test
  public void testAddsLatitudeOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setLatitudeOverride( "foo" );

    assertEquals( "foo", advancedConfiguration.getParameter().get( getRequestKey( VISITOR_LATITUDE_OVERRIDE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullLongitudeOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setLongitudeOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyLongitudeOverride() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN );

    advancedConfiguration.setLongitudeOverride( "" );
  }

  @Test
  public void testAddsLongitudeOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setLongitudeOverride( "foo" );

    assertEquals( "foo", advancedConfiguration.getParameter().get( getRequestKey( VISITOR_LONGITUDE_OVERRIDE ) ) );
  }

  @Test
  public void testAddsIsBotToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setIsBot( true );

    assertEquals( Boolean.valueOf( true ), advancedConfiguration.getParameter().get( getRequestKey( IS_BOT ) ) );
  }

}

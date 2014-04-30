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

    Map<String, Object> parameters = advancedConfiguration.getParameters();

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

    assertEquals( FAKE_TOKEN, advancedConfiguration.getParameters().get( getRequestKey( TOKEN_AUTH ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullVisitorIpOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setVisitorIpOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyVisitorIpOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setVisitorIpOverride( "" );
  }

  @Test
  public void testAddsVisitorIpToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setVisitorIpOverride( "foo" );

    assertEquals( "foo", advancedConfiguration.getParameters().get( getRequestKey( VISITOR_IP_OVERRIDE ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDatetimeOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setDatetimeOverride( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyDatetimeOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setDatetimeOverride( "" );
  }
  
  @Test
  public void testAddsDatetimeToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setDatetimeOverride( "foo" );
    
    assertEquals( "foo", advancedConfiguration.getParameters().get( getRequestKey( VISITOR_DATETIME_OVERRIDE ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullVisitorIdEnforced() {
    new AdvancedConfiguration( FAKE_TOKEN ).setVisitorIdEnforced( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyVisitorIdEnforced() {
    new AdvancedConfiguration( FAKE_TOKEN ).setVisitorIdEnforced( "" );
  }
  
  @Test
  public void testAddsVisitorIdEnforcedToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setVisitorIdEnforced( "foo" );
    
    assertEquals( "foo", advancedConfiguration.getParameters().get( getRequestKey( VISITOR_ID_ENFORCED ) ) );
  }
  
  @Test
  public void testAddsNewVisitToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setNewVisit( true );
    
    assertEquals( Boolean.valueOf( true ), advancedConfiguration.getParameters().get( getRequestKey( NEW_VISIT ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCountryOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setCountryOverride( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCountryOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setCountryOverride( "" );
  }
  
  @Test
  public void testAddsVisitorCountryOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setCountryOverride( "foo" );
    
    assertEquals( "foo", advancedConfiguration.getParameters().get( getRequestKey( VISITOR_COUNTRY_OVERRIDE ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullRegionOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setRegionOverride( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyRegionOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setRegionOverride( "" );
  }
  
  @Test
  public void testAddsRegionOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setRegionOverride( "foo" );
    
    assertEquals( "foo", advancedConfiguration.getParameters().get( getRequestKey( VISITOR_REGION_OVERRIDE ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCityOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setCityOverride( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCityOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setCityOverride( "" );
  }
  
  @Test
  public void testAddsCityOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setCityOverride( "foo" );
    
    assertEquals( "foo", advancedConfiguration.getParameters().get( getRequestKey( VISITOR_CITY_OVERRIDE ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullLatitudeOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setLatitudeOverride( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyLatitudeOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setLatitudeOverride( "" );
  }
  
  @Test
  public void testAddsLatitudeOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setLatitudeOverride( "foo" );
    
    assertEquals( "foo", advancedConfiguration.getParameters().get( getRequestKey( VISITOR_LATITUDE_OVERRIDE ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullLongitudeOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setLongitudeOverride( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyLongitudeOverride() {
    new AdvancedConfiguration( FAKE_TOKEN ).setLongitudeOverride( "" );
  }
  
  @Test
  public void testAddsLongitudeOverrideToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setLongitudeOverride( "foo" );
    
    assertEquals( "foo", advancedConfiguration.getParameters().get( getRequestKey( VISITOR_LONGITUDE_OVERRIDE ) ) );
  }
  
  @Test
  public void testAddsIsBotToParameters() {
    AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration( FAKE_TOKEN ).setIsBot( true );
    
    assertEquals( Boolean.valueOf( true ), advancedConfiguration.getParameters().get( getRequestKey( IS_BOT ) ) );
  }
  
}

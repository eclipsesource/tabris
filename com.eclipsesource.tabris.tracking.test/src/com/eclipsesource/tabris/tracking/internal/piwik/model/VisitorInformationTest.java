/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_CAMPAIGN_KEYWORD;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_CAMPAIGN_NAME;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_CUSTOM_VARIABLES;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_FIRST_VISIT;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_HOUR;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_ID;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_LANGUAGE_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_MINUTE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_PREVIOUS_VISIT;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_REFERRER_URL;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_RESOLUTION;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_SECOND;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_USER_AGENT_OVERRIDE;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.VISITOR_VISITS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class VisitorInformationTest {

  private VisitorInformation visitorInformation;

  @Before
  public void setUp() {
    visitorInformation = new VisitorInformation();
  }

  @Test
  public void testParametersAreNotNull() {
    Map<String, Object> parameters = visitorInformation.getParameter();

    assertNotNull( parameters );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullId() throws Exception {
    visitorInformation.setId( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() throws Exception {
    visitorInformation.setId( "" );
  }

  @Test
  public void testAddsIdToParameters() throws Exception {
    visitorInformation.setId( "foo" );

    assertEquals( "foo", visitorInformation.getParameter().get( getRequestKey( VISITOR_ID ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUrlRef() {
    visitorInformation.setReferrerUrl( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUrlRef() {
    visitorInformation.setReferrerUrl( "" );
  }

  @Test
  public void testAddsReferrerUrlToParameters() {
    visitorInformation.setReferrerUrl( "foo" );

    assertEquals( "foo", visitorInformation.getParameter().get( getRequestKey( VISITOR_REFERRER_URL ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCustomVariables() {
    visitorInformation.setCustomVariables( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCustomVariables() {
    visitorInformation.setCustomVariables( "" );
  }

  @Test
  public void testAddsCustomVariablesToParameters() {
    visitorInformation.setCustomVariables( "foo" );

    assertEquals( "foo", visitorInformation.getParameter().get( getRequestKey( VISITOR_CUSTOM_VARIABLES ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeVisits() {
    visitorInformation.setVisits( -1 );
  }

  @Test
  public void testAddsVisitsToParameters() {
    visitorInformation.setVisits( 2 );

    assertEquals( Integer.valueOf( 2 ), visitorInformation.getParameter().get( getRequestKey( VISITOR_VISITS ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativePreviousVisitTimestamp() {
    visitorInformation.setPreviousVisitUnixTimestamp( -1 );
  }

  @Test
  public void testAddsPreviousVisitUnixTimestampToParameters() {
    visitorInformation.setPreviousVisitUnixTimestamp( 2 );

    assertEquals( Long.valueOf( 2 ), visitorInformation.getParameter().get( getRequestKey( VISITOR_PREVIOUS_VISIT ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeFirstVisitTimestamp() {
    visitorInformation.setFirstVisitUnixTimestamp( -1 );
  }

  @Test
  public void testAddsFirstVisitUnixTimestampToParameters() {
    visitorInformation.setFirstVisitUnixTimestamp( 2 );

    assertEquals( Long.valueOf( 2 ), visitorInformation.getParameter().get( getRequestKey( VISITOR_FIRST_VISIT ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCampaignName() {
    visitorInformation.setCampaignName( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCampaignName() {
    visitorInformation.setCampaignName( "" );
  }

  @Test
  public void testAddsCampaignNameToParameters() {
    visitorInformation.setCampaignName( "foo" );

    assertEquals( "foo", visitorInformation.getParameter().get( getRequestKey( VISITOR_CAMPAIGN_NAME ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCampaignKeyword() {
    visitorInformation.setCampaignKeyword( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCampaignKeyword() {
    visitorInformation.setCampaignKeyword( "" );
  }

  @Test
  public void testAddsCampaignKeywordToParameters() {
    visitorInformation.setCampaignKeyword( "foo" );

    assertEquals( "foo", visitorInformation.getParameter().get( getRequestKey( VISITOR_CAMPAIGN_KEYWORD ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullResolution() {
    visitorInformation.setScreenResolution( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyResolution() {
    visitorInformation.setScreenResolution( "" );
  }

  @Test
  public void testAddsResolutionToParameters() {
    visitorInformation.setScreenResolution( "foo" );

    assertEquals( "foo", visitorInformation.getParameter().get( getRequestKey( VISITOR_RESOLUTION ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithHourTooLarge() {
    visitorInformation.setHour( 24 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithHourTooSmall() {
    visitorInformation.setHour( -1 );
  }

  @Test
  public void testAddsHourToParameters() {
    visitorInformation.setHour( 23 );

    assertEquals( Integer.valueOf( 23 ), visitorInformation.getParameter().get( getRequestKey( VISITOR_HOUR ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithMinuteTooLarge() {
    visitorInformation.setMinute( 60 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithMinuteTooSmall() {
    visitorInformation.setMinute( -1 );
  }

  @Test
  public void testAddsMinuteToParameters() {
    visitorInformation.setMinute( 59 );

    assertEquals( Integer.valueOf( 59 ), visitorInformation.getParameter().get( getRequestKey( VISITOR_MINUTE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithSecondTooLarge() {
    visitorInformation.setSecond( 61 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithSecondTooSmall() {
    visitorInformation.setSecond( -1 );
  }

  @Test
  public void testAddsSecondToParameters() {
    visitorInformation.setSecond( 59 );

    assertEquals( Integer.valueOf( 59 ), visitorInformation.getParameter().get( getRequestKey( VISITOR_SECOND ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUserAgent() {
    visitorInformation.setUserAgentOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUserAgent() {
    visitorInformation.setUserAgentOverride( "" );
  }

  @Test
  public void testAddsUserAgentToParameters() {
    visitorInformation.setUserAgentOverride( "foo" );

    assertEquals( "foo", visitorInformation.getParameter().get( getRequestKey( VISITOR_USER_AGENT_OVERRIDE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullLanguage() {
    visitorInformation.setLanguageOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyLanguage() {
    visitorInformation.setLanguageOverride( "" );
  }

  @Test
  public void testAddsLanguageToParameters() {
    visitorInformation.setLanguageOverride( "foo" );

    assertEquals( "foo", visitorInformation.getParameter().get( getRequestKey( VISITOR_LANGUAGE_OVERRIDE ) ) );
  }
}

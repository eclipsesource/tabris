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
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class VisitorInformationTest {

  @Test
  public void testParametersAreNotNull() {
    VisitorInformation action = new VisitorInformation();

    Map<String, Object> parameters = action.getParameters();

    assertNotNull( parameters );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullId() throws Exception {
    new VisitorInformation().setId( null );
  }
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() throws Exception {
    new VisitorInformation().setId( "" );
  }

  @Test
  public void testAddsIdToParameters() throws Exception {
    VisitorInformation visitorInformation = new VisitorInformation().setId( "foo" );

    assertEquals( "foo", visitorInformation.getParameters().get( getRequestKey( VISITOR_ID ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUrlRef() {
    new VisitorInformation().setReferrerUrl( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUrlRef() {
    new VisitorInformation().setReferrerUrl( "" );
  }

  @Test
  public void testAddsReferrerUrlToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setReferrerUrl( "foo" );

    assertEquals( "foo", visitorInformation.getParameters().get( getRequestKey( VISITOR_REFERRER_URL ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCustomVariables() {
    new VisitorInformation().setCustomVariables( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCustomVariables() {
    new VisitorInformation().setCustomVariables( "" );
  }

  @Test
  public void testAddsCustomVariablesToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setCustomVariables( "foo" );

    assertEquals( "foo",
                  visitorInformation.getParameters()
                    .get( getRequestKey( VISITOR_CUSTOM_VARIABLES ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeVisits() {
    new VisitorInformation().setVisits( -1 );
  }

  @Test
  public void testAddsVisitsToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setVisits( 2 );

    assertEquals( Integer.valueOf( 2 ),
                  visitorInformation.getParameters().get( getRequestKey( VISITOR_VISITS ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativePreviousVisitTimestamp() {
    new VisitorInformation().setPreviousVisitUnixTimestamp( -1 );
  }

  @Test
  public void testAddsPreviousVisitUnixTimestampToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setPreviousVisitUnixTimestamp( 2 );

    assertEquals( Long.valueOf( 2 ),
                  visitorInformation.getParameters().get( getRequestKey( VISITOR_PREVIOUS_VISIT ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeFirstVisitTimestamp() {
    new VisitorInformation().setFirstVisitUnixTimestamp( -1 );
  }

  @Test
  public void testAddsFirstVisitUnixTimestampToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setFirstVisitUnixTimestamp( 2 );

    assertEquals( Long.valueOf( 2 ),
                  visitorInformation.getParameters().get( getRequestKey( VISITOR_FIRST_VISIT ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCampaignName() {
    new VisitorInformation().setCampaignName( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCampaignName() {
    new VisitorInformation().setCampaignName( "" );
  }

  @Test
  public void testAddsCampaignNameToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setCampaignName( "foo" );

    assertEquals( "foo",
                  visitorInformation.getParameters().get( getRequestKey( VISITOR_CAMPAIGN_NAME ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCampaignKeyword() {
    new VisitorInformation().setCampaignKeyword( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCampaignKeyword() {
    new VisitorInformation().setCampaignKeyword( "" );
  }

  @Test
  public void testAddsCampaignKeywordToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setCampaignKeyword( "foo" );

    assertEquals( "foo",
                  visitorInformation.getParameters()
                    .get( getRequestKey( VISITOR_CAMPAIGN_KEYWORD ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullResolution() {
    new VisitorInformation().setScreenResolution( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyResolution() {
    new VisitorInformation().setScreenResolution( "" );
  }

  @Test
  public void testAddsResolutionToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setScreenResolution( "foo" );

    assertEquals( "foo", visitorInformation.getParameters()
      .get( getRequestKey( VISITOR_RESOLUTION ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithHourTooLarge() {
    new VisitorInformation().setHour( 24 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithHourTooSmall() {
    new VisitorInformation().setHour( -1 );
  }

  @Test
  public void testAddsHourToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setHour( 23 );

    assertEquals( Integer.valueOf( 23 ),
                  visitorInformation.getParameters().get( getRequestKey( VISITOR_HOUR ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithMinuteTooLarge() {
    new VisitorInformation().setMinute( 60 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithMinuteTooSmall() {
    new VisitorInformation().setMinute( -1 );
  }

  @Test
  public void testAddsMinuteToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setMinute( 59 );

    assertEquals( Integer.valueOf( 59 ),
                  visitorInformation.getParameters().get( getRequestKey( VISITOR_MINUTE ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithSecondTooLarge() {
    new VisitorInformation().setSecond( 61 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithSecondTooSmall() {
    new VisitorInformation().setSecond( -1 );
  }

  @Test
  public void testAddsSecondToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setSecond( 59 );

    assertEquals( Integer.valueOf( 59 ),
                  visitorInformation.getParameters().get( getRequestKey( VISITOR_SECOND ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUserAgent() {
    new VisitorInformation().setUserAgentOverride( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyUserAgent() {
    new VisitorInformation().setUserAgentOverride( "" );
  }

  @Test
  public void testAddsUserAgentToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setUserAgentOverride( "foo" );

    assertEquals( "foo",
                  visitorInformation.getParameters()
                    .get( getRequestKey( VISITOR_USER_AGENT_OVERRIDE ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullLanguage() {
    new VisitorInformation().setLanguageOverride( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyLanguage() {
    new VisitorInformation().setLanguageOverride( "" );
  }
  
  @Test
  public void testAddsLanguageToParameters() {
    VisitorInformation visitorInformation = new VisitorInformation().setLanguageOverride( "foo" );
    
    assertEquals( "foo",
                  visitorInformation.getParameters()
                  .get( getRequestKey( VISITOR_LANGUAGE_OVERRIDE ) ) );
  }
}

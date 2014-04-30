/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
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

import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.tabris.tracking.internal.Requestable;


@SuppressWarnings("restriction")
public class VisitorInformation implements Requestable {

  private final Map<String, Object> parameter;

  public VisitorInformation() {
    parameter = new HashMap<String, Object>();
  }

  protected void addParameter( String key, Object value ) {
    parameter.put( key, value );
  }

  @Override
  public Map<String, Object> getParameter() {
    return parameter;
  }

  public VisitorInformation setId( String id ) {
    whenNull( id ).throwIllegalArgument( "Id must not be null." );
    when( id.isEmpty() ).throwIllegalArgument( "Id must not be empty." );
    addParameter( getRequestKey( VISITOR_ID ), id );
    return this;
  }

  public VisitorInformation setReferrerUrl( String referrerUrl ) {
    whenNull( referrerUrl ).throwIllegalArgument( "ReferrerUrl must not be null." );
    when( referrerUrl.isEmpty() ).throwIllegalArgument( "ReferrerUrl must not be empty." );
    addParameter( getRequestKey( VISITOR_REFERRER_URL ), referrerUrl );
    return this;
  }

  public VisitorInformation setCustomVariables( String customVariables ) {
    whenNull( customVariables ).throwIllegalArgument( "CustomVariables must not be null." );
    when( customVariables.isEmpty() ).throwIllegalArgument( "CustomVariables must not be empty." );
    addParameter( getRequestKey( VISITOR_CUSTOM_VARIABLES ), customVariables );
    return this;
  }

  public VisitorInformation setVisits( int visits ) {
    when( visits <= 0 ).throwIllegalArgument( "Visits must be > 0, but was " + visits );
    addParameter( getRequestKey( VISITOR_VISITS ), Integer.valueOf( visits ) );
    return this;
  }

  public VisitorInformation setPreviousVisitUnixTimestamp( long timestamp ) {
    when( timestamp <= 0 ).throwIllegalArgument( "Timestamp must be > 0, but was " + timestamp );
    addParameter( getRequestKey( VISITOR_PREVIOUS_VISIT ), Long.valueOf( timestamp ) );
    return this;
  }

  public VisitorInformation setFirstVisitUnixTimestamp( long timestamp ) {
    when( timestamp <= 0 ).throwIllegalArgument( "Timestamp must be > 0, but was " + timestamp );
    addParameter( getRequestKey( VISITOR_FIRST_VISIT ), Long.valueOf( timestamp ) );
    return this;
  }

  public VisitorInformation setCampaignName( String campaignName ) {
    whenNull( campaignName ).throwIllegalArgument( "CampaignName must not be null." );
    when( campaignName.isEmpty() ).throwIllegalArgument( "CampaignName must not be empty." );
    addParameter( getRequestKey( VISITOR_CAMPAIGN_NAME ), campaignName );
    return this;
  }

  public VisitorInformation setCampaignKeyword( String campaignKeyword ) {
    whenNull( campaignKeyword ).throwIllegalArgument( "CampaignKeyword must not be null." );
    when( campaignKeyword.isEmpty() ).throwIllegalArgument( "CampaignKeyword must not be empty." );
    addParameter( getRequestKey( VISITOR_CAMPAIGN_KEYWORD ), campaignKeyword );
    return this;
  }

  public VisitorInformation setScreenResolution( String screenResolution ) {
    whenNull( screenResolution ).throwIllegalArgument( "ScreenResolution must not be null." );
    when( screenResolution.isEmpty() ).throwIllegalArgument( "ScreenResolution must not be null." );
    addParameter( getRequestKey( VISITOR_RESOLUTION ), screenResolution );
    return this;
  }

  public VisitorInformation setHour( int hour ) {
    when( hour < 0 || hour > 23 ).throwIllegalArgument( "Hour must be < 23 and > 0, but was " + hour );
    addParameter( getRequestKey( VISITOR_HOUR ), Integer.valueOf( hour ) );
    return this;
  }

  public VisitorInformation setMinute( int minute ) {
    when( minute < 0 || minute > 59 ).throwIllegalArgument( "Minute must be < 59 and > 0, but was " + minute );
    addParameter( getRequestKey( VISITOR_MINUTE ), Integer.valueOf( minute ) );
    return this;
  }

  public VisitorInformation setSecond( int second ) {
    when( second < 0 || second > 60 ).throwIllegalArgument( "Second must be < 60 and > 0, but was " + second );
    addParameter( getRequestKey( VISITOR_SECOND ), Integer.valueOf( second ) );
    return this;
  }

  public VisitorInformation setUserAgentOverride( String userAgentOverride ) {
    whenNull( userAgentOverride ).throwIllegalArgument( "UserAgentOverride must not be null." );
    when( userAgentOverride.isEmpty() ).throwIllegalArgument( "UserAgentOverride must not be empty." );
    addParameter( getRequestKey( VISITOR_USER_AGENT_OVERRIDE ), userAgentOverride );
    return this;
  }

  public VisitorInformation setLanguageOverride( String languageOverride ) {
    whenNull( languageOverride ).throwIllegalArgument( "LanguageOverride must not be null." );
    when( languageOverride.isEmpty() ).throwIllegalArgument( "LanguageOverride must not be empty." );
    addParameter( getRequestKey( VISITOR_LANGUAGE_OVERRIDE ), languageOverride );
    return this;
  }
}

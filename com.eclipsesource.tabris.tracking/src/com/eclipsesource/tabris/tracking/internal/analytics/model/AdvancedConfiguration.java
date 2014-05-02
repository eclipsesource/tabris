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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.APP_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.APP_VERSION;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.CONTENT_DESCRIPTION;
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

import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.tabris.tracking.internal.Requestable;


@SuppressWarnings("restriction")
public class AdvancedConfiguration implements Requestable {

  private final Map<String, Object> parameter;

  public AdvancedConfiguration() {
    this.parameter = new HashMap<String, Object>();
  }

  public AdvancedConfiguration setIpOverride( String ipOverride ) {
    whenNull( ipOverride ).throwIllegalArgument( "IpOverride must not be null." );
    when( ipOverride.isEmpty() ).throwIllegalArgument( "IpOverride must not be empty." );
    parameter.put( getRequestKey( IP_OVERRIDE ), ipOverride );
    return this;
  }

  public AdvancedConfiguration setScreenResolution( String screenResolution ) {
    whenNull( screenResolution ).throwIllegalArgument( "ScreenResolution must not be null." );
    when( screenResolution.isEmpty() ).throwIllegalArgument( "ScreenResolution must not be empty." );
    parameter.put( getRequestKey( SCREEN_RESOLUTION ), screenResolution );
    return this;
  }

  public AdvancedConfiguration setSessionControl( String sessionControl ) {
    whenNull( sessionControl ).throwIllegalArgument( "SessionControl must not be null." );
    when( sessionControl.isEmpty() ).throwIllegalArgument( "SessionControl must not be empty." );
    parameter.put( getRequestKey( SESSION_CONTROL ), sessionControl );
    return this;
  }

  public AdvancedConfiguration setUserAgentOverride( String userAgentOverride ) {
    whenNull( userAgentOverride ).throwIllegalArgument( "UserAgentOverride must not be null." );
    when( userAgentOverride.isEmpty() ).throwIllegalArgument( "UserAgentOverride must not be empty." );
    parameter.put( getRequestKey( USER_AGENT_OVERRIDE ), userAgentOverride );
    return this;
  }

  public AdvancedConfiguration setUserId( String userId ) {
    whenNull( userId ).throwIllegalArgument( "UserId must not be null." );
    when( userId.isEmpty() ).throwIllegalArgument( "UserId must not be empty." );
    parameter.put( getRequestKey( USER_ID ), userId );
    return this;
  }

  public AdvancedConfiguration setUserLanguage( String userLanguage ) {
    whenNull( userLanguage ).throwIllegalArgument( "UserLanguage must not be null." );
    when( userLanguage.isEmpty() ).throwIllegalArgument( "UserLanguage must not be empty." );
    parameter.put( getRequestKey( USER_LANGUAGE ), userLanguage );
    return this;
  }

  public AdvancedConfiguration setViewportSize( String viewportSize ) {
    whenNull( viewportSize ).throwIllegalArgument( "ViewportSize must not be null." );
    when( viewportSize.isEmpty() ).throwIllegalArgument( "ViewportSize must not be empty." );
    parameter.put( getRequestKey( VIEWPORT_SIZE ), viewportSize );
    return this;
  }

  public AdvancedConfiguration setAppVersion( String appVersion ) {
    whenNull( appVersion ).throwIllegalArgument( "AppVersion must not be null." );
    when( appVersion.isEmpty() ).throwIllegalArgument( "AppVersion must not be empty." );
    parameter.put( getRequestKey( APP_VERSION ), appVersion );
    return this;
  }

  public AdvancedConfiguration setAppId( String appId ) {
    whenNull( appId ).throwIllegalArgument( "AppId must not be null." );
    when( appId.isEmpty() ).throwIllegalArgument( "AppId must not be empty." );
    parameter.put( getRequestKey( APP_ID ), appId );
    return this;
  }

  public AdvancedConfiguration setDocumentHostName( String documentHostName ) {
    whenNull( documentHostName ).throwIllegalArgument( "DocumentHostName must not be null." );
    when( documentHostName.isEmpty() ).throwIllegalArgument( "DocumentHostName must not be empty." );
    parameter.put( getRequestKey( DOCUMENT_HOST_NAME ), documentHostName );
    return this;
  }

  public AdvancedConfiguration setDocumentPath( String documentPath ) {
    whenNull( documentPath ).throwIllegalArgument( "DocumentPath must not be null." );
    when( documentPath.isEmpty() ).throwIllegalArgument( "DocumentPath must not be empty." );
    parameter.put( getRequestKey( DOCUMENT_PATH ), documentPath );
    return this;
  }

  public AdvancedConfiguration setDocumentTitle( String documentTitle ) {
    whenNull( documentTitle ).throwIllegalArgument( "DocumentTitle must not be null." );
    when( documentTitle.isEmpty() ).throwIllegalArgument( "DocumentTitle must not be empty." );
    parameter.put( getRequestKey( DOCUMENT_TITLE ), documentTitle );
    return this;
  }

  public AdvancedConfiguration setContentDescription( String contentDescription ) {
    whenNull( contentDescription ).throwIllegalArgument( "ContentDescription must not be null." );
    when( contentDescription.isEmpty() ).throwIllegalArgument( "ContentDescription must not be empty." );
    parameter.put( getRequestKey( CONTENT_DESCRIPTION ), contentDescription );
    return this;
  }

  public AdvancedConfiguration setCurrencyCode( String currencyCode ) {
    whenNull( currencyCode ).throwIllegalArgument( "CurrencyCode must not be null." );
    when( currencyCode.isEmpty() ).throwIllegalArgument( "CurrencyCode must not be empty." );
    parameter.put( getRequestKey( CURRENCY_CODE ), currencyCode );
    return this;
  }

  public AdvancedConfiguration setCustomDimension( int index, String customDimension ) {
    whenNull( customDimension ).throwIllegalArgument( "CustomDimension must not be null." );
    when( customDimension.isEmpty() ).throwIllegalArgument( "CustomDimension must not be empty." );
    when( index <= 0 ).throwIllegalArgument( "Index must be > 0 but was " + index );
    parameter.put( getRequestKey( CUSTOM_DIMENSION ) + String.valueOf( index ), customDimension );
    return this;
  }

  public AdvancedConfiguration setCustomMetric( int index, String customMetric ) {
    whenNull( customMetric ).throwIllegalArgument( "CustomMetric must not be null." );
    when( customMetric.isEmpty() ).throwIllegalArgument( "CustomMetric must not be empty." );
    when( index <= 0 ).throwIllegalArgument( "Index must be > 0 but was " + index );
    parameter.put( getRequestKey( CUSTOM_METRIC ) + String.valueOf( index ), customMetric );
    return this;
  }

  @Override
  public Map<String, Object> getParameter() {
    return parameter;
  }
}

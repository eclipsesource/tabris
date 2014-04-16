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
package com.eclipsesource.tabris.tracking.internal.analytics;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.util.Map;

import com.eclipsesource.tabris.tracking.internal.analytics.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.analytics.model.AnalyticsConfiguration;
import com.eclipsesource.tabris.tracking.internal.analytics.model.hit.Hit;
import com.eclipsesource.tabris.tracking.internal.analytics.request.HttpRequest;
import com.eclipsesource.tabris.tracking.internal.analytics.request.RequestAssembler;


@SuppressWarnings("restriction")
public class GoogleAnalytics {

  static final String ANALYTICS_URL = "http://www.google-analytics.com/collect";

  private final String baseUrl;
  private final AnalyticsConfiguration configuration;
  private final String appName;

  public GoogleAnalytics( String appName, AnalyticsConfiguration configuration ) {
    this( ANALYTICS_URL, appName, configuration );
  }

  GoogleAnalytics( String baseUrl, String appName, AnalyticsConfiguration configuration ) {
    validateArguments( configuration, appName );
    this.baseUrl = baseUrl;
    this.configuration = configuration;
    this.appName = appName;
  }

  private void validateArguments( AnalyticsConfiguration configuration, String appName ) {
    whenNull( configuration ).throwIllegalArgument( "BaseAnalyticsConfiguration must not be null." );
    whenNull( appName ).throwIllegalArgument( "AppName must not be null." );
    when( "".equals( appName ) ).throwIllegalArgument( "AppName must not be empty." );
  }

  public void track( Hit hit, String clientId ) {
    track( hit, clientId, new AdvancedConfiguration() );
  }

  public void track( Hit hit, String clientId, AdvancedConfiguration advancedConfiguration ) {
    validateArguments( hit, clientId, advancedConfiguration );
    RequestAssembler requestAssembler = new RequestAssembler( appName, clientId, configuration, hit, advancedConfiguration );
    Map<String, Object> request = requestAssembler.assemble();
    HttpRequest httpRequest = HttpRequest.get( baseUrl, request, true );
    verifyResponse( httpRequest );
  }

  private void validateArguments( Hit hit, String clientId, AdvancedConfiguration advancedConfiguration ) {
    whenNull( clientId ).throwIllegalArgument( "ClientId must not be null." );
    when( clientId.isEmpty() ).throwIllegalArgument( "ClientId must not be empty." );
    whenNull( hit ).throwIllegalArgument( "Hit must not be null." );
    whenNull( advancedConfiguration ).throwIllegalArgument( "Optional must not be null." );
  }

  private void verifyResponse( HttpRequest httpRequest ) {
    if( httpRequest.code() != 200 ) {
      throw new IllegalStateException( "Google Analytics GET request failed, HTTP status code: "
                                       + httpRequest.code()
                                       + "\n"
                                       + httpRequest.message() );
    }
  }

}

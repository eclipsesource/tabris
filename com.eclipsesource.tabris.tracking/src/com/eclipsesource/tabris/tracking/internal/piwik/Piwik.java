/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.util.Map;

import com.eclipsesource.tabris.tracking.internal.analytics.request.HttpRequest;
import com.eclipsesource.tabris.tracking.internal.piwik.model.AdvancedConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.PiwikConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.VisitorInformation;
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.Action;
import com.eclipsesource.tabris.tracking.internal.piwik.request.RequestAssembler;

@SuppressWarnings("restriction")
public class Piwik {

  private final String piwikUrl;
  private final PiwikConfiguration configuration;

  public Piwik( String piwikUrl, PiwikConfiguration configuration ) {
    whenNull( piwikUrl ).throwIllegalArgument( "PiwikUrl must not be null." );
    when( piwikUrl.isEmpty() ).throwIllegalArgument( "PiwikUrl must not be empty." );
    whenNull( configuration ).throwIllegalArgument( "Configuration must not be null." );
    this.configuration = configuration;
    this.piwikUrl = piwikUrl;
  }

  public void track( Action action ) {
    track( action, null, null );
  }

  public void track( Action action, VisitorInformation visitorInformation ) {
    track( action, visitorInformation, null );
  }

  public void track( Action action, AdvancedConfiguration advancedConfiguration ) {
    track( action, null, advancedConfiguration );
  }

  public void track( Action action,
                     VisitorInformation visitorInformation,
                     AdvancedConfiguration advancedConfiguration ) {
    RequestAssembler requestAssembler = new RequestAssembler( configuration,
                                                              action,
                                                              visitorInformation,
                                                              advancedConfiguration );
    Map<String, Object> request = requestAssembler.assemble();
    verifyResponse( HttpRequest.get( piwikUrl, request, true ) );
  }

  private void verifyResponse( HttpRequest httpRequest ) {
    if( httpRequest.code() != 200 ) {
      throw new IllegalStateException( "Piwik GET request failed, HTTP status code: "
                                       + httpRequest.code()
                                       + "\n"
                                       + httpRequest.message() );
    }
  }
}

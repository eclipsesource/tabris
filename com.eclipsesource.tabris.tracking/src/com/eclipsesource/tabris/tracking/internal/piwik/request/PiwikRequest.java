/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.request;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.RANDOM;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.REC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.eclipsesource.tabris.tracking.internal.Requestable;
import com.eclipsesource.tabris.tracking.internal.piwik.model.PiwikConfiguration;
import com.eclipsesource.tabris.tracking.internal.piwik.model.action.Action;


@SuppressWarnings("restriction")
public class PiwikRequest {

  private final PiwikConfiguration configuration;
  private final Action action;
  private final List<Requestable> requestables;

  public PiwikRequest( PiwikConfiguration configuration, Action action, Requestable... requestables ) {
    validateArguments( configuration, action );
    this.configuration = configuration;
    this.action = action;
    this.requestables = Arrays.asList( requestables );
  }

  private void validateArguments( PiwikConfiguration configuration, Action action ) {
    whenNull( configuration ).throwIllegalArgument( "Configuration must not be null." );
    whenNull( action ).throwIllegalArgument( "Action must not be null." );
  }

  public Map<String, Object> assemble() {
    Map<String, Object> parameters = createRequestableParameters();
    parameters.putAll( configuration.getParameter() );
    parameters.putAll( action.getParameter() );
    parameters.put( getRequestKey( RANDOM ), UUID.randomUUID().toString() );
    parameters.put( getRequestKey( REC ), Integer.valueOf( 1 ) );
    return parameters;
  }

  private Map<String, Object> createRequestableParameters() {
    HashMap<String, Object> parameters = new HashMap<String, Object>();
    for( Requestable requestable : requestables ) {
      if( requestable != null ) {
        parameters.putAll( requestable.getParameter() );
      }
    }
    return parameters;
  }
}

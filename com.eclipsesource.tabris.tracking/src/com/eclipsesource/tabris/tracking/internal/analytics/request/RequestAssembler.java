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
package com.eclipsesource.tabris.tracking.internal.analytics.request;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.APP_NAME;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.CLIENT_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.getRequestValue;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValues.HIT_APPVIEW;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eclipsesource.tabris.tracking.internal.analytics.model.Requestable;


@SuppressWarnings("restriction")
public class RequestAssembler implements Serializable {

  private final String appName;
  private final List<Requestable> requestables;
  private final String clientId;

  public RequestAssembler( String appName, String clientId, Requestable... requestables ) {
    validateArguments( appName, clientId );
    this.appName = appName;
    this.clientId = clientId;
    this.requestables = Arrays.asList( requestables );
  }

  private void validateArguments( String appName, String clientId ) {
    whenNull( appName ).throwIllegalArgument( "AppName must not be null." );
    when( appName.isEmpty() ).throwIllegalArgument( "AppName must not be empty." );
    whenNull( clientId ).throwIllegalArgument( "ClientId must not be null." );
    when( clientId.isEmpty() ).throwIllegalArgument( "ClientId must not be empty." );
  }

  public Map<String, Object> assemble() {
    Map<String, Object> parameter = createRequestableParameter();
    addAppName( parameter );
    checkAppNameSet( parameter );
    parameter.put( getRequestKey( CLIENT_ID ), clientId );
    return parameter;
  }

  private Map<String, Object> createRequestableParameter() {
    Map<String, Object> parameter = new HashMap<String, Object>();
    for( Requestable requestable : requestables ) {
      parameter.putAll( requestable.getParameter() );
    }
    return parameter;
  }

  private void addAppName( Map<String, Object> result ) {
    if( appName != null ) {
      result.put( getRequestKey( APP_NAME ), appName );
    }
  }

  private void checkAppNameSet( Map<String, Object> hitRequestChunk ) {
    when( hitRequestChunk.containsValue( getRequestValue( HIT_APPVIEW ) ) && appName == null )
      .throwIllegalState( "AppName must be set when assembling an AppViewHit request." );
  }
}

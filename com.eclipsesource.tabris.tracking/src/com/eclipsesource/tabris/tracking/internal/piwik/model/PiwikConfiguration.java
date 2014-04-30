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
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.API_VERSION;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.SITE_ID;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("restriction")
public class PiwikConfiguration implements Requestable {

  private final Map<String, Object> parameters;

  public PiwikConfiguration( String apiVersion, int siteId ) {
    whenNull( apiVersion ).throwIllegalArgument( "ApiVersion must not be null." );
    when(apiVersion.isEmpty()).throwIllegalArgument( "ApiVersion must not be empty." );
    when( siteId <= 0 ).throwIllegalArgument( "SiteId must be > 0 but was " + siteId );
    parameters = new HashMap<String, Object>();
    addParameter( getRequestKey( API_VERSION ), apiVersion );
    addParameter( getRequestKey( SITE_ID ), Integer.valueOf( siteId ) );
  }

  protected void addParameter( String key, Object value ) {
    parameters.put( key, value );
  }

  @Override
  public Map<String, Object> getParameters() {
    return parameters;
  }
}

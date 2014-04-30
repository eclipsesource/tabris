/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_CUSTOM_VARIABLES;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_GENERATION_TIME;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_NAME;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_URL;

import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.tabris.tracking.internal.piwik.model.Requestable;

@SuppressWarnings("restriction")
public class Action implements Requestable {

  private final Map<String, Object> parameters;

  public Action( String actionUrl ) {
    whenNull( actionUrl ).throwIllegalArgument( "ActionUrl must not be null." );
    when( actionUrl.isEmpty() ).throwIllegalArgument( "ActionUrl must not be empty." );
    parameters = new HashMap<String, Object>();
    addParameter( getRequestKey( ACTION_URL ), actionUrl );
  }

  protected void addParameter( String key, Object value ) {
    parameters.put( key, value );
  }

  @Override
  public Map<String, Object> getParameters() {
    return parameters;
  }

  public Action setName( String name ) {
    whenNull( name ).throwIllegalArgument( "Name must not be null." );
    when( name.isEmpty() ).throwIllegalArgument( "Name must not be empty." );
    addParameter( getRequestKey( ACTION_NAME ), name );
    return this;
  }

  public Action setCustomVariables( String customVariables ) {
    whenNull( customVariables ).throwIllegalArgument( "CustomVariables must not be null." );
    when( customVariables.isEmpty() ).throwIllegalArgument( "CustomVariables must not be empty." );
    addParameter( getRequestKey( ACTION_CUSTOM_VARIABLES ), customVariables );
    return this;
  }

  public Action setGenerationTime( int generationTime ) {
    when( generationTime <= 0 ).throwIllegalArgument( "GenerationTime must be > 0, but was " + generationTime );
    addParameter( getRequestKey( ACTION_GENERATION_TIME ), Integer.valueOf( generationTime ) );
    return this;
  }
}

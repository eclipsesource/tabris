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
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.TRACKING_ID;
import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeys.VERSION;

import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.tabris.tracking.internal.Requestable;


@SuppressWarnings("restriction")
public class AnalyticsConfiguration implements Requestable {

  private final Map<String, Object> parameter;

  public AnalyticsConfiguration( String version, String trackingId ) {
    validateArguments( version, trackingId );
    parameter = new HashMap<String, Object>();
    parameter.put( getRequestKey( VERSION ), version );
    parameter.put( getRequestKey( TRACKING_ID ), trackingId );
  }

  private void validateArguments( String version, String trackingId ) {
    validateVersion( version );
    validateTrackingId( trackingId );
  }

  private void validateVersion( String version ) {
    whenNull( version ).throwIllegalArgument( "Version must not be null." );
    when( version.isEmpty() ).throwIllegalArgument( "Version must not be empty." );
  }

  private void validateTrackingId( String trackingId ) {
    whenNull( trackingId ).throwIllegalArgument( "TrackingId must not be null." );
    when( trackingId.isEmpty() ).throwIllegalArgument( "TrackingId must not be empty." );
  }

  @Override
  public Map<String, Object> getParameter() {
    return parameter;
  }

}

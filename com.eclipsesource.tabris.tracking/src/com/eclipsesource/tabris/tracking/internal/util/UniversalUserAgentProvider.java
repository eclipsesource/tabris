/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.util;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import com.eclipsesource.tabris.tracking.TrackingInfo;

@SuppressWarnings("restriction")
public class UniversalUserAgentProvider implements UserAgentProvider {

  @Override
  public String getUserAgent( TrackingInfo trackingInfo ) {
    whenNull( trackingInfo ).throwIllegalArgument( "TrackingInfo must not be null." );
    whenNull( trackingInfo.getUserAgent() ).throwIllegalState( "TrackingInfo was set, but UserAgent was null." );
    when( trackingInfo.getUserAgent().isEmpty() ).throwIllegalState( "TrackingInfo was set, but UserAgent was empty." );
    return trackingInfo.getUserAgent();
  }
}

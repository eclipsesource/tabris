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
public class AndroidUserAgentGenerator implements UserAgentProvider {

  @Override
  public String getUserAgent( TrackingInfo info ) {
    validateArguments( info );
    return "Mozilla/5.0 (Linux; U; Android "
           + info.getDeviceOsVersion()
           + "; "
           + info.getClientLocale()
           + "; "
           + info.getDeviceModel()
           + ") AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
  }

  private void validateArguments( TrackingInfo info ) {
    whenNull( info ).throwIllegalArgument( "TrackingInfo must not be null." );
    whenNull( info.getDeviceOsVersion() ).throwIllegalState( "TrackingInfo was set, but DeviceVersion was null." );
    when( info.getDeviceOsVersion().isEmpty() ).throwIllegalState( "TrackingInfo was set, but DeviceVersion was empty." );
    whenNull( info.getClientLocale() ).throwIllegalState( "TrackingInfo was set, but ClientLocale was null." );
    whenNull( info.getDeviceModel() ).throwIllegalState( "TrackingInfo was set, but DeviceModel was null." );
    when( info.getDeviceModel().isEmpty() ).throwIllegalState( "TrackingInfo was set, but DeviceModel was empty." );
  }
}

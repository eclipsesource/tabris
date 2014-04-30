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
public class IOSUserAgentGenerator implements UserAgentProvider {

  @Override
  public String getUserAgent( TrackingInfo info ) {
    validateArguments( info );
    return "Mozilla/5.0 ("
           + info.getDeviceModel()
           + "; U; CPU iPhone OS "
           + info.getDeviceOsVersion().replace( ".", "_" )
           + " like Mac OS X; "
           + info.getClientLocale()
           + " ) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7";
  }

  private void validateArguments( TrackingInfo info ) {
    whenNull( info ).throwIllegalArgument( "TrackingInfo must not be null." );
    whenNull( info.getDeviceModel() ).throwIllegalState( "TrackingInfo present, but DeviceModel was null." );
    when( info.getDeviceModel().isEmpty() ).throwIllegalState( "TrackingInfo present, but DeviceModel was empty." );
    whenNull( info.getDeviceOsVersion() ).throwIllegalState( "TrackingInfo present, but DeviceOsVersion was null." );
    when( info.getDeviceOsVersion().isEmpty() ).throwIllegalState( "TrackingInfo present, but DeviceOsVersion was empty." );
    whenNull( info.getClientLocale() ).throwIllegalState( "TrackingInfo present, but ClientLocale was null." );
  }
}

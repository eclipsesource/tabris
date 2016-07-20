/*******************************************************************************
 * Copyright (c) 2013, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.device.ClientDevice.Platform.ANDROID;
import static com.eclipsesource.tabris.device.ClientDevice.Platform.IOS;
import static com.eclipsesource.tabris.device.ClientDevice.Platform.SWT;
import static com.eclipsesource.tabris.device.ClientDevice.Platform.WEB;
import static com.eclipsesource.tabris.device.ClientDevice.Platform.WINDOWS;
import static com.eclipsesource.tabris.internal.Constants.ID_ANDROID;
import static com.eclipsesource.tabris.internal.Constants.ID_IOS;
import static com.eclipsesource.tabris.internal.Constants.ID_SWT;
import static com.eclipsesource.tabris.internal.Constants.ID_WINDOWS;
import static com.eclipsesource.tabris.internal.Constants.USER_AGENT;

import org.eclipse.rap.rwt.RWT;

import com.eclipsesource.tabris.device.ClientDevice.Platform;


public class DeviceUtil {

  public static Platform getPlatform() {
    String userAgent = RWT.getRequest().getHeader( USER_AGENT );
    Platform result = WEB;
    if( userAgent != null ) {
      if( userAgent.contains( ID_IOS ) ) {
        result = IOS;
      } else if( userAgent.contains( ID_ANDROID ) ) {
        result = ANDROID;
      } else if( userAgent.contains( ID_WINDOWS ) ) {
        result = WINDOWS;
      } else if( userAgent.contains( ID_SWT ) ) {
        result = SWT;
      }
    }

    return result;
  }

  private DeviceUtil() {
    // prevent instantiation
  }
}

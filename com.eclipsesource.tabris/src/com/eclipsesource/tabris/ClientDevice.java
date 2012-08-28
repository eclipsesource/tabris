/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris;

import org.eclipse.rap.rwt.RWT;

import com.eclipsesource.tabris.internal.Constants;


public class ClientDevice {
  
  public boolean isPlatform( Platform platform ) {
    String userAgent = RWT.getRequest().getHeader( Constants.USER_AGENT );
    Platform result = Platform.WEB;
    if( userAgent != null && userAgent.contains( Constants.ID_IOS ) ) {
      result = Platform.IOS;
    } else if( userAgent != null && userAgent.contains( Constants.ID_ANDROID ) ) {
      result = Platform.ANDROID;
    }
    return result == platform;
  }

  public static ClientDevice getCurrent() {
    return new ClientDevice(); 
  }
}

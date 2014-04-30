/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.util;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.tabris.device.ClientDevice.Platform;


@SuppressWarnings("restriction")
public class UserAgentUtil {

  static Map<Platform, UserAgentProvider> providers;

  static {
    Map<Platform, UserAgentProvider> mapping = new HashMap<Platform, UserAgentProvider>();
    mapping.put( Platform.ANDROID, new AndroidUserAgentGenerator() );
    mapping.put( Platform.IOS, new IOSUserAgentGenerator() );
    mapping.put( Platform.SWT, new UniversalUserAgentProvider() );
    mapping.put( Platform.WEB, new UniversalUserAgentProvider() );
    mapping.put( ( Platform )null, new UniversalUserAgentProvider() );
    providers = Collections.unmodifiableMap( mapping );
  }

  public static UserAgentProvider getProvider( Platform platform ) {
    whenNull( platform ).throwIllegalArgument( "Platform must not be null" );
    return providers.get( platform );
  }

  private UserAgentUtil() {
    //prevent instantiation
  }
}

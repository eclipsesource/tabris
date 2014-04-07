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
package com.eclipsesource.tabris.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.rap.rwt.RWT;


public class VersionCheck {

  private static final Pattern VERSION_PATTERN = Pattern.compile( ".*/([0-9]*\\.[0-9]*)\\.[0-9]* \\(.*\\)" );
  static final String TABRIS_SERVER_VERSION = "1.4";

  private final String clientVersion;
  private final String serverVersion;

  public VersionCheck() {
    this( TABRIS_SERVER_VERSION );
  }

  VersionCheck( String serverVersion ) {
    this.serverVersion = serverVersion;
    this.clientVersion = findClientVersion();
  }

  private String findClientVersion() {
    String userAgent = RWT.getRequest().getHeader( Constants.USER_AGENT );
    Matcher matcher = VERSION_PATTERN.matcher( userAgent );
    if( matcher.matches() ) {
      return matcher.group( 1 );
    }
    return "";
  }

  public boolean matches() {
    return serverVersion.equals( clientVersion );
  }

  public String getServerVersion() {
    return serverVersion;
  }

  public String getClientVersion() {
    return clientVersion;
  }
}

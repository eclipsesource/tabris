/*******************************************************************************
 * Copyright (c) 2014, 2017 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.rap.rwt.RWT;

import com.eclipsesource.tabris.VersionCheck;


public class VersionChecker {

  static final String TABRIS_SERVER_VERSION = "3.4.0";
  private static final Pattern VERSION_PATTERN = Pattern.compile( ".*/([0-9]*\\.[0-9]*\\.[0-9])* \\(.*\\)" );

  private final String clientVersion;
  private final VersionCheck versionCheck;
  private final String serverVersion;

  public VersionChecker( VersionCheck versionCheck ) {
    this( versionCheck, TABRIS_SERVER_VERSION );
  }

  VersionChecker( VersionCheck versionCheck, String serverVersion ) {
    whenNull( versionCheck ).throwIllegalArgument( "versionCheck must not be null" );
    this.versionCheck = versionCheck;
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

  public boolean accept() {
    return versionCheck.accept( clientVersion, serverVersion );
  }

  public String getErrorMessage() {
    return versionCheck.getErrorMessage( clientVersion, serverVersion );
  }

}

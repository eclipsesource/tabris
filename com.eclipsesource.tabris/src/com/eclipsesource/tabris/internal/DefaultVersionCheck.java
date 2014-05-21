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

import com.eclipsesource.tabris.VersionCheck;


public class DefaultVersionCheck implements VersionCheck {

  private static final Pattern SHORT_VERSION_PATTERN = Pattern.compile( "([0-9]*\\.[0-9]*)\\.[0-9]*" );

  @Override
  public boolean accept( String clientVersion, String serverVersion ) {
    return getShortVersion( serverVersion ).equals( getShortVersion( clientVersion ) );
  }

  @Override
  public String getErrorMessage( String clientVersion, String serverVersion ) {
    return "Incompatible Tabris Versions:\nClient "
           + getShortVersion( clientVersion )
           + " vs. Server "
           + getShortVersion( serverVersion );
  }

  private String getShortVersion( String version ) {
    Matcher matcher = SHORT_VERSION_PATTERN.matcher( version );
    if( matcher.matches() ) {
      return matcher.group( 1 );
    }
    return version;
  }

}

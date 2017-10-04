/*******************************************************************************
 * Copyright (c) 2012, 2017 EclipseSource and others.
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
import static com.eclipsesource.tabris.device.ClientDevice.Platform.WINDOWS;
import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Constants.HEADER_SERVER_ID;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_VERSION_CHECK;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_ANDROID;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_IOS;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_WINDOWS;
import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.internal.client.ClientProvider;
import org.eclipse.rap.rwt.internal.protocol.ProtocolMessageWriter;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.internal.theme.ThemeUtil;

import com.eclipsesource.tabris.VersionCheck;
import com.eclipsesource.tabris.device.ClientDevice.Platform;


@SuppressWarnings("restriction")
public class TabrisClientProvider implements ClientProvider, Serializable {

  private final String serverId;
  private final VersionCheck versionCheck;

  public TabrisClientProvider( VersionCheck versionCheck ) {
    this( versionCheck, null );
  }

  public TabrisClientProvider( VersionCheck versionCheck, String serverId ) {
    verifyServerId( serverId );
    this.versionCheck = versionCheck;
    this.serverId = serverId;
  }

  private void verifyServerId( String serverId ) {
    if( serverId != null ) {
      when( serverId.isEmpty() ).throwIllegalArgument( "ServerId must not be empty" );
    }
  }

  @Override
  public boolean accept( HttpServletRequest request ) {
    Platform platform = DeviceUtil.getPlatform();
    boolean result = platform == ANDROID || platform == IOS || platform == WINDOWS;
    if( result ) {
      setPlatformTheme( platform );
      setServerId();
      validateVersion();
    }
    return result;
  }

  private void setPlatformTheme( Platform platform ) {
    if( platform == IOS ) {
      ThemeUtil.setCurrentThemeId( RWT.getUISession(), THEME_ID_IOS );
    } else if( platform == ANDROID ) {
      ThemeUtil.setCurrentThemeId( RWT.getUISession(), THEME_ID_ANDROID );
    } else if( platform == WINDOWS ) {
      ThemeUtil.setCurrentThemeId( RWT.getUISession(), THEME_ID_WINDOWS );
    }
  }

  private void setServerId() {
    HttpServletResponse response = RWT.getResponse();
    if( serverId != null ) {
      response.addHeader( HEADER_SERVER_ID, serverId );
    }
  }

  private void validateVersion() {
    if( wantsVersionCheck() ) {
      checkVersion();
    }
  }

  private void checkVersion() {
    VersionChecker versionChecker = new VersionChecker( versionCheck );
    if( !versionChecker.accept() ) {
      writeInvalidVersion( versionChecker.getErrorMessage() );
    }
  }

  private boolean wantsVersionCheck() {
    String property = System.getProperty( PROPERTY_VERSION_CHECK );
    if( property != null && !Boolean.getBoolean( property ) ) {
      return false;
    }
    return true;
  }

  private void writeInvalidVersion( String errorMsg ) {
    HttpServletResponse response = RWT.getResponse();
    response.setStatus( SC_PRECONDITION_FAILED );
    ProtocolMessageWriter writer = ContextProvider.getProtocolWriter();
    writer.appendHead( "error", JsonValue.valueOf( errorMsg ) );
  }

  @Override
  public Client getClient() {
    return new TabrisClientImpl();
  }
}

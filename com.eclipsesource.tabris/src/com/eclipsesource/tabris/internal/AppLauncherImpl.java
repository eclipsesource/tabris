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
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.PROPERTY_APP;
import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN;
import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN_URL;
import static com.eclipsesource.tabris.internal.Constants.TYPE_APP_LAUNCHER;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_URL;
import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.interaction.AppLauncher;
import com.eclipsesource.tabris.interaction.LaunchOptions;


public class AppLauncherImpl implements AppLauncher {

  private final RemoteObject remoteObject;

  public AppLauncherImpl() {
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( TYPE_APP_LAUNCHER );
  }

  @Override
  public void open( LaunchOptions launchOptions ) {
    checkArgumentNotNull( launchOptions, "LaunchOptions" );
    createOpenCall( launchOptions );
  }

  private void createOpenCall( LaunchOptions launchOptions ) {
    Map<String, Object> properties = new HashMap<String, Object>( launchOptions.getOptions() );
    properties.put( PROPERTY_APP, launchOptions.getApp().toString() );
    remoteObject.call( METHOD_OPEN, properties );
  }

  @Override
  public void openUrl( String url ) {
    checkArgumentNotNull( url, "URL" );
    validateUrl( url );
    createOpenUrlCall( url );
  }

  private void validateUrl( String url ) {
    try {
      new URL( url );
    } catch( MalformedURLException mue ) {
      throw new IllegalArgumentException( url + " is not a valid url", mue );
    }
  }

  private void createOpenUrlCall( String url ) {
    HashMap<String, Object> poperties = new HashMap<String, Object>();
    poperties.put( PROPERTY_URL, url );
    remoteObject.call( METHOD_OPEN_URL, poperties );
  }
}

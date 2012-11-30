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

import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectFactory;

import com.eclipsesource.tabris.interaction.AppLauncher;
import com.eclipsesource.tabris.interaction.LaunchOptions;


@SuppressWarnings("restriction")
public class AppLauncherImpl implements AppLauncher {

  private static final String APP_PROPERTY = "app";
  private static final String URL_PROPERTY = "url";
  private static final String METHOD_OPEN = "open";
  private static final String METHOD_OPEN_URL = "openUrl";
  
  private final RemoteObject remoteObject;

  public AppLauncherImpl() {
    remoteObject = RemoteObjectFactory.getInstance().createRemoteObject( "tabris.AppLauncher" );
  }

  @Override
  public void open( LaunchOptions launchOptions ) {
    argumentNotNull( launchOptions, "LaunchOptions" );
    createOpenCall( launchOptions );
  }

  private void createOpenCall( LaunchOptions launchOptions ) {
    Map<String, Object> properties = new HashMap<String, Object>( launchOptions.getOptions() );
    properties.put( APP_PROPERTY, launchOptions.getApp().toString() );
    remoteObject.call( METHOD_OPEN, properties );
  }

  @Override
  public void openUrl( String url ) {
    argumentNotNull( url, "URL" );
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
    poperties.put( URL_PROPERTY, url );
    remoteObject.call( METHOD_OPEN_URL, poperties );
  }
}

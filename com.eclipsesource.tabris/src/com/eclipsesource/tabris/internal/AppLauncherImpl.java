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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN;
import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN_URL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_APP;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_URL;
import static com.eclipsesource.tabris.internal.Constants.TYPE_APP_LAUNCHER;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.interaction.AppLauncher;
import com.eclipsesource.tabris.interaction.LaunchOptions;


public class AppLauncherImpl implements AppLauncher, Serializable {

  private final RemoteObject remoteObject;

  public AppLauncherImpl() {
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( TYPE_APP_LAUNCHER );
  }

  @Override
  public void open( LaunchOptions launchOptions ) {
    whenNull( launchOptions ).throwIllegalArgument( "Launch Options must not be null" );
    createOpenCall( launchOptions );
  }

  private void createOpenCall( LaunchOptions launchOptions ) {
    JsonObject properties = new JsonObject();
    addLaunchOptions( properties, launchOptions.getOptions() );
    properties.add( PROPERTY_APP, launchOptions.getApp().toString() );
    remoteObject.call( METHOD_OPEN, properties );
  }

  private void addLaunchOptions( JsonObject properties, Map<String, Object> options ) {
    for( Entry<String, Object> entry : options.entrySet() ) {
      if( entry.getValue() instanceof String ) {
        properties.add( entry.getKey(), ( String )entry.getValue() );
      } else if( entry.getValue() instanceof Boolean ) {
        properties.add( entry.getKey(), ( ( Boolean )entry.getValue() ).booleanValue() );
      }
    }
  }

  @Override
  public void openUrl( String url ) {
    whenNull( url ).throwIllegalArgument( "URL must not be null" );
    when( url.isEmpty() ).throwIllegalArgument( "URL must not be empty" );
    createOpenUrlCall( url );
  }

  private void createOpenUrlCall( String url ) {
    JsonObject poperties = new JsonObject();
    poperties.add( PROPERTY_URL, url );
    remoteObject.call( METHOD_OPEN_URL, poperties );
  }
}

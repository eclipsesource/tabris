/*******************************************************************************
 * Copyright (c) 2012,2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.eclipse.rap.rwt.SingletonUtil.getSessionInstance;

import org.eclipse.rap.rwt.client.service.ClientInfo;
import org.eclipse.rap.rwt.client.service.ClientService;

import com.eclipsesource.tabris.ClientStore;
import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.app.App;
import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.device.ClientDevice;
import com.eclipsesource.tabris.geolocation.Geolocation;
import com.eclipsesource.tabris.interaction.AppLauncher;


public class TabrisClientImpl implements TabrisClient {

  public TabrisClientImpl() {
    initializeServices();
  }

  private void initializeServices() {
    getService( ClientDevice.class );
    getService( App.class );
    getService( ClientStore.class );
    DataWhitelist.register();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends ClientService> T getService( Class<T> type ) {
    T result = null;
    if( type == AppLauncher.class ) {
      result = ( T )getSessionInstance( AppLauncherImpl.class );
    } else if( type == App.class ) {
      result = ( T )getSessionInstance( AppImpl.class );
    } else if( type == ClientStore.class ) {
      result = ( T )getSessionInstance( ClientStoreImpl.class );
    } else if( type == ClientDevice.class || type == ClientInfo.class ) {
      result = ( T )getSessionInstance( ClientDeviceImpl.class );
    } else if( type == Camera.class ) {
      result = ( T )getSessionInstance( CameraImpl.class );
    } else if( type == Geolocation.class ) {
      result = ( T )getSessionInstance( GeolocationImpl.class );
    }
    return result;
  }
}

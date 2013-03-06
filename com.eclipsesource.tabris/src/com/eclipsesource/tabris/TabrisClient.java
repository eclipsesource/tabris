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

import static org.eclipse.rap.rwt.SingletonUtil.getSessionInstance;

import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.client.service.ClientInfo;
import org.eclipse.rap.rwt.client.service.ClientService;
import org.eclipse.rap.rwt.internal.client.WidgetDataWhiteList;

import com.eclipsesource.tabris.app.App;
import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.device.ClientDevice;
import com.eclipsesource.tabris.geolocation.Geolocation;
import com.eclipsesource.tabris.interaction.AppLauncher;
import com.eclipsesource.tabris.internal.AppImpl;
import com.eclipsesource.tabris.internal.AppLauncherImpl;
import com.eclipsesource.tabris.internal.CameraImpl;
import com.eclipsesource.tabris.internal.ClientDeviceImpl;
import com.eclipsesource.tabris.internal.ClientStoreImpl;
import com.eclipsesource.tabris.internal.DataWhitelist;
import com.eclipsesource.tabris.internal.GeolocationImpl;


/**
 * <p>
 * Special Client implementation for Tabris. Can be obtained using RWT.getClient(). Current services are:
 * <ul>
 * <li>{@link ClientDevice}</li>
 * <li>{@link App}</li>
 * <li>{@link AppLauncher}</li>
 * <li>{@link ClientInfo}</li>
 * <li>{@link ClientStore}</li>
 * <li>{@link Camera}</li>
 * <li>{@link Geolocation}</li>
 * </ul>
 * They can be obtained using the getService( Class ) method.
 * </p>
 *
 * @since 0.9
 */
@SuppressWarnings("restriction")
public class TabrisClient implements Client {

  public TabrisClient() {
    initializeServices();
  }

  private void initializeServices() {
    getService( ClientDevice.class );
    getService( App.class );
    getService( ClientStore.class );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends ClientService> T getService( Class<T> type ) {
    T result = null;
    if( type == WidgetDataWhiteList.class ) {
      result = ( T )getSessionInstance( DataWhitelist.class );
    } else if( type == AppLauncher.class ) {
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

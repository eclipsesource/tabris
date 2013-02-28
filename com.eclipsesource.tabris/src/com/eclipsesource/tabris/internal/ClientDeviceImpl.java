/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;
import static com.eclipsesource.tabris.internal.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.lifecycle.ProcessActionRunner;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.device.ClientDevice;
import com.eclipsesource.tabris.device.ClientDeviceListener;


@SuppressWarnings("restriction")
public class ClientDeviceImpl extends AbstractOperationHandler implements ClientDevice {

  private static final String EVENT_DEVICE_CHANGE = "ClientDeviceChange";
  private static final String TYPE = "tabris.Device";
  private static final String TIMEZONE_OFFSET = "timezoneOffset";
  private static final String CONNECTION_TYPE = "connectionType";
  private static final String CAPABILITIES = "capabilities";
  private static final Object ORIENTATION = "orientation";

  private final RemoteObject remoteObject;
  private final List<ClientDeviceListener> clientDeviceListeners;
  private Locale[] locales;
  private Integer timezoneOffset;
  private ConnectionType connectionType;
  private Orientation orientation;
  private List<Capability> capabilities;

  public ClientDeviceImpl() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( TYPE );
    remoteObject.setHandler( this );
    clientDeviceListeners = new ArrayList<ClientDeviceListener>();
    readLocales();
  }

  private void readLocales() {
    HttpServletRequest request = ContextProvider.getRequest();
    if( request.getHeader( "Accept-Language" ) != null ) {
      Enumeration<Locale> locales = request.getLocales();
      this.locales = Collections.list( locales ).toArray( new Locale[ 1 ] );
    }
  }

  @Override
  public Platform getPlatform() {
    return DeviceUtil.getPlatform();
  }

  @Override
  public void handleSet( Map<String, Object> properties ) {
    if( properties.containsKey( TIMEZONE_OFFSET ) ) {
      timezoneOffset = ( Integer )properties.get( TIMEZONE_OFFSET );
    }
    if( properties.containsKey( CONNECTION_TYPE ) ) {
      connectionType = ConnectionType.valueOf( ( String )properties.get( CONNECTION_TYPE ) );
      fireConnectionTypeChange();
    }
    if( properties.containsKey( CAPABILITIES ) ) {
      setCapabilities( ( Object[] )properties.get( CAPABILITIES ) );
    }
    if( properties.containsKey( ORIENTATION ) ) {
      orientation = Orientation.valueOf( ( String )properties.get( ORIENTATION ) );
      fireOrientationChange();
    }
  }

  private void setCapabilities( Object[] objects ) {
    capabilities = new ArrayList<Capability>();
    for( Object capability : objects ) {
      capabilities.add( Capability.valueOf( ( String )capability ) );
    }
  }

  @Override
  public int getTimezoneOffset() {
    checkState( timezoneOffset, "timezoneOffset is not set." );
    return timezoneOffset.intValue();
  }

  @Override
  public Locale getLocale() {
    return locales == null ? null : locales[ 0 ];
  }

  @Override
  public Locale[] getLocales() {
    return locales == null ? new Locale[ 0 ] : locales.clone();
  }

  @Override
  public Orientation getOrientation() {
    checkState( orientation, "orientation is not set." );
    return orientation;
  }

  @Override
  public boolean hasCapability( Capability capability ) {
    checkState( capabilities, "capabilities not set." );
    return capabilities.contains( capability );
  }

  @Override
  public ConnectionType getConnectionType() {
    checkState( connectionType, "connectionType is not set." );
    return connectionType;
  }

  @Override
  public void addClientDeviceListener( ClientDeviceListener listener ) {
    checkArgumentNotNull( listener, ClientDeviceListener.class.getSimpleName() );
    if( clientDeviceListeners.isEmpty() ) {
      remoteObject.listen( EVENT_DEVICE_CHANGE, true );
    }
    clientDeviceListeners.add( listener );
  }

  @Override
  public void removeClientDeviceListener( ClientDeviceListener listener ) {
    checkArgumentNotNull( listener, ClientDeviceListener.class.getSimpleName() );
    clientDeviceListeners.remove( listener );
    if( clientDeviceListeners.isEmpty() ) {
      remoteObject.listen( EVENT_DEVICE_CHANGE, false );
    }
  }

  private void fireOrientationChange() {
    ProcessActionRunner.add( new Runnable() {
      @Override
      public void run() {
        for( ClientDeviceListener listener : clientDeviceListeners ) {
          listener.orientationChange( orientation );
        }
      }
    } );
  }

  private void fireConnectionTypeChange() {
    ProcessActionRunner.add( new Runnable() {
      @Override
      public void run() {
        for( ClientDeviceListener listener : clientDeviceListeners ) {
          listener.connectionTypeChanged( connectionType );
        }
      }
    } );
  }

}

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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CAPABILITIES;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CONNECTION_TYPE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MODEL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ORIENTATION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_OS_VERSION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SCALE_FACTOR;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TIMEZONE_OFFSET;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_VENDOR;
import static com.eclipsesource.tabris.internal.Constants.TYPE_CLIENT_DEVICE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.device.ClientDevice;
import com.eclipsesource.tabris.device.ClientDeviceListener;


@SuppressWarnings("restriction")
public class ClientDeviceImpl extends AbstractOperationHandler implements ClientDevice {

  private final RemoteObject remoteObject;
  private final List<ClientDeviceListener> clientDeviceListeners;
  private Locale[] locales;
  private Integer timezoneOffset;
  private ConnectionType connectionType;
  private Orientation orientation;
  private List<Capability> capabilities;
  private String vendor;
  private String model;
  private String osVersion;
  private float scaleFactor;

  public ClientDeviceImpl() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( TYPE_CLIENT_DEVICE );
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
  public String getVendor() {
    return vendor;
  }

  @Override
  public String getModel() {
    return model;
  }

  @Override
  public String getOSVersion() {
    return osVersion;
  }

  @Override
  public float getScaleFactor() {
    return scaleFactor;
  }

  @Override
  public Platform getPlatform() {
    return DeviceUtil.getPlatform();
  }

  @Override
  public void handleSet( JsonObject properties ) {
    setTimezoneOffset( properties );
    setConnectionType( properties );
    setCapabilities( properties );
    setOrientation( properties );
    setVendor( properties );
    setModel( properties );
    setOsVersion( properties );
    setScaleFactor( properties );
  }

  private void setTimezoneOffset( JsonObject properties ) {
    if( properties.get( PROPERTY_TIMEZONE_OFFSET ) != null ) {
      timezoneOffset = Integer.valueOf( properties.get( PROPERTY_TIMEZONE_OFFSET ).asInt() );
    }
  }

  private void setConnectionType( JsonObject properties ) {
    if( properties.get( PROPERTY_CONNECTION_TYPE ) != null ) {
      connectionType = ConnectionType.valueOf( properties.get( PROPERTY_CONNECTION_TYPE ).asString() );
      fireConnectionTypeChange();
    }
  }

  private void setCapabilities( JsonObject properties ) {
    if( properties.get( PROPERTY_CAPABILITIES ) != null ) {
      setCapabilities( properties.get( PROPERTY_CAPABILITIES ).asArray() );
    }
  }

  private void setOrientation( JsonObject properties ) {
    if( properties.get( PROPERTY_ORIENTATION ) != null ) {
      orientation = Orientation.valueOf( properties.get( PROPERTY_ORIENTATION ).asString() );
      fireOrientationChange();
    }
  }

  private void setVendor( JsonObject properties ) {
    if( properties.get( PROPERTY_VENDOR ) != null ) {
      vendor = properties.get( PROPERTY_VENDOR ).asString();
    }
  }

  private void setModel( JsonObject properties ) {
    if( properties.get( PROPERTY_MODEL ) != null ) {
      model = properties.get( PROPERTY_MODEL ).asString();
    }
  }

  private void setOsVersion( JsonObject properties ) {
    if( properties.get( PROPERTY_OS_VERSION ) != null ) {
      osVersion = properties.get( PROPERTY_OS_VERSION ).asString();
    }
  }

  private void setScaleFactor( JsonObject properties ) {
    if( properties.get( PROPERTY_SCALE_FACTOR ) != null ) {
      scaleFactor = properties.get( PROPERTY_SCALE_FACTOR ).asFloat();
    }
  }

  private void setCapabilities( JsonArray jsonArray ) {
    capabilities = new ArrayList<Capability>();
    for( JsonValue capability : jsonArray ) {
      capabilities.add( Capability.valueOf( capability.asString() ) );
    }
  }

  @Override
  public int getTimezoneOffset() {
    whenNull( timezoneOffset ).throwIllegalState( "timezoneOffset is not set." );
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
    whenNull( orientation ).throwIllegalState( "orientation is not set." );
    return orientation;
  }

  @Override
  public boolean hasCapability( Capability capability ) {
    whenNull( capabilities ).throwIllegalState( "capabilities is not set." );
    return capabilities.contains( capability );
  }

  @Override
  public ConnectionType getConnectionType() {
    whenNull( connectionType ).throwIllegalState( "connectionType is not set." );
    return connectionType;
  }

  @Override
  public void addClientDeviceListener( ClientDeviceListener listener ) {
    whenNull( listener ).throwIllegalArgument( "listener is not set." );
    clientDeviceListeners.add( listener );
  }

  @Override
  public void removeClientDeviceListener( ClientDeviceListener listener ) {
    whenNull( listener ).throwIllegalArgument( "listener is not set." );
    clientDeviceListeners.remove( listener );
  }

  private void fireOrientationChange() {
    ProcessActionRunner.add( new Runnable() {
      @Override
      public void run() {
        List<ClientDeviceListener> listeners = new ArrayList<ClientDeviceListener>( clientDeviceListeners );
        for( ClientDeviceListener listener : listeners ) {
          listener.orientationChange( orientation );
        }
      }
    } );
  }

  private void fireConnectionTypeChange() {
    ProcessActionRunner.add( new Runnable() {
      @Override
      public void run() {
        List<ClientDeviceListener> listeners = new ArrayList<ClientDeviceListener>( clientDeviceListeners );
        for( ClientDeviceListener listener : listeners ) {
          listener.connectionTypeChanged( connectionType );
        }
      }
    } );
  }

}

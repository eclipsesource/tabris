/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectFactory;
import org.eclipse.rap.rwt.internal.remote.RemoteOperationHandler;
import org.eclipse.rap.rwt.internal.service.ContextProvider;

import com.eclipsesource.tabris.event.App;
import com.eclipsesource.tabris.event.AppEvent;
import com.eclipsesource.tabris.event.AppListener;
import com.eclipsesource.tabris.event.EventType;

@SuppressWarnings("restriction")
public class AppImpl implements App {

  private static final String TYPE = "tabris.App";
  private static final String TIMEZONE_OFFSET = "timezoneOffset";
  private final RemoteObject remoteObject;
  private final Map<EventType, List<AppListener>> eventListeners;
  private Locale[] locales;
  private Integer timezoneOffset;

  public AppImpl() {
    remoteObject = RemoteObjectFactory.getInstance().createServiceObject( TYPE );
    remoteObject.setHandler( new AppOperationHandler() );
    eventListeners = new HashMap<EventType, List<AppListener>>();
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
  public void addListener( EventType type, AppListener listener ) {
    List<AppListener> listeners = eventListeners.get( type );
    if( listeners == null ) {
      listeners = new ArrayList<AppListener>();
      eventListeners.put( type, listeners );
      remoteObject.listen( type.getName(), true );
    }
    listeners.add( listener );
  }

  @Override
  public void removeListener( EventType type, AppListener listener ) {
    List<AppListener> listeners = eventListeners.get( type );
    listeners.remove( listener );
    if( listeners.isEmpty() ) {
      eventListeners.remove( type );
      remoteObject.listen( type.getName(), false );
    }
  }
  private class AppOperationHandler extends RemoteOperationHandler {

    @Override
    public void handleNotify( String event, Map<String, Object> properties ) {
      AppEvent appEvent = new AppEvent( EventType.fromName( event ), properties );
      notifyListeners( appEvent );
    }

    private void notifyListeners( AppEvent appEvent ) {
      List<AppListener> listeners = eventListeners.get( appEvent.getType() );
      if( listeners != null ) {
        for( AppListener appListener : listeners ) {
          appListener.handleEvent( appEvent );
        }
      }
    }

    @Override
    public void handleSet( Map<String, Object> properties ) {
      if( properties.containsKey( TIMEZONE_OFFSET ) ) {
        timezoneOffset = ( Integer )properties.get( TIMEZONE_OFFSET );
      }
    }
  }

  @Override
  public int getTimezoneOffset() {
    if( timezoneOffset == null ) {
      throw new IllegalStateException( "timezoneOffset is not set" );
    }
    return timezoneOffset.intValue();
  }

  @Override
  public Locale getLocale() {
    return locales == null
                          ? null
                          : locales[ 0 ];
  }

  @Override
  public Locale[] getLocales() {
    return locales == null
                          ? new Locale[ 0 ]
                          : locales.clone();
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }
}

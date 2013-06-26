/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Constants.EVENT_BACK_NAVIGATION;
import static com.eclipsesource.tabris.internal.Constants.TYPE_APP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.app.App;
import com.eclipsesource.tabris.app.AppEvent;
import com.eclipsesource.tabris.app.AppListener;
import com.eclipsesource.tabris.app.BackNavigationListener;
import com.eclipsesource.tabris.app.EventType;

@SuppressWarnings("restriction")
public class AppImpl extends AbstractOperationHandler implements App {

  private final RemoteObject remoteObject;
  private final Map<EventType, List<AppListener>> eventListeners;
  private final List<BackNavigationListener> backNavigationListeners;

  public AppImpl() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( TYPE_APP );
    remoteObject.setHandler( this );
    eventListeners = new HashMap<EventType, List<AppListener>>();
    backNavigationListeners = new ArrayList<BackNavigationListener>();
  }

  @Override
  public void addEventListener( EventType type, AppListener listener ) {
    List<AppListener> listeners = eventListeners.get( type );
    if( listeners == null ) {
      listeners = new ArrayList<AppListener>();
      eventListeners.put( type, listeners );
      remoteObject.listen( type.getName(), true );
    }
    listeners.add( listener );
  }

  @Override
  public void removeEventListener( EventType type, AppListener listener ) {
    List<AppListener> listeners = eventListeners.get( type );
    listeners.remove( listener );
    if( listeners.isEmpty() ) {
      eventListeners.remove( type );
      remoteObject.listen( type.getName(), false );
    }
  }

  @Override
  public void addBackNavigationListener( BackNavigationListener listener ) {
    if( backNavigationListeners.isEmpty() ) {
      remoteObject.listen( EVENT_BACK_NAVIGATION, true );
    }
    backNavigationListeners.add( listener );
  }

  @Override
  public void removeBackNavigationListener( BackNavigationListener listener ) {
    backNavigationListeners.remove( listener );
    if( backNavigationListeners.isEmpty() ) {
      remoteObject.listen( EVENT_BACK_NAVIGATION, false );
    }
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( event.equals( EVENT_BACK_NAVIGATION ) ) {
      notifyBackNavigationListeners();
    } else {
      AppEvent appEvent = new AppEvent( EventType.fromName( event ), properties );
      notifyEventListeners( appEvent );
    }
  }

  private void notifyBackNavigationListeners() {
    List<BackNavigationListener> listeners = new ArrayList<BackNavigationListener>( backNavigationListeners );
    for( BackNavigationListener listener : listeners ) {
      listener.navigatedBack();
    }
  }

  private void notifyEventListeners( AppEvent appEvent ) {
    List<AppListener> listeners = eventListeners.get( appEvent.getType() );
    if( listeners != null ) {
      List<AppListener> listenersCopy = new ArrayList<AppListener>( listeners );
      for( AppListener appListener : listenersCopy ) {
        appListener.handleEvent( appEvent );
      }
    }
  }

  @Override
  public void activateInactivityLock( int inactivityTime ) {
    when( inactivityTime < 0 ).throwIllegalArgument( "inactivityTime must be >= 0 but was " + inactivityTime );
    JsonObject parameters = new JsonObject();
    parameters.add( "inactivityTime", inactivityTime );
    remoteObject.call( "activateInactivityLock", parameters );
  }

  @Override
  public void deactivateInactivityLock() {
    remoteObject.call( "deactivateInactivityLock", null );
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }
}

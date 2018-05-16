/*******************************************************************************
 * Copyright (c) 2014, 2018 EclipseSource and others.
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
import static com.eclipsesource.tabris.internal.Constants.EVENT_INSTANCE_ID_CHANGED;
import static com.eclipsesource.tabris.internal.Constants.EVENT_MESSAGE_RECEIVED;
import static com.eclipsesource.tabris.internal.Constants.EVENT_REGISTERED;
import static com.eclipsesource.tabris.internal.Constants.EVENT_TOKEN_CHANGED;
import static com.eclipsesource.tabris.internal.Constants.METHOD_RESET_INSTANCE_ID;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_INSTANCE_ID;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_NOTIFICATION_ENABLED;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TOKEN;
import static com.eclipsesource.tabris.internal.Constants.TYPE_CLOUD_PUSH;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.push.CloudPush;
import com.eclipsesource.tabris.push.CloudPushListener;


@SuppressWarnings("restriction")
public class CloudPushImpl extends AbstractOperationHandler implements CloudPush {

  private final RemoteObject remoteObject;
  private final List<CloudPushListener> listeners;
  private String message;

  public CloudPushImpl() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( TYPE_CLOUD_PUSH );
    remoteObject.setHandler( this );
    listeners = new ArrayList<CloudPushListener>();
  }

  @Override
  public void addListener( CloudPushListener listener ) {
    whenNull( listener ).throwIllegalArgument( "CloudPushListener must not be null" );
    if( listeners.isEmpty() ) {
      remoteObject.set( PROPERTY_NOTIFICATION_ENABLED, true );
    }
    listeners.add( listener );
  }

  @Override
  public void removeListener( CloudPushListener listener ) {
    whenNull( listener ).throwIllegalArgument( "CloudPushListener must not be null" );
    listeners.remove( listener );
    if( listeners.isEmpty() ) {
      remoteObject.set( PROPERTY_NOTIFICATION_ENABLED, false );
    }
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public void resetInstanceId() {
    remoteObject.call( METHOD_RESET_INSTANCE_ID, null );
  }

  @Override
  public void handleSet( JsonObject properties ) {
    if( properties.get( PROPERTY_MESSAGE ) != null ) {
      message = properties.get( PROPERTY_MESSAGE ).asString();
    }
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( event.equals( EVENT_REGISTERED ) ) {
      dispatchRegistered( properties );
    } else if( event.equals( EVENT_MESSAGE_RECEIVED ) ) {
      dispatchMessageReceived( properties );
    } else if( event.equals( EVENT_TOKEN_CHANGED ) ) {
      dispatchTokenChanged( properties );
    } else if( event.equals( EVENT_INSTANCE_ID_CHANGED ) ) {
      dispatchInstanceIdChanged( properties );
    } else {
      super.handleNotify( event, properties );
    }
  }

  private void dispatchRegistered( JsonObject properties ) {
    String token = properties.get( PROPERTY_TOKEN ).asString();
    String instanceId = properties.get( PROPERTY_INSTANCE_ID ).asString();
    for( CloudPushListener listener : listeners ) {
      listener.registered( instanceId, token );
    }
  }

  private void dispatchTokenChanged( JsonObject properties ) {
    String token = properties.get( PROPERTY_TOKEN ).asString();
    for( CloudPushListener listener : listeners ) {
      listener.tokenChanged( token );
    }
  }

  private void dispatchInstanceIdChanged( JsonObject properties ) {
    String instanceId = properties.get( PROPERTY_INSTANCE_ID ).asString();
    for( CloudPushListener listener : listeners ) {
      listener.instanceIdChanged( instanceId );
    }
  }

  private void dispatchMessageReceived( JsonObject properties ) {
    for( CloudPushListener listener : listeners ) {
      listener.messageReceived();
    }
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

}
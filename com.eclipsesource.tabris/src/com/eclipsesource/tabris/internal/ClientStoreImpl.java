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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.METHOD_ADD;
import static com.eclipsesource.tabris.internal.Constants.METHOD_CLEAR;
import static com.eclipsesource.tabris.internal.Constants.METHOD_REMOVE;
import static com.eclipsesource.tabris.internal.Constants.METHOD_SYNCHRONIZE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_KEY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_KEYS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_VALUE;
import static com.eclipsesource.tabris.internal.Constants.TYPE_CLIENT_STORE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.ClientStore;


@SuppressWarnings("restriction")
public class ClientStoreImpl extends AbstractOperationHandler implements ClientStore {

  private final RemoteObject serviceObject;
  private final Map<String, String> store;

  public ClientStoreImpl() {
    ConnectionImpl connection = ( ConnectionImpl )RWT.getUISession().getConnection();
    serviceObject = connection.createServiceObject( TYPE_CLIENT_STORE );
    serviceObject.setHandler( this );
    store = new HashMap<String, String>();
  }

  @Override
  public void add( String key, String value ) {
    whenNull( key ).thenIllegalArgument( "Key must not be null" );
    when( key.isEmpty() ).thenIllegalArgument( "Key must not be empty" );
    whenNull( value ).thenIllegalArgument( "Value must not be null" );
    store.put( key, value );
    sendAdd( key, value );
  }

  private void sendAdd( String key, String value ) {
    JsonObject properties = new JsonObject();
    properties.add( PROPERTY_KEY, key );
    properties.add( PROPERTY_VALUE, value );
    serviceObject.call( METHOD_ADD, properties );
  }

  @Override
  public String get( String key ) {
    return store.get( key );
  }

  @Override
  public void remove( String... keys ) {
    for( String key : keys ) {
      store.remove( key );
    }
    sendRemoveKeys( keys );
  }

  private void sendRemoveKeys( String[] keys ) {
    if( keys.length > 0 ) {
      JsonObject properties = new JsonObject();
      properties.add( PROPERTY_KEYS, createJsonArray( keys ) );
      serviceObject.call( METHOD_REMOVE, properties );
    }
  }

  private JsonArray createJsonArray( String[] keys ) {
    JsonArray result = new JsonArray();
    for( String key : keys ) {
      result.add( key );
    }
    return result;
  }

  @Override
  public void clear() {
    store.clear();
    serviceObject.call( METHOD_CLEAR, null );
  }

  @Override
  public void handleCall( String method, JsonObject parameters ) {
    if( method.equals( METHOD_SYNCHRONIZE ) ) {
      List<String> names = parameters.names();
      for( String name : names ) {
        store.put( name, parameters.get( name ).asString() );
      }
    }
  }

}

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

import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNull;
import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNullAndNotEmpty;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.ClientStore;


@SuppressWarnings("restriction")
public class ClientStoreImpl extends AbstractOperationHandler implements ClientStore {

  private static final String EVENT_SYNCHRONIZE_STORE = "SynchronizeStore";
  private static final String METHOD_CLEAR = "clear";
  private static final String METHOD_REMOVE = "remove";
  private static final String METHOD_ADD = "add";
  private static final String PROPERTY_KEYS = "keys";
  private static final String PROPERTY_VALUE = "value";
  private static final String PROPERTY_KEY = "key";

  private final RemoteObject serviceObject;
  private final Map<String, String> store;

  public ClientStoreImpl() {
    ConnectionImpl connection = ( ConnectionImpl )RWT.getUISession().getConnection();
    serviceObject = connection.createServiceObject( "tabris.ClientStore" );
    serviceObject.setHandler( this );
    store = new HashMap<String, String>();
  }

  @Override
  public void add( String key, String value ) {
    argumentNotNullAndNotEmpty( key, "Key" );
    argumentNotNull( value, "Value" );
    store.put( key, value );
    sendAdd( key, value );
  }

  private void sendAdd( String key, String value ) {
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( PROPERTY_KEY, key );
    properties.put( PROPERTY_VALUE, value );
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
      Map<String, Object> properties = new HashMap<String, Object>();
      properties.put( PROPERTY_KEYS, keys );
      serviceObject.call( METHOD_REMOVE, properties );
    }
  }

  @Override
  public void clear() {
    store.clear();
    serviceObject.call( METHOD_CLEAR, null );
  }

  @Override
  public void handleNotify( String event, Map<String, Object> properties ) {
    if( event.equals( EVENT_SYNCHRONIZE_STORE ) ) {
      for( Entry<String, Object> entry : properties.entrySet() ) {
        store.put( entry.getKey(), ( String )entry.getValue() );
      }
    }
  }
}

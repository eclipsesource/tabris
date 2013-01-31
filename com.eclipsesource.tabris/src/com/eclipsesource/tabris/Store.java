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
package com.eclipsesource.tabris;

import java.util.HashMap;
import java.util.Map;


/**
 * @since 0.11
 */
public class Store {

  private final Map<String, Object> store;

  public Store() {
    this.store = new HashMap<String, Object>();
  }

  public Store( Store store ) {
    this();
    if( store != null ) {
      this.store.putAll( store.getAll() );
    }
  }

  public void add( String key, Object value ) {
    store.put( key, value );
  }

  @SuppressWarnings("unchecked")
  public <T> T get( String key, Class<T> type ) {
    T result = null;
    Object value = store.get( key );
    if( value != null ) {
      if( type.isAssignableFrom( value.getClass() ) ) {
        result = ( T )value;
      } else {
        throw new IllegalArgumentException( "Value with key " + key + " is not of type " + type.getName() );
      }
    }
    return result;
  }

  Map<String, Object> getAll() {
    return new HashMap<String, Object>( store );
  }

  public void addStore( Store store ) {
    this.store.putAll( store.getAll() );
  }
}

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

import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.widgets.swipe.Swipe;


/**
 * <p>
 * A Store object can be used to store session specific data. You should see it as a convenience abstraction of a Map.
 * In Tabris some context objects exist like in {@link Swipe} or {@link Page}. These context object are using such a
 * store to enable a loose communication between the components.
 * </p>
 *
 * @since 0.11
 */
public class Store {

  private final Map<String, Object> store;

  public Store() {
    this.store = new HashMap<String, Object>();
  }

  /**
   * <p>
   * Takes a store and copies all existing values from this store into the newly created one.
   * </p>
   */
  public Store( Store store ) {
    this();
    if( store != null ) {
      this.store.putAll( store.getAll() );
    }
  }

  /**
   * <p>
   * Adds a value to a store. Already existing values will be overridden.
   * </p>
   * @param key key to identify the stored value. Must not be empty or <code>null</code>.
   * @param value the value to be stored. Must not be <code>null</code>.
   */
  public void add( String key, Object value ) {
    store.put( key, value );
  }

  /**
   * <p>
   * Adds all Values form the passed in store to this store.
   * </p>
   */
  public void addStore( Store store ) {
    this.store.putAll( store.getAll() );
  }

  /**
   * <p>
   * Gets a value from this store, already casted to the passed in type.
   * </p>
   *
   * @throws IllegalArgumentException when the stored value can not be casted to the passed in type.
   */
  @SuppressWarnings("unchecked")
  public <T> T get( String key, Class<T> type ) throws IllegalArgumentException {
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
}

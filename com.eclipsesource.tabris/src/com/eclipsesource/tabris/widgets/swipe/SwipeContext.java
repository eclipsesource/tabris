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
package com.eclipsesource.tabris.widgets.swipe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * A <code>SwipeContet</p> is a shared store that exists only once per {@link Swipe} object. The context will be shared
 * during all swiping events.
 * </p>
 *
 * @see SwipeListener
 *
 * @since 0.10
 */
public class SwipeContext implements Serializable {

  private final Map<String, Object> store;

  public SwipeContext() {
    this.store = new HashMap<String, Object>();
  }

  /**
   * <p>
   * Adds a value to this context. Already existing values will be overridden.
   * </p>
   * @param key key to identify the stored value. Must not be empty or <code>null</code>.
   * @param value the value to be stored. Must not be <code>null</code>.
   * @since 1.0
   */
  public void set( String key, Object value ) {
    store.put( key, value );
  }

  /**
   * <p>
   * Gets a value from this context, already casted to the passed in type.
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
}

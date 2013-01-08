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

import java.util.HashMap;
import java.util.Map;


/**
 * @since 0.10
 */
public class SwipeContext {
  
  private final Map<String, Object> store;

  public SwipeContext() {
    store = new HashMap<String, Object>();
  }
  
  public void add( String key, Object value ) {
    store.put( key, value );
  }
  
  @SuppressWarnings("unchecked")
  public <T> T get( String key, Class<T> type ) {
    T result = null;
    Object value = store.get( key );
    if( value != null ) {
      if( value.getClass().isAssignableFrom( type ) ) {
        result = ( T )value;
      } else {
        throw new IllegalArgumentException( "Value with key " + key + " is not of type " + type.getName() );
      }
    }
    return result;
  }
}

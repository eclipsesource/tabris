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

import com.eclipsesource.tabris.Store;


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
public class SwipeContext {

  private final Store store;

  public SwipeContext() {
    store = new Store();
  }

  public void add( String key, Object value ) {
    store.add( key, value );
  }

  public <T> T get( String key, Class<T> type ) {
    return store.get( key, type );
  }
}

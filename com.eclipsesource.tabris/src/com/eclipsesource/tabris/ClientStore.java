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

import org.eclipse.rap.rwt.client.service.ClientService;


/**
 * <p>
 * A <code>ClientStore</p> can be used to store data on the client device. It's comparable with cookies for the Browser.
 * </p>
 * <p>
 * NOTE: Please don't use the ClientStore to store big data. When a session will be created form a device all
 *       previously stored data on this device will be sent to the server within the first request. Instead it's a
 *       better practice to save small data to identify the client like an ID to restore the session from a
 *       persistent store on the server side.
 * </p>
 *
 * @noimplement
 * @since 0.11
 */
public interface ClientStore extends ClientService {

  /**
   * <p>
   * Adds a value to this store and sends it to the accessing client device. Already existing values will be overridden.
   * </p>
   * @param key key to identify the stored value. Must not be empty or <code>null</code>.
   * @param value the value to be stored on the client device. Must not be <code>null</code>.
   */
  void add( String key, String value );

  /**
   * <p>
   * Gets a values out of the client store. Calling the get method does not mean that the client will be accessed. All
   * data stored on the client will be transfered in the first request to the sever. So this lookup will be
   * fast as hell.
   * </p>
   * @param key key to identify the stored value. Must not be empty or <code>null</code>.
   */
  String get( String key );

  /**
   * <p>
   * Removes a entry from the ClientStore. The removed value will also be removed on the client device.
   * </p>
   * @param key keys to identify the stored values. Must not be empty or <code>null</code>.
   */
  void remove( String... keys );

  /**
   * <p>
   * Clears the whole ClientStore on the server and client side.
   * </p>
   */
  void clear();
}

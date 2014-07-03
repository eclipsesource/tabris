/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.push;

import org.eclipse.rap.rwt.client.service.ClientService;


/**
 * <p>
 * {@link CloudPush} is a {@link ClientService} implementation and can be obtained by calling
 * <code>RWT.getClient().getService( CloudPush.class );</code>. A {@link CloudPush} instance can be used to register
 * a client to receive push notifications by simply adding a {@link CloudPushListener}. This instructs the client to
 * register itself once at the push service that is common for the client platform.
 * </p>
 * <p>
 * After a client is registered to receive notifications the {@link CloudPush} service notifies all attached
 * {@link CloudPushListener} if a registration, message or error was received.
 * </p>
 * <p>
 * After a message was pushed to the client it forwards the message to the server. This message can be obtained by
 * calling {@link CloudPush#getMessage()}.
 * </p>
 *
 * @see CloudPushListener
 *
 * @since 1.4
 *
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface CloudPush extends ClientService {

  /**
   * <p>
   * Adds a {@link CloudPushListener} for the current session. If the client needs to register for push notifications
   * this will instruct the client to register.
   * </p>
   */
  void addListener( CloudPushListener listener );

  /**
   * <p>
   * Removes a {@link CloudPushListener}.
   * </p>
   */
  void removeListener( CloudPushListener listener );

  /**
   * <p>
   * Returns the last received push notification message sent to the client.
   * </p>
   */
  String getMessage();

}

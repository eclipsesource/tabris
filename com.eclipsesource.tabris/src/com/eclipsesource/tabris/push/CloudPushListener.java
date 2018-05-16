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
package com.eclipsesource.tabris.push;


/**
 * <p>
 * A {@link CloudPushListener} is added to a {@link CloudPush} service. The {@link CloudPushListener} can be used to
 * process registrations, messages and errors a client received.
 * </p>
 *
 * @since 1.4
 */
public interface CloudPushListener {

  /**
   * <p>
   * Will be called after a client has registered itself for push notifications. The passed token identifies the client
   * and can be used to send push notifications using the platform specific push services.
   * </p>
   *
   *  @param instanceId a stable identifier that uniquely identifies the app installation.
   *  @param token the token a client received after a successful registration.
   */
  void registered( String instanceId, String token );

  /**
   * <p>
   * Will be called when the current registration token has changed.
   * </p>
   */
  void tokenChanged( String token );

  /**
   * <p>
   * The event is fired asynchronously when the {@link CloudPush#resetInstanceId()} method is invoked.
   * </p>
   */
  void instanceIdChanged( String instanceId );

  /**
   * <p>
   * Will be called when a client has received a push notification. The forwarded message can be obtained by calling
   * {@link CloudPush#getMessage()}.
   * </p>
   */
  void messageReceived();

}
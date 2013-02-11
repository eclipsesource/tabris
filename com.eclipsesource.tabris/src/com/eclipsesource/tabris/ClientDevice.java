/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris;

import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.client.service.ClientInfo;


/**
 * <p>
 * The <code>ClientDevice</code> provides service methods to gain information regarding the requesting client device
 * like the platform, the orientation, the locale and so on. An instance of ClientDevice can be accessed using
 * RWT.getClient().getService( ClientDevice.class ).
 * <p>
 *
 * @see Platform
 * @see Client
 * @since 0.11
 */
public interface ClientDevice extends ClientInfo {

  /**
   * <p>
   * Enumeration to make the identification of a requesting client easier.
   * </p>
   *
   * @since 0.11
   */
  public enum Platform {
    IOS, ANDROID, WEB
  }

  /**
   * @since 0.11
   */
  Platform getPlatform();

  /**
   * @since 0.11
   */
  public enum Orientation {
    PORTRAIT, LANDSCAPE
  }

  /**
   * @since 0.11
   */
  Orientation getOrientation();

  /**
   * @since 0.11
   */
  public enum Capability {
    LOCATION, MESSAGE, PHONE, CAMERA, MAPS
  }

  /**
   * @since 0.11
   */
  boolean hasCapability( Capability capability );

  /**
   * @since 0.11
   */
  public enum ConnectionType {
    WIFI, CELULAR
  }

  /**
   * @since 0.11
   */
  ConnectionType getConnectionType();

}

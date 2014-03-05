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
package com.eclipsesource.tabris.device;

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
 * @see Orientation
 * @see Capability
 * @see ConnectionType
 * @see Client
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * @since 0.11
 */
public interface ClientDevice extends ClientInfo {

  /**
   * <p>
   * Returns the vendor name of the client device e.g. Apple, Samsung and so on.
   * </p>
   *
   * @since 1.3
   */
  String getVendor();

  /**
   * <p>
   * Returns the model name of the client device e.g. iPhone, Galaxy S3 and so on.
   * </p>
   *
   * @since 1.3
   */
  String getModel();

  /**
   * <p>
   * Returns the version of the client's operating system.
   * </p>
   *
   * @since 1.3
   */
  String getOSVersion();

  /**
   * <p>
   * Returns the scale factor of the device's display.
   * </p>
   *
   * @since 1.3
   */
  float getScaleFactor();

  /**
   * <p>
   * Enumeration to make the identification of a requesting client easier.
   * </p>
   *
   * @since 0.11
   */
  public static enum Platform {
    IOS,
    ANDROID,
    WEB,

    /**
     * @since 1.3
     */
    SWT
  }

  /**
   * <p>
   * Returns the platform of the accessing client.
   * </p>
   *
   * @since 0.11
   */
  Platform getPlatform();

  /**
   * <p>
   * Enumeration to distinguish the display orientation.
   * </p>
   *
   * @since 0.11
   */
  public static enum Orientation {
    PORTRAIT, LANDSCAPE, SQUARE
  }

  /**
   * <p>
   * Returns the orientation of the accessing client. Can change during runtime.
   * </p>
   *
   * @since 0.11
   */
  Orientation getOrientation();

  /**
   * <p>
   * Each accessing device has different capabilities like the capability to access the device location or make phone
   * calls. This enumeration maps those devices.
   * </p>
   *
   * @since 0.11
   */
  public static enum Capability {
    LOCATION, MESSAGE, PHONE, CAMERA, MAPS
  }

  /**
   * <p>
   * Returns a boolean value to indicate if the accessing device has the defined capability.
   * </p>
   *
   * @since 0.11
   */
  boolean hasCapability( Capability capability );

  /**
   * <p>
   * An accessing device can use different kinds of connection types like wifi or 3G. Here two different kinds are
   * defined. Wifi should be self explaining. Cellular means basically any Wide Area Network connection like 3G, GSM
   * and so on.
   * <p>
   *
   * @since 0.11
   */
  public static enum ConnectionType {
    WIFI, CELLULAR
  }

  /**
   * <p>
   * Returns the use connection type of the accessing device. Can change durign runtime.
   * </p>
   *
   * @since 0.11
   */
  ConnectionType getConnectionType();

  /**
   * <p>
   * Attaches a listener that will be notified when a property has changed.
   * </p>
   *
   * @param listener the listener to be attached. Must not be <code>null</code>.
   *
   * @since 1.0
   */
  void addClientDeviceListener( ClientDeviceListener listener );

  /**
   * <p>
   * Removes a previously attached listener.
   * </p>
   *
   * @param listener the listener to be removed. Must not be <code>null</code>.
   *
   * @since 1.0
   */
  void removeClientDeviceListener( ClientDeviceListener listener );

}

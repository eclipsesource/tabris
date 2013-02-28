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
package com.eclipsesource.tabris.device;

import com.eclipsesource.tabris.device.ClientDevice.ConnectionType;
import com.eclipsesource.tabris.device.ClientDevice.Orientation;


/**
 * <a>
 * This listener can be attached to a {@link ClientDevice} to get notifications about changes regarding the clien
 * device, e.g. when the orientation has changed.
 * </p>
 * <p>
 * There is also an empty adapter available, see {@link ClientDeviceAdapter}.
 * </p>
 *
 * @see ClientDevice
 * @see ClientDeviceAdapter
 *
 * @since 1.0
 */
public interface ClientDeviceListener {

  /**
   * <p>
   * Will be called when the orientation of a device has changed.
   * </p>
   *
   * @param newOrientation the new orientation of the device.
   */
  void orientationChange( Orientation newOrientation );

  /**
   * <p>
   * Will be called when the connection type of a device has changed.
   * </p>
   *
   * @param newConnectionType the new connection type of the device.
   */
  void connectionTypeChanged( ConnectionType newConnectionType );
}

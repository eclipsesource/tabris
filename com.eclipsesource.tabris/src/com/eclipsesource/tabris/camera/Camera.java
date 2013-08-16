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
package com.eclipsesource.tabris.camera;

import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.client.service.ClientService;


/**
 * <p>
 * The <code>Camera</code> component can be used to take and receive pictures from a mobile client's camera or photo
 * album. The taken picture will be sent to the server side and a callback will be called. See
 * <code>CameraCallback</code>. An instance of {@link Camera} can be accessed using
 * RWT.getClient().getService( Camera.class ).
 * </p>
 *
 * @see CameraListener
 * @see CameraOptions
 * @see Client
 *
 * @noimplement
 * @since 0.8
 */
public interface Camera extends ClientService {

  /**
   * <p>
   * Instructs the client to open the camera or photo album. The added {@link CameraListener}s will be called when the
   * user has taken/selected a picture or in the case of an error.
   * </p>
   *
   *  @param options The options that should be used as the configuration for taking a picture. Must not
   *                 be <code>null</code>.
   *
   *  @see CameraListener
   */
  void takePicture( CameraOptions options );

  /**
   * <p>
   * Adds a {@link CameraListener} to get notified about image events.
   * </p>
   */
  void addCameraListener( CameraListener listener );

  /**
   * <p>
   * Removes a {@link CameraListener}.
   * </p>
   */
  void removeCameraListener( CameraListener listener );

}

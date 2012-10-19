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

import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.rap.rwt.internal.protocol.ClientObjectAdapter;
import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;

import com.eclipsesource.tabris.camera.internal.CameraAdapter;
import com.eclipsesource.tabris.camera.internal.CameraSynchronizer;


/**
 * <p>
 * The <code>Camera</code> component can be used to take and receive pictures from a mobile client's camera or photo 
 * album. The taken picture will be sent to the server side and a callback will be called. See 
 * <code>CameraCallback</code>. 
 * </p>
 * <p>
 * To configure the client's camera an options object needs to be passed as a constructor argument.
 * </p>
 * 
 * @see CameraCallback
 * @see CameraOptions
 * @since 0.8
 */
@SuppressWarnings("restriction")
public class Camera implements Adaptable {
  
  private final ClientObjectAdapter clientObjectAdapter;
  private final CameraAdapter cameraAdapter;

  /**
   * <p>
   * Creates a new camera object with the configuration passed as argument. 
   * </p>
   * 
   * @param options the configuration for the camera. Must not be <code>null</code>.
   * @see CameraOptions
   */
  public Camera( CameraOptions options ) {
    checkOptions( options );
    clientObjectAdapter = new ClientObjectAdapter( "c" );
    new CameraSynchronizer( this );
    cameraAdapter = new CameraAdapter();
    cameraAdapter.setOptions( options );
  }
  
  private void checkOptions( CameraOptions options ) {
    if( options == null ) {
      throw new IllegalArgumentException( "Camera Options must not be null, you can use CameraOptions.NONE." );
    }
  }

  /**
   * <p>
   * Instructs the client to open the camera or photo album. The <code>CameraCallback</code> will be called when the 
   * user has taken/selected a picture or in the case of an error.
   * </p>
   * 
   *  @param callback The callback to call. Must not be <code>null</code>.
   *  @see CameraCallback
   */
  public void takePicture( CameraCallback callback ) {
    cameraAdapter.setCallback( callback );
    cameraAdapter.open();
  }
  
  /**
   * <p>
   * Destroys the camera object. Behaves the same like other SWT Widgets.
   * </p>
   */
  public void dispose() {
    cameraAdapter.dispose();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    T result = null;
    if( adapter == CameraAdapter.class ) {
      result = ( T )cameraAdapter;
    } else if( adapter == IClientObjectAdapter.class ) {
      result = ( T )clientObjectAdapter;
    }
    return result;
  }
  
}

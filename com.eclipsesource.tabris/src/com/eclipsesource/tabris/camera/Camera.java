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

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.internal.Base64;
import com.eclipsesource.tabris.internal.Preconditions;


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
public class Camera {

  private static final String TYPE = "tabris.Camera";
  private static final String OPEN_METHOD = "open";
  private static final String PROPERTY_RESOLUTION = "resolution";
  private static final String PROPERTY_SAVETOALBUM = "saveToAlbum";
  private static final String IMAGE_SELECTION_EVENT = "ImageSelection";
  private static final String IMAGE_SELECTION_ERROR_EVENT = "ImageSelectionError";
  private static final String PROPERTY_IMAGE = "image";

  private final RemoteObject remoteObject;
  private CameraCallback callback;

  private final OperationHandler cameraHandler = new AbstractOperationHandler() {
    @Override
    public void handleNotify( String event, Map<String,Object> properties ) {
      if( IMAGE_SELECTION_EVENT.equals( event ) ) {
        callback.onSuccess( decodeImage( ( String )properties.get( PROPERTY_IMAGE ) ) );
      } else if( IMAGE_SELECTION_ERROR_EVENT.equals( event ) ) {
        callback.onError();
      }
    }
  };

  /**
   * <p>
   * Creates a new camera object with the configuration passed as argument.
   * </p>
   *
   * @param options the configuration for the camera. Must not be <code>null</code>.
   * @see CameraOptions
   */
  public Camera( CameraOptions options ) {
    Preconditions.checkArgumentNotNull( options, "Camera Options" );
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( TYPE );
    remoteObject.setHandler( cameraHandler );
    setInitialValues( options );
  }

  private Image decodeImage( String encodedImage ) {
    byte[] bytes = Base64.decode( encodedImage );
    ByteArrayInputStream stream = new ByteArrayInputStream( bytes );
    return new Image( Display.getCurrent(), stream );
  }

  private void setInitialValues( CameraOptions options ) {
    setResolution( options );
    setSaveToAlbum( options );
  }

  private void setResolution( CameraOptions options ) {
    Point resolution = options.getResolution();
    if( resolution != null ) {
      remoteObject.set( PROPERTY_RESOLUTION, new int[] { resolution.x, resolution.y } );
    }
  }

  private void setSaveToAlbum( CameraOptions options ) {
    if( options.savesToAlbum() ) {
      remoteObject.set( PROPERTY_SAVETOALBUM, true );
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
    checkArgumentNotNull( callback, "Callback" );
    this.callback = callback;
    remoteObject.call( OPEN_METHOD, null );
  }

  /**
   * <p>
   * Destroys the camera object. Behaves the same like other SWT Widgets.
   * </p>
   */
  public void dispose() {
    remoteObject.destroy();
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

}

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
package com.eclipsesource.tabris.internal.camera;

import static org.eclipse.rap.rwt.internal.protocol.ProtocolUtil.readEventPropertyValueAsString;
import static org.eclipse.rap.rwt.internal.protocol.ProtocolUtil.wasCallSend;
import static org.eclipse.rap.rwt.internal.protocol.ProtocolUtil.wasEventSent;

import java.io.ByteArrayInputStream;

import org.eclipse.rap.rwt.internal.protocol.IClientObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.camera.CameraOptions.SourceType;
import com.eclipsesource.tabris.internal.AbstractObjectSynchronizer;


@SuppressWarnings("restriction")
public class CameraSynchronizer extends AbstractObjectSynchronizer<Camera> {
  
  static final String TYPE = "tabris.Camera";
  // write properties
  static final String OPEN_METHOD = "open";
  static final String PROPERTY_SOURCETYPE = "sourceType";
  static final String PROPERTY_RESOLUTION = "resolution";
  static final String PROPERTY_SAVETOALBUM = "saveToAlbum";
  // read properties
  static final String IMAGE_SELECTION_EVENT = "ImageSelection";
  static final String IMAGE_SELECTION_ERROR_EVENT = "ImageSelectionError";
  static final String PROPERTY_IMAGE = "image";
  static final String CLOSE_METHOD = "close";
  private static final String INTERNAL_ERROR = "ERROR";

  public CameraSynchronizer( Camera camera ) {
    super( camera );
  }

  @Override
  protected void renderInitialization( IClientObject clientObject, Camera camera ) {
    clientObject.create( TYPE );
    CameraAdapter adapter = camera.getAdapter( CameraAdapter.class );
    setResolution( clientObject, adapter );
    setSourceType( clientObject, adapter );
    setSaveToAlbum( clientObject, adapter );
  }

  private void setResolution( IClientObject clientObject, CameraAdapter adapter ) {
    Point resolution = adapter.getOptions().getResolution();
    if( resolution != null ) {
      clientObject.set( PROPERTY_RESOLUTION, new int[] { resolution.x, resolution.y } );
    }
  }

  private void setSourceType( IClientObject clientObject, CameraAdapter adapter ) {
    SourceType sourceType = adapter.getOptions().getSourceType();
    if( sourceType != null ) {
      clientObject.set( PROPERTY_SOURCETYPE, sourceType.toString().toLowerCase() );
    }
  }
  
  private void setSaveToAlbum( IClientObject clientObject, CameraAdapter adapter ) {
    if( adapter.getOptions().savesToAlbum() ) {
      clientObject.set( PROPERTY_SAVETOALBUM, adapter.getOptions().savesToAlbum() );
    }
  }

  @Override
  protected void readData( Camera camera ) {
    CameraAdapter adapter = camera.getAdapter( CameraAdapter.class );
    handleImageSelectionEvent( adapter );
    handleImageSelectionErrorEvent( adapter );
    handleClose( adapter );
  }

  private void handleImageSelectionEvent( CameraAdapter adapter ) {
    if( wasEventSent( getId(), IMAGE_SELECTION_EVENT ) ) {
      String image = readEventPropertyValueAsString( getId(), IMAGE_SELECTION_EVENT, PROPERTY_IMAGE );
      adapter.setEncodedImage( image );
    }
  }

  private void handleImageSelectionErrorEvent( CameraAdapter adapter ) {
    if( wasEventSent( getId(), IMAGE_SELECTION_ERROR_EVENT ) ) {
      adapter.setEncodedImage( INTERNAL_ERROR );
    }
  }

  private void handleClose( CameraAdapter adapter ) {
    if( wasCallSend( getId(), CLOSE_METHOD ) ) {
      adapter.close();
    }
  }

  @Override
  protected void processAction( Camera camera ) {
    CameraAdapter cameraAdapter = camera.getAdapter( CameraAdapter.class );
    if( cameraAdapter.getCallback() != null && cameraAdapter.getEncodedImage() != null ) {
      handleCameraResult( cameraAdapter, camera );
      reset( cameraAdapter );
    }
  }

  private void handleCameraResult( CameraAdapter cameraAdapter, Camera camera ) {
    if( cameraAdapter.getCallback() != null && cameraAdapter.getEncodedImage() != null ) {
      if( !cameraAdapter.getEncodedImage().equals( INTERNAL_ERROR ) ) {
        cameraAdapter.getCallback().onSuccess( decodeImage( cameraAdapter.getEncodedImage(), camera ) );
      } else {
        cameraAdapter.getCallback().onError();
      }
    }
  }

  private Image decodeImage( String encodedImage, Camera camera ) {
    byte[] bytes = Base64.decode( encodedImage );
    ByteArrayInputStream stream = new ByteArrayInputStream( bytes );
    return new Image( getDisplay(), stream );
  }

  private void reset( CameraAdapter cameraAdapter ) {
    cameraAdapter.setCallback( null );
    cameraAdapter.setEncodedImage( null );
  }

  @Override
  protected void renderChanges( Camera camera ) {
    CameraAdapter adapter = camera.getAdapter( CameraAdapter.class );
    if( adapter.isOpen() ) {
      getClientObject().call( OPEN_METHOD, null );
      adapter.close();
    }
    if( adapter.isDisposed() && !adapter.isDestroyed() ) {
      getClientObject().destroy();
      adapter.destroy();
    }
  }

  @Override
  protected void preserveValues( Camera camera ) {
    // no need to preserve any values
  }
}

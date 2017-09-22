/*******************************************************************************
 * Copyright (c) 2012, 2017 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/

package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.EVENT_IMAGE_SELECTION;
import static com.eclipsesource.tabris.internal.Constants.EVENT_IMAGE_SELECTION_CANCEL;
import static com.eclipsesource.tabris.internal.Constants.EVENT_IMAGE_SELECTION_ERROR;
import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_COMPRESSON_QUALITY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_RESOLUTION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SAVE_TO_ALBUM;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_UPLOAD_PATH;
import static com.eclipsesource.tabris.internal.Constants.TYPE_CAMERA;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.rap.fileupload.FileDetails;
import org.eclipse.rap.fileupload.FileUploadEvent;
import org.eclipse.rap.fileupload.FileUploadHandler;
import org.eclipse.rap.fileupload.FileUploadListener;
import org.eclipse.rap.fileupload.FileUploadReceiver;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.lifecycle.LifeCycleUtil;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.rap.rwt.service.UISessionEvent;
import org.eclipse.rap.rwt.service.UISessionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.camera.CameraListener;
import com.eclipsesource.tabris.camera.CameraOptions;


@SuppressWarnings("restriction")
public class CameraImpl extends AbstractOperationHandler implements Camera {

  private static final Pattern SERVICE_URL_PATTERN = Pattern.compile(".*/([^/.]*\\?.*)"); //$NON-NLS-1$

  private final RemoteObject remoteObject;
  private final List<CameraListener> cameraListeners;
  private final UISession uiSession;
  private final ServerPushSession serverPush;

  public CameraImpl() {
    uiSession = RWT.getUISession();
    remoteObject = ( ( ConnectionImpl )uiSession.getConnection() ).createServiceObject( TYPE_CAMERA );
    remoteObject.setHandler( this );
    cameraListeners = new ArrayList<CameraListener>();
    serverPush = new ServerPushSession();
    String uploadPath = registerFileUploadServiceHandler();
    remoteObject.set( PROPERTY_UPLOAD_PATH, stripContextPath( uploadPath ) );
  }

  @Override
  public void addCameraListener( CameraListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    cameraListeners.add( listener );
  }

  @Override
  public void removeCameraListener( CameraListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    cameraListeners.remove( listener );
  }

  @Override
  public void takePicture( CameraOptions options ) {
    whenNull( options ).throwIllegalArgument( "Options must not be null" );
    JsonObject properties = createProperties( options );
    remoteObject.call( METHOD_OPEN, properties );
  }

  private JsonObject createProperties( CameraOptions options ) {
    JsonObject properties = new JsonObject();
    addResolution( properties, options );
    addSaveToAlbum( properties, options );
    addCompressionQuality( properties, options );
    return properties;
  }

  private void addResolution( JsonObject properties, CameraOptions options ) {
    Point resolution = options.getResolution();
    if( resolution != null ) {
      JsonArray jsonArray = new JsonArray();
      jsonArray.add( resolution.x );
      jsonArray.add( resolution.y );
      properties.add( PROPERTY_RESOLUTION, jsonArray );
    }
  }

  private void addSaveToAlbum( JsonObject properties, CameraOptions options ) {
    if( options.savesToAlbum() ) {
      properties.add( PROPERTY_SAVE_TO_ALBUM, true );
    }
  }

  private void addCompressionQuality( JsonObject properties, CameraOptions options ) {
    properties.add( PROPERTY_COMPRESSON_QUALITY, options.getCompressionQuality() );
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( EVENT_IMAGE_SELECTION.equals( event ) ) {
      serverPush.start();
    } else if( EVENT_IMAGE_SELECTION_ERROR.equals( event ) || EVENT_IMAGE_SELECTION_CANCEL.equals( event ) ) {
      notifyListenersWithoutPicture();
    }
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

  private String registerFileUploadServiceHandler() {
    final Display display = LifeCycleUtil.getSessionDisplay( uiSession );
    final ImageUploadReceiver receiver = new ImageUploadReceiver( display );
    final FileUploadHandler uploadHandler = new FileUploadHandler( receiver );
    uploadHandler.addUploadListener( new FileUploadListener() {
      @Override
      public void uploadProgress( FileUploadEvent event ) {
      }
      @Override
      public void uploadFailed( FileUploadEvent event ) {
        handleUploadFailed( display, receiver );
      }
      @Override
      public void uploadFinished( FileUploadEvent event ) {
        handleUploadFinished( display, receiver );
      }
    } );
    uiSession.addUISessionListener( new UISessionListener() {
      @Override
      public void beforeDestroy( UISessionEvent event ) {
        uploadHandler.dispose();
      }
    } );
    return uploadHandler.getUploadUrl();
  }

  static String stripContextPath( String serviceHandlerUrl ) {
    Matcher matcher = SERVICE_URL_PATTERN.matcher( serviceHandlerUrl );
    if( matcher.matches() ) {
      return matcher.group( 1 );
    }
    return serviceHandlerUrl;
  }

  void handleUploadFailed( Display display, final ImageUploadReceiver receiver ) {
    if( display != null && !display.isDisposed() ) {
      display.asyncExec( new Runnable() {
        @Override
        public void run() {
          notifyListenersWithoutPicture();
          receiver.reset();
          serverPush.stop();
        }
      } );
    }
  }

  void handleUploadFinished( Display display, final ImageUploadReceiver receiver ) {
    if( display != null && !display.isDisposed() ) {
      display.asyncExec( new Runnable() {
        @Override
        public void run() {
          notifyListenersWithImage( receiver.getImage() );
          receiver.reset();
          serverPush.stop();
        }
      } );
    }
  }

  private void notifyListenersWithImage( Image image ) {
    List<CameraListener> listeners = new ArrayList<CameraListener>( cameraListeners );
    for( CameraListener listener : listeners ) {
      listener.receivedPicture( image );
    }
  }

  private void notifyListenersWithoutPicture() {
    List<CameraListener> listeners = new ArrayList<CameraListener>( cameraListeners );
    for( CameraListener listener : listeners ) {
      listener.receivedPicture( null );
    }
  }

  public class ImageUploadReceiver extends FileUploadReceiver {

    private final Display display;
    private Image image;

    public ImageUploadReceiver( Display display ) {
      this.display = display;
    }

    @Override
    public void receive( InputStream stream, FileDetails details ) throws IOException {
      image = new Image( display, stream );
    }

    public Image getImage() {
      return image;
    }

    public void reset() {
      image = null;
    }

  }

}

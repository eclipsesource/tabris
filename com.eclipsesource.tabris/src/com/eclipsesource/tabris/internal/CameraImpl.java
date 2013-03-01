package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.camera.CameraCallback;
import com.eclipsesource.tabris.camera.CameraOptions;


@SuppressWarnings("restriction")
public class CameraImpl extends AbstractOperationHandler implements Camera {

  private static final String TYPE = "tabris.Camera";
  private static final String OPEN_METHOD = "open";
  private static final String PROPERTY_RESOLUTION = "resolution";
  private static final String PROPERTY_SAVETOALBUM = "saveToAlbum";
  private static final String IMAGE_SELECTION_EVENT = "ImageSelection";
  private static final String IMAGE_SELECTION_ERROR_EVENT = "ImageSelectionError";
  private static final String PROPERTY_IMAGE = "image";

  private final RemoteObject remoteObject;
  private CameraCallback callback;

  public CameraImpl() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( TYPE );
    remoteObject.setHandler( this );
  }

  private Image decodeImage( String encodedImage ) {
    byte[] bytes = Base64.decode( encodedImage );
    ByteArrayInputStream stream = new ByteArrayInputStream( bytes );
    return new Image( Display.getCurrent(), stream );
  }

  @Override
  public void takePicture( CameraOptions options, CameraCallback callback ) {
    checkArgumentNotNull( callback, "Callback" );
    checkArgumentNotNull( options, "Options" );
    this.callback = callback;
    Map<String, Object> properties = createProperties( options );
    remoteObject.call( OPEN_METHOD, properties );
  }

  private Map<String, Object> createProperties( CameraOptions options ) {
    Map<String, Object> properties = new HashMap<String, Object>();
    assResolution( properties, options );
    addSaveToAlbum( properties, options );
    return properties;
  }

  private void assResolution( Map<String, Object> properties, CameraOptions options ) {
    Point resolution = options.getResolution();
    if( resolution != null ) {
      properties.put( PROPERTY_RESOLUTION, new int[] { resolution.x, resolution.y } );
    }
  }

  private void addSaveToAlbum( Map<String, Object> properties, CameraOptions options ) {
    if( options.savesToAlbum() ) {
      properties.put( PROPERTY_SAVETOALBUM, Boolean.TRUE );
    }
  }

  @Override
  public void handleNotify( String event, Map<String,Object> properties ) {
    if( IMAGE_SELECTION_EVENT.equals( event ) ) {
      callback.onSuccess( decodeImage( ( String )properties.get( PROPERTY_IMAGE ) ) );
    } else if( IMAGE_SELECTION_ERROR_EVENT.equals( event ) ) {
      callback.onError();
    }
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

}

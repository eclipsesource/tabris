/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
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
import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_COMPRESSON_QUALITY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_RESOLUTION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SOURCETYPE;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.camera.CameraListener;
import com.eclipsesource.tabris.camera.PhotoAlbum;
import com.eclipsesource.tabris.camera.PhotoAlbumListener;
import com.eclipsesource.tabris.camera.PhotoAlbumOptions;


public class PhotoAlbumImpl implements PhotoAlbum {

  private final CameraImpl camera;
  private final List<ListenerHolder> listeners;

  public PhotoAlbumImpl() {
    listeners = new ArrayList<ListenerHolder>();
    camera = ( CameraImpl )RWT.getClient().getService( Camera.class );
  }

  @Override
  public void open( PhotoAlbumOptions options ) {
    whenNull( options ).throwIllegalArgument( "Options must not be null" );
    JsonObject properties = createProperties( options );
    RemoteObject remoteObject = camera.getRemoteObject();
    remoteObject.call( METHOD_OPEN, properties );
  }

  private JsonObject createProperties( PhotoAlbumOptions options ) {
    JsonObject properties = new JsonObject().add( PROPERTY_SOURCETYPE, "saved_photo_album" );
    addResolution( properties, options );
    addCompressionQuality( properties, options );
    return properties;
  }

  private void addResolution( JsonObject properties, PhotoAlbumOptions options ) {
    Point resolution = options.getResolution();
    if( resolution != null ) {
      JsonArray jsonArray = new JsonArray();
      jsonArray.add( resolution.x );
      jsonArray.add( resolution.y );
      properties.add( PROPERTY_RESOLUTION, jsonArray );
    }
  }

  private void addCompressionQuality( JsonObject properties, PhotoAlbumOptions options ) {
    properties.add( PROPERTY_COMPRESSON_QUALITY, options.getCompressionQuality() );
  }

  @Override
  public void addPhotoAlbumListener( PhotoAlbumListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    ListenerHolder holder = createDelegatingCameraListener( listener );
    listeners.add( holder );
    camera.addCameraListener( holder.cameraListener );
  }

  private ListenerHolder createDelegatingCameraListener( final PhotoAlbumListener listener ) {
    return new ListenerHolder( listener, new CameraListener() {

      @Override
      public void receivedPicture( Image image ) {
        listener.receivedImage( image );
      }
    } );
  }

  @Override
  public void removePhotoAlbumListener( PhotoAlbumListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    ListenerHolder holder = getListenerHolder( listener );
    camera.removeCameraListener( holder.cameraListener );
    listeners.remove( holder );
  }

  private ListenerHolder getListenerHolder( PhotoAlbumListener listener ) {
    for( ListenerHolder holder : listeners ) {
      if( holder.albumListener == listener ) {
        return holder;
      }
    }
    return null;
  }

  private static class ListenerHolder {

    private final PhotoAlbumListener albumListener;
    private final CameraListener cameraListener;

    public ListenerHolder( PhotoAlbumListener albumListener, CameraListener cameraListener ) {
      this.albumListener = albumListener;
      this.cameraListener = cameraListener;
    }

  }

}

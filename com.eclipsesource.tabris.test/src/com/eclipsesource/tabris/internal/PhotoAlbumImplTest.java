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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.camera.PhotoAlbumListener;
import com.eclipsesource.tabris.camera.PhotoAlbumOptions;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class PhotoAlbumImplTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private CameraImpl camera;

  @Before
  public void setUp() {
    Client client = mock( TabrisClient.class );
    camera = new CameraImpl();
    when( client.getService( Camera.class ) ).thenReturn( camera );
    environment.setClient( client );
    new Display();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PhotoAlbumImpl.class ) );
  }

  @Test
  public void testCameraListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PhotoAlbumListener.class ) );
  }

  @Test
  public void testSetsNoInitialCameraOptionsWithDefaultOptions() {
    RemoteObject remoteObject = environment.getServiceObject();

    new PhotoAlbumImpl();

    verify( remoteObject, never() ).set( eq( "resolution" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "sourceType" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "compressionQuality" ), any( JsonValue.class ) );
  }

  @Test
  public void testSendsOpenWithOpenCall() {
    PhotoAlbumImpl album = new PhotoAlbumImpl();
    RemoteObject remoteObject = camera.getRemoteObject();
    PhotoAlbumListener listener = mock( PhotoAlbumListener.class );
    album.addPhotoAlbumListener( listener );

    album.open( createOptions() );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "open" ), captor.capture() );
    JsonArray resolution = captor.getValue().get( "resolution" ).asArray();
    assertEquals( 100, resolution.get( 0 ).asInt() );
    assertEquals( 100, resolution.get( 1 ).asInt() );
    assertEquals( captor.getValue().get( "sourceType" ).asString(), "saved_photo_album" );
    assertEquals( captor.getValue().get( "compressionQuality" ).asFloat(), 0.5F, 0 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullOptions() {
    PhotoAlbumImpl album = new PhotoAlbumImpl();

    album.open( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddFailsWithNullListener() {
    PhotoAlbumImpl album = new PhotoAlbumImpl();

    album.addPhotoAlbumListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveFailsWithNullListener() {
    PhotoAlbumImpl album = new PhotoAlbumImpl();

    album.removePhotoAlbumListener( null );
  }

  @Test
  public void testDelegatesError() {
    PhotoAlbumImpl album = new PhotoAlbumImpl();
    PhotoAlbumListener listener = mock( PhotoAlbumListener.class );
    album.addPhotoAlbumListener( listener );

    album.open( createOptions() );
    environment.dispatchNotifyOnServiceObject( "ImageSelectionError", null );

    verify( listener ).receivedImage( null );
  }

  @Test
  public void testDelegatesErrorToAllListeners() {
    PhotoAlbumImpl album = new PhotoAlbumImpl();
    PhotoAlbumListener listener1 = mock( PhotoAlbumListener.class );
    PhotoAlbumListener listener2 = mock( PhotoAlbumListener.class );
    album.addPhotoAlbumListener( listener1 );
    album.addPhotoAlbumListener( listener2 );

    album.open( createOptions() );
    environment.dispatchNotifyOnServiceObject( "ImageSelectionError", null );

    verify( listener1 ).receivedImage( null );
    verify( listener2 ).receivedImage( null );
  }


  @Test
  public void testDelegatesCancel() {
    PhotoAlbumImpl album = new PhotoAlbumImpl();
    PhotoAlbumListener listener = mock( PhotoAlbumListener.class );
    album.addPhotoAlbumListener( listener );

    album.open( createOptions() );
    environment.dispatchNotifyOnServiceObject( "ImageSelectionCancel", null );

    verify( listener ).receivedImage( null );
  }

  @Test
  public void testDelegatesCancelToAllListeners() {
    PhotoAlbumImpl album = new PhotoAlbumImpl();
    PhotoAlbumListener listener1 = mock( PhotoAlbumListener.class );
    PhotoAlbumListener listener2 = mock( PhotoAlbumListener.class );
    album.addPhotoAlbumListener( listener1 );
    album.addPhotoAlbumListener( listener2 );

    album.open( createOptions() );
    environment.dispatchNotifyOnServiceObject( "ImageSelectionCancel", null );

    verify( listener1 ).receivedImage( null );
    verify( listener2 ).receivedImage( null );
  }

  @Test
  public void testDelegatesImage() throws IOException {
    String encodedImage = getEncodedImage();
    PhotoAlbumImpl album = new PhotoAlbumImpl();
    PhotoAlbumListener listener = mock( PhotoAlbumListener.class );
    album.addPhotoAlbumListener( listener );

    album.open( createOptions() );
    JsonObject properties = new JsonObject();
    properties.add( "image", encodedImage );
    environment.dispatchNotifyOnServiceObject( "ImageSelection", properties );

    verify( listener ).receivedImage( any( Image.class ) );
  }

  @Test
  public void testDelegatesImageToAllListeners() throws IOException {
    String encodedImage = getEncodedImage();
    PhotoAlbumImpl album = new PhotoAlbumImpl();
    PhotoAlbumListener listener1 = mock( PhotoAlbumListener.class );
    PhotoAlbumListener listener2 = mock( PhotoAlbumListener.class );
    album.addPhotoAlbumListener( listener1 );
    album.addPhotoAlbumListener( listener2 );

    album.open( createOptions() );
    JsonObject properties = new JsonObject();
    properties.add( "image", encodedImage );
    environment.dispatchNotifyOnServiceObject( "ImageSelection", properties );

    verify( listener1 ).receivedImage( any( Image.class ) );
    verify( listener2 ).receivedImage( any( Image.class ) );
  }

  private String getEncodedImage() throws IOException {
    InputStream resourceStream = getClass().getResourceAsStream( "tabris.png" );
    return Base64.encodeBytes( getBytes( resourceStream ) );
  }

  private byte[] getBytes( InputStream is ) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[ 16384 ];
    while( ( nRead = is.read( data, 0, data.length ) ) != -1 ) {
      buffer.write( data, 0, nRead );
    }
    buffer.flush();
    return buffer.toByteArray();
  }

  private PhotoAlbumOptions createOptions() {
    PhotoAlbumOptions options = new PhotoAlbumOptions();
    options.setResolution( 100, 100 );
    options.setCompressionQuality( 0.5F );
    return options;
  }

}

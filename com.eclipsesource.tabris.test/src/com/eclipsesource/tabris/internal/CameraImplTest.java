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
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.test.TabrisTestUtil.mockServiceObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.camera.CameraListener;
import com.eclipsesource.tabris.camera.CameraOptions;
import com.eclipsesource.tabris.test.TabrisTestUtil;


@RunWith( MockitoJUnitRunner.class )
public class CameraImplTest {

  @Before
  public void setUp() {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    new Display();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( CameraImpl.class ) );
  }

  @Test
  public void testCameraListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( CameraListener.class ) );
  }

  @Test
  public void testSetsNoInitialCameraOptionsWithDefaultOptions() {
    RemoteObject remoteObject = mockServiceObject();

    new CameraImpl();

    verify( remoteObject, never() ).set( eq( "resolution" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "saveToAlbum" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "sourceType" ), any( JsonValue.class ) );
  }

  @Test
  public void testSendsOpenWithTakePhotoCall() {
    RemoteObject remoteObject = mockServiceObject();
    Camera camera = new CameraImpl();
    CameraListener listener = mock( CameraListener.class );
    camera.addCameraListener( listener );

    camera.takePicture( createOptions() );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "open" ), captor.capture() );
    JsonArray resolution = captor.getValue().get( "resolution" ).asArray();
    assertEquals( 100, resolution.get( 0 ).asInt() );
    assertEquals( 100, resolution.get( 1 ).asInt() );
    assertTrue( captor.getValue().get( "saveToAlbum" ).asBoolean() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullOptions() {
    Camera camera = new CameraImpl();

    camera.takePicture( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddFailsWithNullListener() {
    Camera camera = new CameraImpl();

    camera.addCameraListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveFailsWithNullListener() {
    Camera camera = new CameraImpl();

    camera.removeCameraListener( null );
  }

  @Test
  public void testDelegatesError() {
    CameraImpl camera = new CameraImpl();
    CameraListener listener = mock( CameraListener.class );
    camera.addCameraListener( listener );

    camera.takePicture( createOptions() );
    TabrisTestUtil.dispatchNotify( camera.getRemoteObject(), "ImageSelectionError", null );

    verify( listener ).receivedPicture( null );
  }

  @Test
  public void testDelegatesErrorToAllListeners() {
    CameraImpl camera = new CameraImpl();
    CameraListener listener1 = mock( CameraListener.class );
    CameraListener listener2 = mock( CameraListener.class );
    camera.addCameraListener( listener1 );
    camera.addCameraListener( listener2 );

    camera.takePicture( createOptions() );
    TabrisTestUtil.dispatchNotify( camera.getRemoteObject(), "ImageSelectionError", null );

    verify( listener1 ).receivedPicture( null );
    verify( listener2 ).receivedPicture( null );
  }

  @Test
  public void testDelegatesImage() throws IOException {
    String encodedImage = getEncodedImage();
    CameraImpl camera = new CameraImpl();
    RemoteObject remoteObject = camera.getRemoteObject();
    CameraListener listener = mock( CameraListener.class );
    camera.addCameraListener( listener );

    camera.takePicture( createOptions() );
    JsonObject properties = new JsonObject();
    properties.add( "image", encodedImage );
    TabrisTestUtil.dispatchNotify( remoteObject, "ImageSelection", properties );

    verify( listener ).receivedPicture( any( Image.class ) );
  }

  @Test
  public void testDelegatesImageToAllListeners() throws IOException {
    String encodedImage = getEncodedImage();
    CameraImpl camera = new CameraImpl();
    RemoteObject remoteObject = camera.getRemoteObject();
    CameraListener listener1 = mock( CameraListener.class );
    CameraListener listener2 = mock( CameraListener.class );
    camera.addCameraListener( listener1 );
    camera.addCameraListener( listener2 );

    camera.takePicture( createOptions() );
    JsonObject properties = new JsonObject();
    properties.add( "image", encodedImage );
    TabrisTestUtil.dispatchNotify( remoteObject, "ImageSelection", properties );

    verify( listener1 ).receivedPicture( any( Image.class ) );
    verify( listener2 ).receivedPicture( any( Image.class ) );
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

  private CameraOptions createOptions() {
    CameraOptions options = new CameraOptions();
    options.setResolution( 100, 100 );
    options.setSaveToAlbum( true );
    return options;
  }

}

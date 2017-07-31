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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.Serializable;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.internal.serverpush.ServerPushManager;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.camera.CameraListener;
import com.eclipsesource.tabris.camera.CameraOptions;
import com.eclipsesource.tabris.internal.CameraImpl.ImageUploadReceiver;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


@SuppressWarnings("restriction")
public class CameraImplTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();
  private Display display;

  @Before
  public void setUp() {
    display = new Display();
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
    RemoteObject remoteObject = environment.getServiceObject();

    new CameraImpl();

    verify( remoteObject, never() ).set( eq( "resolution" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "saveToAlbum" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "sourceType" ), any( JsonValue.class ) );
    verify( remoteObject, never() ).set( eq( "compressionQuality" ), any( JsonValue.class ) );
  }

  @Test
  public void testSendsOpenWithTakePhotoCall() {
    RemoteObject remoteObject = environment.getServiceObject();
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
    assertEquals( captor.getValue().get( "compressionQuality" ).asFloat(), 0.5F, 0 );
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
  public void testDelegatesImage_activatesServerPush() {
    CameraImpl camera = new CameraImpl();
    CameraListener listener = mock( CameraListener.class );
    camera.addCameraListener( listener );

    camera.takePicture( createOptions() );
    environment.dispatchNotifyOnServiceObject( "ImageSelection", new JsonObject() );

    assertTrue( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testDelegatesImage_withFailedUpload() {
    CameraImpl camera = new CameraImpl();
    CameraListener listener = mock( CameraListener.class );
    camera.addCameraListener( listener );
    camera.takePicture( createOptions() );
    ImageUploadReceiver receiver = mock( ImageUploadReceiver.class );

    camera.handleUploadFailed( display, receiver );
    display.readAndDispatch();

    verify( listener ).receivedPicture( null );
    verify( receiver ).reset();
    assertFalse( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testDelegatesImage_withSuccessfulUpload() {
    Image image = getImage();
    CameraImpl camera = new CameraImpl();
    CameraListener listener = mock( CameraListener.class );
    camera.addCameraListener( listener );
    camera.takePicture( createOptions() );
    ImageUploadReceiver receiver = mock( ImageUploadReceiver.class );
    when( receiver.getImage() ).thenReturn( image );

    camera.handleUploadFinished( display, receiver );
    display.readAndDispatch();

    verify( listener ).receivedPicture( image );
    verify( receiver ).reset();
    assertFalse( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testDelegatesError() {
    CameraImpl camera = new CameraImpl();
    CameraListener listener = mock( CameraListener.class );
    camera.addCameraListener( listener );

    camera.takePicture( createOptions() );
    environment.dispatchNotifyOnServiceObject( "ImageSelectionError", null );

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
    environment.dispatchNotifyOnServiceObject( "ImageSelectionError", null );

    verify( listener1 ).receivedPicture( null );
    verify( listener2 ).receivedPicture( null );
  }


  @Test
  public void testDelegatesCancel() {
    CameraImpl camera = new CameraImpl();
    CameraListener listener = mock( CameraListener.class );
    camera.addCameraListener( listener );

    camera.takePicture( createOptions() );
    environment.dispatchNotifyOnServiceObject( "ImageSelectionCancel", null );

    verify( listener ).receivedPicture( null );
  }

  @Test
  public void testDelegatesCancelToAllListeners() {
    CameraImpl camera = new CameraImpl();
    CameraListener listener1 = mock( CameraListener.class );
    CameraListener listener2 = mock( CameraListener.class );
    camera.addCameraListener( listener1 );
    camera.addCameraListener( listener2 );

    camera.takePicture( createOptions() );
    environment.dispatchNotifyOnServiceObject( "ImageSelectionCancel", null );

    verify( listener1 ).receivedPicture( null );
    verify( listener2 ).receivedPicture( null );
  }

  private CameraOptions createOptions() {
    CameraOptions options = new CameraOptions();
    options.setResolution( 100, 100 );
    options.setSaveToAlbum( true );
    options.setCompressionQuality( 0.5F );
    return options;
  }

  private Image getImage() {
    InputStream resourceStream = getClass().getResourceAsStream( "tabris.png" );
    return new Image( display, resourceStream );
  }

}

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

import static com.eclipsesource.tabris.test.TabrisTestUtil.mockRemoteObject;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.internal.Base64;


@RunWith( MockitoJUnitRunner.class )
public class CameraTest {

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

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullOptions() {
    new Camera( null );
  }

  @Test
  public void testSetsNoInitialCameraOptionsWithDefaultOptions() {
    RemoteObject remoteObject = mockRemoteObject();

    new Camera( new CameraOptions() );

    verify( remoteObject, never() ).set( eq( "resolution" ), anyObject() );
    verify( remoteObject, never() ).set( eq( "saveToAlbum" ), anyObject() );
    verify( remoteObject, never() ).set( eq( "sourceType" ), anyObject() );
  }

  @Test
  public void testSetsInitialCameraOptions() {
    RemoteObject remoteObject = mockRemoteObject();
    createCamera();

    verify( remoteObject ).set( eq( "resolution" ), eq( new int[] { 100, 100 } ) );
    verify( remoteObject ).set( "saveToAlbum", true );
  }

  @Test
  public void testSendsOpenWithTakePhotoCall() {
    RemoteObject remoteObject = mockRemoteObject();
    Camera camera = createCamera();

    camera.takePicture( mock( CameraCallback.class ) );

    verify( remoteObject ).call( "open", null );
  }

  @Test
  public void testDisposeSendsDestroy() {
    RemoteObject remoteObject = mockRemoteObject();
    Camera camera = createCamera();

    camera.dispose();

    verify( remoteObject ).destroy();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCallback() {
    Camera camera = createCamera();

    camera.takePicture( null );
  }

  @Test
  public void testDelegatesError() {
    Camera camera = createCamera();
    CameraCallback callback = mock( CameraCallback.class );

    camera.takePicture( callback );
    Fixture.dispatchNotify( camera.getRemoteObject(), "ImageSelectionError", null );

    verify( callback ).onError();
  }

  @Test
  public void testDelegatesImage() throws IOException {
    String encodedImage = getEncodedImage();
    Camera camera = createCamera();
    CameraCallback callback = mock( CameraCallback.class );

    camera.takePicture( callback );
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( "image", encodedImage );
    RemoteObject remoteObject = camera.getRemoteObject();
    Fixture.dispatchNotify( remoteObject, "ImageSelection", properties );

    verify( callback ).onSuccess( any( Image.class ) );
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

  private Camera createCamera() {
    CameraOptions options = new CameraOptions();
    options.setResolution( 100, 100 );
    options.setSaveToAlbum( true );
    return new Camera( options );
  }

}

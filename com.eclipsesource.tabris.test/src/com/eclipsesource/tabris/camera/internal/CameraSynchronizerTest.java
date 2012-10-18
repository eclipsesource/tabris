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
package com.eclipsesource.tabris.camera.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.rwt.internal.protocol.ClientObjectAdapter;
import org.eclipse.rap.rwt.internal.protocol.IClientObject;
import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.camera.CameraCallback;
import com.eclipsesource.tabris.camera.CameraOptions;
import com.eclipsesource.tabris.camera.CameraOptions.SourceType;


@SuppressWarnings("restriction")
public class CameraSynchronizerTest {
  
  private Camera object;
  private CameraAdapter adapter;
  private CameraSynchronizer synchronizer;

  @Before
  public void setUp() {
    Fixture.setUp();
    new Display();
    object = mock( Camera.class );
    adapter = mock( CameraAdapter.class );
    when( object.getAdapter( CameraAdapter.class ) ).thenReturn( adapter );
    when( object.getAdapter( IClientObjectAdapter.class ) ).thenReturn( new ClientObjectAdapter() );
    CameraSynchronizer original = new CameraSynchronizer( object );
    synchronizer = spy( original );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testReadData() {
    String image = "image";
    when( synchronizer.readPropertyValue( CameraSynchronizer.PROPERTY_IMAGE ) ).thenReturn( image );
    
    synchronizer.readData( object );
    
    verify( adapter ).setEncodedImage( image );
  }
  
  @Test
  public void testClose() {
    String image = "true";
    when( synchronizer.readPropertyValue( CameraSynchronizer.PROPERTY_CLOSE ) ).thenReturn( image );
    
    synchronizer.readData( object );
    
    verify( adapter ).close();
  }
  
  @Test
  public void testRenderInitialization() {
    CameraOptions options = new CameraOptions();
    options.setResolution( 100, 100 );
    options.setSourceType( SourceType.PHOTO_LIBRARY );
    options.setSaveToAlbum( true );
    when( adapter.getOptions() ).thenReturn( options );
    IClientObject clientObject = mock( IClientObject.class );
    
    synchronizer.renderInitialization( clientObject, object );
    
    verify( clientObject ).create( CameraSynchronizer.TYPE );
    verify( clientObject ).set( CameraSynchronizer.PROPERTY_RESOLUTION, new int[]{ 100, 100 } );
    verify( clientObject ).set( CameraSynchronizer.PROPERTY_SOURCETYPE, SourceType.PHOTO_LIBRARY.toString().toLowerCase() );
    verify( clientObject ).set( CameraSynchronizer.PROPERTY_SAVETOALBUM, true );
  }
  
  @Test
  public void testRenderChanges() {
    CameraAdapter cameraAdapter = new CameraAdapter();
    cameraAdapter.dispose();
    when( object.getAdapter( CameraAdapter.class ) ).thenReturn( cameraAdapter );
    IClientObject clientObject = mock( IClientObject.class );
    doReturn( clientObject ).when( synchronizer ).getClientObject();
    
    synchronizer.renderChanges( object );
    
    verify( clientObject ).destroy();
  }
  
  @Test
  public void testRenderChangesWithOpen() {
    CameraAdapter cameraAdapter = new CameraAdapter();
    cameraAdapter.open();
    when( object.getAdapter( CameraAdapter.class ) ).thenReturn( cameraAdapter );
    IClientObject clientObject = mock( IClientObject.class );
    doReturn( clientObject ).when( synchronizer ).getClientObject();
    
    synchronizer.renderChanges( object );
    
    verify( clientObject ).call( "open", null );
  }
  
  @Test
  public void testRenderChangesWithOpenOnce() {
    CameraAdapter cameraAdapter = new CameraAdapter();
    cameraAdapter.open();
    when( object.getAdapter( CameraAdapter.class ) ).thenReturn( cameraAdapter );
    IClientObject clientObject = mock( IClientObject.class );
    doReturn( clientObject ).when( synchronizer ).getClientObject();
    
    synchronizer.renderChanges( object );
    synchronizer.renderChanges( object );
    
    verify( clientObject ).call( "open", null );
  }
  
  @Test
  public void testProcessAction() throws IOException {
    when( adapter.getEncodedImage() ).thenReturn( getEncodedImage() );
    CameraCallback callback = mock( CameraCallback.class );
    when( adapter.getCallback() ).thenReturn( callback );
    
    synchronizer.processAction( object );
    
    verify( callback ).onSuccess( any( Image.class ) );
    verify( adapter ).setCallback( null );
    verify( adapter ).setEncodedImage( null );
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

  @Test
  public void testProcessActionWithError() {
    when( adapter.getEncodedImage() ).thenReturn( CameraSynchronizer.ERROR );
    CameraCallback callback = mock( CameraCallback.class );
    when( adapter.getCallback() ).thenReturn( callback );
    
    synchronizer.processAction( object );
    
    verify( callback ).onError();
    verify( adapter ).setCallback( null );
    verify( adapter ).setEncodedImage( null );
  }
  
  @Test
  public void testCameraInListener() {
    CameraSelectionAdapter cameraAdapter = new CameraSelectionAdapter();
    
    forceSelectionEvent( cameraAdapter );
    
    Message message = Fixture.getProtocolMessage();
    Camera camera = cameraAdapter.getCamera();
    IClientObjectAdapter adapter = camera.getAdapter( IClientObjectAdapter.class );
    assertNotNull( message.findCreateOperation( adapter.getId() ) );
  }

  private void forceSelectionEvent( CameraSelectionAdapter cameraAdapter ) {
    Shell shell = new Shell();
    Button button = new Button( shell, SWT.PUSH );
    button.addSelectionListener( cameraAdapter );
    String id = WidgetUtil.getId( button );
    Fixture.fakeRequestParam( id + ".selection", String.valueOf( true ) );
    Fixture.fakeRequestParam( "org.eclipse.swt.events.widgetSelected", id );
    Fixture.executeLifeCycleFromServerThread();
  }
  
  @Test
  public void testDisposesCallsDestroy() {
    CameraAdapter cameraAdapter = new CameraAdapter();
    when( object.getAdapter( CameraAdapter.class ) ).thenReturn( cameraAdapter );
    assertFalse( cameraAdapter.isDestroyed() );
    
    cameraAdapter.dispose();
    synchronizer.renderChanges( object );
    
    assertTrue( cameraAdapter.isDestroyed() );
  }

  private static class CameraSelectionAdapter extends SelectionAdapter {
    
    private Camera camera;
    
    public Camera getCamera() {
      return camera;
    }
  
    @Override
    public void widgetSelected( SelectionEvent e ) {
      CameraOptions cameraOptions = new CameraOptions();
      camera = new Camera( cameraOptions );
      camera.takePicture( new CameraCallback() {
        
        @Override
        public void onSuccess( Image image ) {
        }
        
        @Override
        public void onError() {
        }
      } );
    }
  }
  
}

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.camera.CameraCallback;
import com.eclipsesource.tabris.camera.CameraOptions;
import com.eclipsesource.tabris.camera.CameraOptions.SourceType;
import com.eclipsesource.tabris.camera.internal.CameraAdapter;


public class CameraAdapterTest {
  
  private CameraAdapter adapter;

  @Before
  public void setUp() {
    adapter = new CameraAdapter();
  }
  
  @Test
  public void testDefaults() {
    assertNull( adapter.getCallback() );
    assertNull( adapter.getOptions() );
    assertNull( adapter.getEncodedImage() );
    assertFalse( adapter.isDisposed() );
    assertFalse( adapter.isOpen() );
  }
  
  @Test
  public void testOptionsIsSafeCopy() {
    CameraOptions cameraOptions = new CameraOptions();
    cameraOptions.setResolution( 100, 100 );
    cameraOptions.setSourceType( SourceType.PHOTOLIBRARY );
    cameraOptions.setSaveToAlbum( true );
    adapter.setOptions( cameraOptions );
    
    assertNotSame( cameraOptions, adapter.getOptions() );
    assertEquals( cameraOptions, adapter.getOptions() );
  }
  
  @Test
  public void testCallback() {
    CameraCallback callback = mock( CameraCallback.class );
    
    adapter.setCallback( callback );
    
    assertEquals( callback, adapter.getCallback() );
  }
  
  @Test
  public void testEncodedImage() {
    adapter.setEncodedImage( "foo" );
    
    assertEquals( "foo", adapter.getEncodedImage() );
  }
  
  @Test
  public void testDispose() {
    adapter.dispose();
    
    assertTrue( adapter.isDisposed() );
  }
  
  @Test
  public void testOpen() {
    adapter.open();
    
    assertTrue( adapter.isOpen() );
  }
  
  @Test
  public void testClose() {
    adapter.open();
    adapter.close();
    
    assertFalse( adapter.isOpen() );
  }
}

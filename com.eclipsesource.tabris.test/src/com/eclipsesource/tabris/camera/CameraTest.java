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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.camera.CameraOptions.SourceType;
import com.eclipsesource.tabris.camera.internal.CameraAdapter;


public class CameraTest {
  
  @Before
  public void setUp() {
    Fixture.setUp();
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testCallback() {
    Camera camera = new Camera( CameraOptions.NONE );
    CameraAdapter adapter = camera.getAdapter( CameraAdapter.class );
    CameraCallback callback = mock( CameraCallback.class );
    
    camera.takePicture( callback );
    
    assertSame( callback, adapter.getCallback() );
  }
  
  @Test
  public void testResolution() {
    CameraOptions options = new CameraOptions();
    options.setResolution( 500, 500 );
    Camera camera = new Camera( options );
    CameraAdapter adapter = camera.getAdapter( CameraAdapter.class );
    
    assertEquals( new Point( 500, 500 ), adapter.getOptions().getResolution() );
  }
  
  @Test
  public void testSourceType() {
    CameraOptions options = new CameraOptions();
    options.setSourceType( SourceType.PHOTOLIBRARY );
    Camera camera = new Camera( options );
    CameraAdapter adapter = camera.getAdapter( CameraAdapter.class );
    
    assertSame( SourceType.PHOTOLIBRARY, adapter.getOptions().getSourceType() );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testPhotoWithNullOptions() {
    new Camera( null );
  }
}

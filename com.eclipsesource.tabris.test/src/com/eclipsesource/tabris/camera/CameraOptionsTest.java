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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.eclipse.swt.graphics.Point;
import org.junit.Test;

import com.eclipsesource.tabris.camera.CameraOptions.SourceType;


public class CameraOptionsTest {
  
  @Test
  public void testResolution() {
    CameraOptions cameraOptions = new CameraOptions();
    
    cameraOptions.setResolution( 500, 500 );
    
    assertEquals( new Point( 500, 500 ), cameraOptions.getResolution() );
  }
  
  @Test
  public void testSourceType() {
    CameraOptions cameraOptions = new CameraOptions();
    
    cameraOptions.setSourceType( SourceType.PHOTO_LIBRARY );

    assertEquals( SourceType.PHOTO_LIBRARY, cameraOptions.getSourceType() );
  }
  
  @Test
  public void testNoneOptions() {
    assertNull( CameraOptions.NONE.getResolution() );
    assertSame( SourceType.CAMERA, CameraOptions.NONE.getSourceType() );
    assertFalse( CameraOptions.NONE.savesToAlbum() );
  }
}

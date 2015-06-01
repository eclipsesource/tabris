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
package com.eclipsesource.tabris.camera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.eclipse.swt.graphics.Point;
import org.junit.Test;


public class PhotoAlbumOptionsTest {

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PhotoAlbumOptions.class ) );
  }

  @Test
  public void testNoneOptions() {
    assertNull( CameraOptions.NONE.getResolution() );
    assertFalse( CameraOptions.NONE.savesToAlbum() );
    assertEquals( 1.0F, CameraOptions.NONE.getCompressionQuality(), 0 );
  }

  @Test
  public void testResolution() {
    PhotoAlbumOptions albumOptions = new PhotoAlbumOptions();

    albumOptions.setResolution( 500, 500 );

    assertEquals( new Point( 500, 500 ), albumOptions.getResolution() );
  }

  @Test
  public void testDefaultResolutionIsNull() {
    PhotoAlbumOptions albumOptions = new PhotoAlbumOptions();

    Point resolution = albumOptions.getResolution();

    assertNull( resolution );
  }

  @Test
  public void testSetsComptressionQuality() {
    PhotoAlbumOptions albumOptions = new PhotoAlbumOptions();

    albumOptions.setCompressionQuality( 0.5F );

    assertEquals( 0.5F, albumOptions.getCompressionQuality(), 0 );
  }

  @Test
  public void testDefaultCompressionQualityIsOne() {
    PhotoAlbumOptions albumOptions = new PhotoAlbumOptions();

    float compressionQuality = albumOptions.getCompressionQuality();

    assertEquals( 1.0F, compressionQuality, 0 );
  }

}

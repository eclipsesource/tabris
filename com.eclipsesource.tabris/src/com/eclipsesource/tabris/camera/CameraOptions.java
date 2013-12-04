/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.camera;

import static com.eclipsesource.tabris.internal.Clauses.when;

import java.io.Serializable;

import org.eclipse.swt.graphics.Point;

/**
 * <p>
 * The <code>CameraOptions</code> is used to configure a <code>Camera</code>
 * object when taking a picture via {@link Camera#takePicture(CameraOptions)}.
 * </p>
 *
 * @see Camera
 * @since 0.8
 */
public class CameraOptions implements Serializable {

  public static final CameraOptions NONE = new CameraOptions();
  private Point resolution;
  private boolean saveToAlbum;
  private float quality;

  public CameraOptions() {
    quality = 1.0F;
  }

  /**
   * <p>
   * Defines the preferred resolution for images. When possible images will have
   * the configured size when sending them to the server.
   * </p>
   *
   * @param x Preferred width. Must be positive.
   * @param y Preferred height. Must be positive.
   */
  public void setResolution( int x, int y ) {
    resolution = new Point( x, y );
  }

  /**
   * <p>
   * Returns the preferred resolution.
   * </p>
   */
  public Point getResolution() {
    return resolution;
  }

  /**
   * <p>
   * When set the taken pictures will be saved to the client's photo album.
   * </p>
   */
  public void setSaveToAlbum( boolean saveToAlbum ) {
    this.saveToAlbum = saveToAlbum;
  }

  /**
   * <p>
   * Returns if the taken pictures should be saved to the client's photo album
   * or not.
   * </p>
   */
  public boolean savesToAlbum() {
    return saveToAlbum;
  }

  /**
   * <p>
   * The quality of the resulting image, expressed as a value from 0.0 to 1.0. The value 0.0 represents the maximum
   * compression (or lowest quality) while the value 1.0 represents the least compression (or best quality).
   * </p>
   *
   * @since 1.2
   */
  public void setCompressionQuality( float quality ) {
    when( quality < 0 || quality > 1 )
      .throwIllegalArgument( "Compression Quality must be >= 0 and <= 1 but was " + quality );
    this.quality = quality;
  }

  /**
   * <p>
   * Returns the compression quality of the resulting image. The default value is 1.0.
   * </p>
   *
   * @since 1.2
   */
  public float getCompressionQuality() {
    return quality;
  }
}

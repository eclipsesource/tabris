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

import org.eclipse.swt.graphics.Point;


/**
 * <p>
 * The <code>CameraOptions</code> is used to configure a <code>Camera</code> object. Typically it's passed as a
 * constructor argument to a <code>Camera</code>
 * </p>
 *
 * @see Camera
 * @since 0.8
 */
public class CameraOptions {

  public static final CameraOptions NONE = new CameraOptions();

  private Point resolution;
  private boolean saveToAlbum;

  /**
   * <p>
   * Defines the preferred resolution for images. When possible images will have the configured size when sending them
   * to the server.
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
   * When set the taken pictures willb e saved to the client's photo album.
   * </p>
   */
  public void setSaveToAlbum( boolean saveToAlbum ) {
    this.saveToAlbum = saveToAlbum;
  }

  /**
   * <p>
   * Returns if the taken pictures should be saved to the client's photo album or not.
   * </p>
   */
  public boolean savesToAlbum() {
    return saveToAlbum;
  }

}

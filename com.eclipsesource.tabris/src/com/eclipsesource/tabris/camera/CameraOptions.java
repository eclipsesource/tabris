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
  
  private SourceType sourceType;
  private Point resolution;
  private boolean saveToAlbum;

  /**
   * <p>
   * The source type defines the sources to take pictures from. 
   * </p>
   */
  public enum SourceType {
    /**
     * <p>
     * CAMERA means to take pictures from the client's built in camera.
     * </p>
     */
    CAMERA,
    
    /**
     * <p>
     * PHOTO_LIBRARY means to take pictures from the client device's photo library.
     * </p>
     */
    PHOTO_LIBRARY,
    
    /**
     * <p>
     * SAVED_PHOTO_ALBUM means to select images from saved images on the client device.
     * </p>
     */
    SAVED_PHOTO_ALBUM
  }
  
  public CameraOptions() {
    sourceType = SourceType.CAMERA;
  }
  
  /**
   * <p>
   * Defines the <code>Camera</code> object's source type. See <code>SourceType</code> for the available source types.
   * The default source type is <code>SourceType.CAMERA</code>.
   * </p>
   */
  public void setSourceType( SourceType sourceType ) {
    this.sourceType = sourceType;
  }
  
  /**
   * <p>
   * Returns the used <code>SourceType</code>.
   * </p>
   */
  public SourceType getSourceType() {
    return sourceType;
  }

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( resolution == null )
                                                      ? 0
                                                      : resolution.hashCode() );
    result = prime * result + ( saveToAlbum
                                           ? 1231
                                           : 1237 );
    result = prime * result + ( ( sourceType == null )
                                                      ? 0
                                                      : sourceType.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj )
      return true;
    if( obj == null )
      return false;
    if( getClass() != obj.getClass() )
      return false;
    CameraOptions other = ( CameraOptions )obj;
    if( resolution == null ) {
      if( other.resolution != null )
        return false;
    } else if( !resolution.equals( other.resolution ) )
      return false;
    if( saveToAlbum != other.saveToAlbum )
      return false;
    if( sourceType != other.sourceType )
      return false;
    return true;
  }

}

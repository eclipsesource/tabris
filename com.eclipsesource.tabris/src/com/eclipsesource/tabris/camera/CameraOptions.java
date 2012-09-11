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
 * @since 0.8
 */
public class CameraOptions {
  
  public static final CameraOptions NONE = new CameraOptions(); 
  
  private SourceType sourceType;
  private Point resolution;
  private boolean saveToAlbum;

  public enum SourceType {
    CAMERA, PHOTOLIBRARY, SAVEDPHOTOALBUM
  }
  
  public CameraOptions() {
    sourceType = SourceType.CAMERA;
  }
  
  public void setSourceType( SourceType sourceType ) {
    this.sourceType = sourceType;
  }
  
  public SourceType getSourceType() {
    return sourceType;
  }

  public void setResolution( int x, int y ) {
    resolution = new Point( x, y );
  }

  public Point getResolution() {
    return resolution;
  }

  public boolean savesToAlbum() {
    return saveToAlbum;
  }

  public void setSaveToAlbum( boolean saveToAlbum ) {
    this.saveToAlbum = saveToAlbum;
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

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
package com.eclipsesource.tabris.internal.camera;

import org.eclipse.swt.graphics.Point;

import com.eclipsesource.tabris.camera.CameraCallback;
import com.eclipsesource.tabris.camera.CameraOptions;


public class CameraAdapter {

  private CameraOptions options;
  private CameraCallback callback;
  private boolean disposed;
  private boolean destroyed;
  private String encodedImage;
  private boolean open;

  public void setOptions( CameraOptions options ) {
    this.options = new CameraOptions();
    copyOptions( options );
  }

  private void copyOptions( CameraOptions options ) {
    Point resolution = options.getResolution();
    if( resolution != null ) {
      this.options.setResolution( resolution.x, resolution.y );
    }
    this.options.setSourceType( options.getSourceType() );
    this.options.setSaveToAlbum( options.savesToAlbum() );
  }

  public CameraOptions getOptions() {
    return options;
  }

  public void setCallback( CameraCallback callback ) {
    this.callback = callback;
  }

  public CameraCallback getCallback() {
    return callback;
  }

  public void dispose() {
    disposed = true;
  }

  public boolean isDisposed() {
    return disposed;
  }

  public void setEncodedImage( String encodedImage ) {
    this.encodedImage = encodedImage;
  }

  public String getEncodedImage() {
    return encodedImage;
  }

  public void open() {
    this.open = true;
  }
  
  public void close() {
    this.open = false;
  }

  public boolean isOpen() {
    return open;
  }

  public boolean isDestroyed() {
    return destroyed;
  }
  
  public void destroy() {
    destroyed = true;
  }
  
}

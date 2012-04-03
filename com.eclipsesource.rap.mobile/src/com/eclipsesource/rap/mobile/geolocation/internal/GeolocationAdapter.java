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
package com.eclipsesource.rap.mobile.geolocation.internal;

import com.eclipsesource.rap.mobile.geolocation.GeolocationCallback;
import com.eclipsesource.rap.mobile.geolocation.GeolocationOptions;
import com.eclipsesource.rap.mobile.geolocation.Position;
import com.eclipsesource.rap.mobile.geolocation.PositionError;


public class GeolocationAdapter {
  
  public enum NeedsPositionFlavor {
    NEVER, ONCE, CONTINUOUS
  }
  
  private PositionError error;
  private Position position;
  private NeedsPositionFlavor flavor;
  private GeolocationCallback callback;
  private GeolocationOptions options;
  private boolean disposed;

  public void setError( PositionError error ) {
    this.error = error;
  }
  
  public PositionError getError() {
    return error;
  }
  
  public void setPosition( Position position ) {
    this.position = position;
  }
  
  public Position getPosition() {
    return position;
  }

  public NeedsPositionFlavor getFlavor() {
    return flavor;
  }

  public void setFlavor( NeedsPositionFlavor flavor ) {
    this.flavor = flavor;
  }

  public GeolocationCallback getCallback() {
    return callback;
  }

  public void setCallback( GeolocationCallback callback ) {
    this.callback = callback;
  }

  public GeolocationOptions getOptions() {
    return options;
  }

  public void setOptions( GeolocationOptions options ) {
    this.options = options;
  }

  public void dispose() {
    this.disposed = true;
  }
  
  public boolean isDisposed() {
    return disposed;
  }
}

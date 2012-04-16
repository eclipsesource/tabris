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
package com.eclipsesource.rap.mobile.internal;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.json.JSONArray;
import org.json.JSONException;


public class GCOperationDispatcher {

  static final String PROP_POLYLINE = "polyline";
  static final String PROP_FOREGROUND = "foreground";
  static final String PROP_LINE_WIDTH = "lineWidth";
  
  private final GC gc;
  private JSONArray drawings;

  public GCOperationDispatcher( GC gc, String drawings ) {
    this.gc = gc;
    try {
      this.drawings = new JSONArray( drawings );
    } catch( JSONException jex ) {
      throw new IllegalArgumentException( "Drawings are not valid json: " + drawings );
    }
  }

  public void dispatch() {
    try {
      doDispatch();
    } catch( JSONException e ) {
      throw new IllegalStateException( "Drawings cannot be dispatch. Invalid format: " + drawings );
    }
  }

  private void doDispatch() throws JSONException {
    for( int i = 0; i < drawings.length(); i++ ) {
      JSONArray operation = drawings.getJSONArray( i );
      dispatchOperation( operation );
    }
  }

  private void dispatchOperation( JSONArray operation ) throws JSONException {
    String operationType = operation.getString( 0 );
    JSONArray parameters = operation.getJSONArray( 1 );
    if( PROP_LINE_WIDTH.equals( operationType ) ) {
      dispatchLineWidth( parameters );
    } else if( PROP_FOREGROUND.equals( operationType ) ) {
      dispatchSetForeground( parameters );
    } else if( PROP_POLYLINE.equals( operationType ) ) {
      dispatchDrawPolyline( parameters );
    }
  }

  private void dispatchLineWidth( JSONArray parameters ) throws JSONException {
    int width = parameters.getInt( 0 );
    gc.setLineWidth( width );
  }

  private void dispatchSetForeground( JSONArray parameters ) throws JSONException {
    int r = parameters.getInt( 0 );
    int g = parameters.getInt( 1 );
    int b = parameters.getInt( 2 );
    gc.setForeground( new Color( gc.getDevice(), new RGB( r, g, b ) ) );
  }

  private void dispatchDrawPolyline( JSONArray parameters ) throws JSONException {
    int[] polyline = new int[ parameters.length() ];
    for( int i = 0; i < parameters.length(); i++ ) {
      polyline[ i ] = parameters.getInt( i );
    }
    gc.drawPolyline( polyline );
  }
}

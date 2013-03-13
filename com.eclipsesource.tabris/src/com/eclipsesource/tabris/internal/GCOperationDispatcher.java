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
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.PROPERTY_FOREGROUND;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_LINE_WIDTH;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PATH;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.json.JSONArray;
import org.json.JSONException;


@SuppressWarnings("restriction")
public class GCOperationDispatcher {

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
    int lineWidth = gc.getLineWidth();
    Color foreground = gc.getForeground();
    int alpha = gc.getAlpha();
    dispatchOperations();
    restoreLastSettings( lineWidth, foreground, alpha );
  }

  private void dispatchOperations() throws JSONException {
    for( int i = 0; i < drawings.length(); i++ ) {
      JSONArray operation = drawings.getJSONArray( i );
      dispatchOperation( operation );
    }
  }

  private void restoreLastSettings( int lineWidth, Color foreground, int alpha ) {
    gc.setLineWidth( lineWidth );
    gc.setForeground( foreground );
    gc.setAlpha( alpha );
  }

  private void dispatchOperation( JSONArray operation ) throws JSONException {
    String operationType = operation.getString( 0 );
    JSONArray parameters = operation.getJSONArray( 1 );
    if( PROPERTY_LINE_WIDTH.equals( operationType ) ) {
      dispatchLineWidth( parameters );
    } else if( PROPERTY_FOREGROUND.equals( operationType ) ) {
      dispatchSetForeground( parameters );
    } else if( PROPERTY_PATH.equals( operationType ) ) {
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
    int a = parameters.getInt( 3 );
    gc.setForeground( new Color( gc.getDevice(), new RGB( r, g, b ) ) );
    gc.setAlpha( a );
  }

  private void dispatchDrawPolyline( JSONArray parameters ) throws JSONException {
    int[] polyline = new int[ parameters.length() ];
    for( int i = 0; i < parameters.length(); i++ ) {
      polyline[ i ] = parameters.getInt( i );
    }
    gc.drawPolyline( polyline );
  }
}

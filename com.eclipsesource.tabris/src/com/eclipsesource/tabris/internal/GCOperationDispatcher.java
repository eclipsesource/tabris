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

import java.io.Serializable;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;


public class GCOperationDispatcher implements Serializable {

  private final GC gc;
  private final JsonArray drawings;

  public GCOperationDispatcher( GC gc, String drawings ) {
    this.gc = gc;
    this.drawings = JsonArray.readFrom( drawings );
  }

  public void dispatch() {
    doDispatch();
  }

  private void doDispatch() {
    int lineWidth = gc.getLineWidth();
    Color foreground = gc.getForeground();
    int alpha = gc.getAlpha();
    dispatchOperations();
    restoreLastSettings( lineWidth, foreground, alpha );
  }

  private void dispatchOperations() {
    for( int i = 0; i < drawings.size(); i++ ) {
      JsonArray operation = drawings.get( i ).asArray();
      dispatchOperation( operation );
    }
  }

  private void restoreLastSettings( int lineWidth, Color foreground, int alpha ) {
    gc.setLineWidth( lineWidth );
    gc.setForeground( foreground );
    gc.setAlpha( alpha );
  }

  private void dispatchOperation( JsonArray operation ) {
    String operationType = operation.get( 0 ).asString();
    JsonArray parameters = operation.get( 1 ).asArray();
    if( PROPERTY_LINE_WIDTH.equals( operationType ) ) {
      dispatchLineWidth( parameters );
    } else if( PROPERTY_FOREGROUND.equals( operationType ) ) {
      dispatchSetForeground( parameters );
    } else if( PROPERTY_PATH.equals( operationType ) ) {
      dispatchDrawPolyline( parameters );
    }
  }

  private void dispatchLineWidth( JsonArray parameters ) {
    int width = parameters.get( 0 ).asInt();
    gc.setLineWidth( width );
  }

  private void dispatchSetForeground( JsonArray parameters ) {
    int r = parameters.get( 0 ).asInt();
    int g = parameters.get( 1 ).asInt();
    int b = parameters.get( 2 ).asInt();
    int a = parameters.get( 3 ).asInt();
    gc.setForeground( new Color( gc.getDevice(), new RGB( r, g, b ) ) );
    gc.setAlpha( a );
  }

  private void dispatchDrawPolyline( JsonArray parameters ) {
    int[] polyline = new int[ parameters.size() ];
    for( int i = 0; i < parameters.size(); i++ ) {
      polyline[ i ] = Double.valueOf( parameters.get( i ).asDouble() ).intValue();
    }
    gc.drawPolyline( polyline );
  }
}

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

import org.json.JSONArray;

import com.eclipsesource.rap.mobile.internal.GCOperationDispatcher;


public class ClientCanvasTestUtil {
  
  public static String createDrawings() {
    JSONArray drawings = new JSONArray();
    JSONArray polylineParam = createPolylineParam();
    drawings.put( polylineParam );
    JSONArray lineWidthParam = createLineWidthParam();
    drawings.put( lineWidthParam );
    JSONArray colorParam = createColorParam();
    drawings.put( colorParam );
    JSONArray polylineParam2 = createPolylineParam();
    drawings.put( polylineParam2 );
    return drawings.toString();
  }

  private static JSONArray createPolylineParam() {
    JSONArray drawingParam1 = new JSONArray();
    drawingParam1.put( GCOperationDispatcher.PROP_POLYLINE );
    JSONArray polyline = createPolyline();
    drawingParam1.put( polyline );
    return drawingParam1;
  }

  private static JSONArray createPolyline() {
    JSONArray polyline = new JSONArray();
    polyline.put( 0 );
    polyline.put( 1 );
    polyline.put( 5 );
    polyline.put( 5 );
    return polyline;
  }

  private static JSONArray createLineWidthParam() {
    JSONArray drawingParam2 = new JSONArray();
    drawingParam2.put( GCOperationDispatcher.PROP_LINE_WIDTH );
    JSONArray lineWidth = new JSONArray();
    lineWidth.put( 3 );
    drawingParam2.put( lineWidth );
    return drawingParam2;
  }

  private static JSONArray createColorParam() {
    JSONArray drawingParam3 = new JSONArray();
    drawingParam3.put( GCOperationDispatcher.PROP_FOREGROUND );
    JSONArray color = new JSONArray();
    color.put( 50 );
    color.put( 100 );
    color.put( 200 );
    drawingParam3.put( color );
    return drawingParam3;
  }
  
  
  private ClientCanvasTestUtil() {
    // prevent instantiation
  }
}

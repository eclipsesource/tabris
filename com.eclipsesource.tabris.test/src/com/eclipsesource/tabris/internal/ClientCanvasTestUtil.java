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

import org.json.JSONArray;


@SuppressWarnings("restriction")
public class ClientCanvasTestUtil {

  public static final int LINE_WITH = 3;

  public static String createDrawingsWithoutLineWidth() {
    JSONArray drawings = new JSONArray();
    JSONArray polylineParam = createPolylineParam();
    drawings.put( polylineParam );
    JSONArray colorParam = createColorParam();
    drawings.put( colorParam );
    JSONArray polylineParam2 = createPolylineParam();
    drawings.put( polylineParam2 );
    return drawings.toString();
  }

  public static String createDrawings( int lineWidth ) {
    JSONArray drawings = new JSONArray();
    JSONArray polylineParam = createPolylineParam();
    drawings.put( polylineParam );
    JSONArray lineWidthParam = createLineWidthParam( lineWidth );
    drawings.put( lineWidthParam );
    JSONArray colorParam = createColorParam();
    drawings.put( colorParam );
    JSONArray polylineParam2 = createPolylineParam();
    drawings.put( polylineParam2 );
    return drawings.toString();
  }

  private static JSONArray createPolylineParam() {
    JSONArray drawingParam1 = new JSONArray();
    drawingParam1.put( Constants.PROPERTY_POLYLINE );
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

  private static JSONArray createLineWidthParam( int lineWidth ) {
    JSONArray drawingParam2 = new JSONArray();
    drawingParam2.put( Constants.PROPERTY_LINE_WIDTH );
    JSONArray lineWidthArray = new JSONArray();
    lineWidthArray.put( lineWidth );
    drawingParam2.put( lineWidthArray );
    return drawingParam2;
  }

  private static JSONArray createColorParam() {
    JSONArray drawingParam3 = new JSONArray();
    drawingParam3.put( Constants.PROPERTY_FOREGROUND );
    JSONArray color = new JSONArray();
    color.put( 50 );
    color.put( 100 );
    color.put( 200 );
    color.put( 10 ); // alpha
    drawingParam3.put( color );
    return drawingParam3;
  }

  private ClientCanvasTestUtil() {
    // prevent instantiation
  }
}

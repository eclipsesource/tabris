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

import org.eclipse.rap.json.JsonArray;


public class ClientCanvasTestUtil {

  public static final int LINE_WITH = 3;

  public static String createDrawingsWithoutLineWidth() {
    JsonArray drawings = new JsonArray();
    JsonArray polylineParam = createPolylineParam();
    drawings.add( polylineParam );
    JsonArray colorParam = createColorParam();
    drawings.add( colorParam );
    JsonArray polylineParam2 = createPolylineParam();
    drawings.add( polylineParam2 );
    return drawings.toString();
  }

  public static String createDrawings( int lineWidth ) {
    JsonArray drawings = new JsonArray();
    JsonArray polylineParam = createPolylineParam();
    drawings.add( polylineParam );
    JsonArray lineWidthParam = createLineWidthParam( lineWidth );
    drawings.add( lineWidthParam );
    JsonArray colorParam = createColorParam();
    drawings.add( colorParam );
    JsonArray polylineParam2 = createPolylineParam();
    drawings.add( polylineParam2 );
    return drawings.toString();
  }

  private static JsonArray createPolylineParam() {
    JsonArray drawingParam1 = new JsonArray();
    drawingParam1.add( Constants.PROPERTY_PATH );
    JsonArray polyline = createPolyline();
    drawingParam1.add( polyline );
    return drawingParam1;
  }

  private static JsonArray createPolyline() {
    JsonArray polyline = new JsonArray();
    polyline.add( 0 );
    polyline.add( 1 );
    polyline.add( 5 );
    polyline.add( 5 );
    return polyline;
  }

  private static JsonArray createLineWidthParam( int lineWidth ) {
    JsonArray drawingParam2 = new JsonArray();
    drawingParam2.add( Constants.PROPERTY_LINE_WIDTH );
    JsonArray lineWidthArray = new JsonArray();
    lineWidthArray.add( lineWidth );
    drawingParam2.add( lineWidthArray );
    return drawingParam2;
  }

  private static JsonArray createColorParam() {
    JsonArray drawingParam3 = new JsonArray();
    drawingParam3.add( Constants.PROPERTY_FOREGROUND );
    JsonArray color = new JsonArray();
    color.add( 50 );
    color.add( 100 );
    color.add( 200 );
    color.add( 10 ); // alpha
    drawingParam3.add( color );
    return drawingParam3;
  }

  private ClientCanvasTestUtil() {
    // prevent instantiation
  }
}

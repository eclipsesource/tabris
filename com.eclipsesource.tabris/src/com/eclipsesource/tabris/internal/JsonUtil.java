/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
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


public class JsonUtil {

  public static JsonArray createJsonArray( String... proposalsToSend ) {
    JsonArray result = new JsonArray();
    for( String proposal : proposalsToSend ) {
      result.add( proposal );
    }
    return result;
  }

  public static JsonArray createJsonArray( int... values ) {
    JsonArray array = new JsonArray();
    for( int i = 0; i < values.length; i++ ) {
      array.add( values[ i ] );
    }
    return array;
  }

  private JsonUtil() {
    // prevent instantiation
  }

}

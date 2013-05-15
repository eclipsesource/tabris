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

import static org.junit.Assert.assertEquals;

import org.eclipse.rap.json.JsonArray;
import org.junit.Test;


public class JsonUtilTest {

  @Test
  public void testConvertsIntArray() {
    JsonArray jsonArray = JsonUtil.createJsonArray( 0, 1, 2 );

    assertEquals( new JsonArray().add( 0 ).add( 1 ).add( 2 ), jsonArray );
  }

  @Test
  public void testConvertsStringArray() {
    JsonArray jsonArray = JsonUtil.createJsonArray( "foo", "bar" );

    assertEquals( new JsonArray().add( "foo" ).add( "bar" ), jsonArray );
  }
}

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
package com.eclipsesource.tabris.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.junit.Test;


public class AppEventTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullType() {
    new AppEvent( null, mock( JsonObject.class ) );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( AppEvent.class ) );
  }

  @Test
  public void testReturnsNullWhenAcessingNullProperties() {
    AppEvent appEvent = new AppEvent( EventType.PAUSE, null );

    assertNull( appEvent.getProperty( "foo" ) );
  }

  @Test
  public void testReturnsCorrectProperty() {
    JsonObject properties = new JsonObject();
    properties.add( "foo", "bar" );

    AppEvent appEvent = new AppEvent( EventType.PAUSE, properties );

    assertEquals( JsonValue.valueOf( "bar" ), appEvent.getProperty( "foo" ) );
  }
}

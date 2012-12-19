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
package com.eclipsesource.tabris.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class AppEventTest {
  
  @Test( expected = IllegalArgumentException.class )
  @SuppressWarnings("unchecked")
  public void testFailsWithNullType() {
    new AppEvent( null, mock( Map.class ) );
  }
  
  @Test
  public void testReturnsNullWhenAcessingNullProperties() {
    AppEvent appEvent = new AppEvent( EventType.PAUSE, null );
    
    assertNull( appEvent.getProperty( "foo" ) );
  }
  
  @Test
  public void testReturnsCorrectProperty() {
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( "foo", "bar" );
    
    AppEvent appEvent = new AppEvent( EventType.PAUSE, properties );
    
    assertEquals( "bar", appEvent.getProperty( "foo" ) );
  }
}

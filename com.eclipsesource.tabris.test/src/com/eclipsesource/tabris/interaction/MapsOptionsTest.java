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
package com.eclipsesource.tabris.interaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.eclipsesource.tabris.interaction.LaunchOptions.App;


public class MapsOptionsTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutQuery() {
    new MapsOptions( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyQuery() {
    new MapsOptions( "" );
  }
  
  @Test
  public void testWithQuery() {
    MapsOptions options = new MapsOptions( "foo" );
    
    assertSame( App.MAPS, options.getApp() );
    assertEquals( "foo", options.getOptions().get( "query" ) );
  }
  
  @Test
  public void testWithQueryAndZoom() {
    MapsOptions options = new MapsOptions( "foo", 23 );
    
    assertEquals( "23", options.getOptions().get( "zoom" ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithQueryAndNegativeZoom() {
    new MapsOptions( "foo", -1 );
  }
  
  @Test
  public void testWithLatLon() {
    MapsOptions options = new MapsOptions( 23.23, 42.42 );
    
    assertSame( App.MAPS, options.getApp() );
    assertEquals( "23.23", options.getOptions().get( "latitude" ) );
    assertEquals( "42.42", options.getOptions().get( "longitude" ) );
  }
  
  @Test
  public void testWithLatLonAndZoom() {
    MapsOptions options = new MapsOptions( 23.23, 42.42, 23 );
    
    assertEquals( "23", options.getOptions().get( "zoom" ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithLatLonAndNegativeZoom() {
    new MapsOptions( 23.23, 42.42, -23 );
  }
  
}

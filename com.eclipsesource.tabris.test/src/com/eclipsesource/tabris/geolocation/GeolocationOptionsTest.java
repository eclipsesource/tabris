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
package com.eclipsesource.tabris.geolocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class GeolocationOptionsTest {
  
  private GeolocationOptions options;

  @Before
  public void setUp() {
    options = new GeolocationOptions().enableHighAccuracy().setFrequency( 10 ).setMaximumAge( 100 );
  }
  
  @Test
  public void testValues() {
    assertTrue( options.isEnableHighAccuracy() );
    assertEquals( 10, options.getFrequency() );
    assertEquals( 100, options.getMaximumAge() );
  }
  
  @Test
  public void testDefaultValues() {
    GeolocationOptions geolocationOptions = new GeolocationOptions();
    
    assertFalse( geolocationOptions.isEnableHighAccuracy() );
    assertEquals( 10000, geolocationOptions.getFrequency() );
    assertEquals( -1, geolocationOptions.getMaximumAge() );
  }
}

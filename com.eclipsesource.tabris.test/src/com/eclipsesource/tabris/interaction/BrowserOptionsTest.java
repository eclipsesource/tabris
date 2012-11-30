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


public class BrowserOptionsTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutUrl() {
    new BrowserOptions( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithInvalidUrl() {
    new BrowserOptions( "foo" );
  }
  
  @Test
  public void testWithUrl() {
    BrowserOptions options = new BrowserOptions( "http://foo.bar" );
    
    assertSame( App.BROWSER, options.getApp() );
    assertEquals( "http://foo.bar", options.getOptions().get( "url" ) );
  }
}

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


public class PhoneOptionsTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutNumber() {
    new PhoneOptions( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyNumber() {
    new PhoneOptions( "" );
  }
  
  @Test
  public void testWithNumber() {
    PhoneOptions options = new PhoneOptions( "(212) 555 1212" );
    
    assertSame( App.PHONE, options.getApp() );
    assertEquals( "(212) 555 1212", options.getOptions().get( "number" ) );
  }
}

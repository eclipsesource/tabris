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


public class SMSOptionsTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutNumber() {
    new SMSOptions( null, "foo" );
  }
  
  @Test
  public void testFailsNotWithEmptyNumber() {
    new SMSOptions( "", "foo" );
  }
  
  @Test
  public void testFailsNotWithEmptyText() {
    new SMSOptions( "(212) 555 1212", "" );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNoText() {
    new SMSOptions( "(212) 555 1212", null );
  }
  
  @Test
  public void testWithNumberAndText() {
    SMSOptions options = new SMSOptions( "(212) 555 1212", "foo" );
    
    assertSame( App.SMS, options.getApp() );
    assertEquals( "(212) 555 1212", options.getOptions().get( "number" ) );
    assertEquals( "foo", options.getOptions().get( "text" ) );
  }
}

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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Map;

import org.junit.Test;

import com.eclipsesource.tabris.interaction.LaunchOptions.App;


public class LaunchOptionsTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutApp() {
    new LaunchOptions( null );
  }
  
  @Test
  public void testGetApp() {
    LaunchOptions options = new LaunchOptions( App.MAIL );
    
    assertSame( App.MAIL, options.getApp() );
  }
  
  @Test
  public void testAddsOptions() {
    LaunchOptions options = new LaunchOptions( App.MAIL );
    options.add( "foo", "bar" );
    options.add( "foo1", "bar1" );
    
    Map<String, String> optionValues = options.getOptions();
    
    assertEquals( 2, optionValues.size() );
    assertEquals( "bar", optionValues.get( "foo" ) );
    assertEquals( "bar1", optionValues.get( "foo1" ) );
  }
  
  @Test
  public void testOptionsIsSafeCopy() {
    LaunchOptions options = new LaunchOptions( App.MAIL );
    options.add( "foo", "bar" );
    options.add( "foo1", "bar1" );
    
    assertNotSame( options.getOptions(), options.getOptions() );
  }
  
  @Test
  public void testModifyoptionMapDoesNotAffectOriginal() {
    LaunchOptions options = new LaunchOptions( App.MAIL );
    options.add( "foo", "bar" );
    options.add( "foo1", "bar1" );
    
    Map<String, String> optionValues = options.getOptions();
    optionValues.put( "foo2", "bar2" );
    
    assertEquals( 2, options.getOptions().size() );
  }
}

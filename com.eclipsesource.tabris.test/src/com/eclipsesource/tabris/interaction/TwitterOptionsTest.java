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

import org.junit.Test;


public class TwitterOptionsTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutText() {
    new TwitterOptions( null );
  }
  
  @Test
  public void testFailsNotWithEmptyText() {
    new TwitterOptions( "" );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsSetUrlWithNull() {
    TwitterOptions twitterOptions = new TwitterOptions( "My Tweet" );
    
    twitterOptions.setUrl( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithSetInvalidUrl() {
    TwitterOptions twitterOptions = new TwitterOptions( "My Tweet" );
    
    twitterOptions.setUrl( "foo" );
  }
  
  @Test
  public void testSetUrl() {
    TwitterOptions twitterOptions = new TwitterOptions( "My Tweet" );
    
    twitterOptions.setUrl( "http://foo" );
    
    assertEquals( "http://foo", twitterOptions.getOptions().get( "url" ) );
  }
  
}

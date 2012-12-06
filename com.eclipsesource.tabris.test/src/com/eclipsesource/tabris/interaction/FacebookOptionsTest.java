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


public class FacebookOptionsTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutText() {
    new FacebookOptions( null );
  }
  
  @Test
  public void testFailsNotWithEmptyText() {
    new FacebookOptions( "" );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsSetUrlWithNull() {
    FacebookOptions facebookOptions = new FacebookOptions( "My Wall Post" );
    
    facebookOptions.setUrl( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithSetInvalidUrl() {
    FacebookOptions facebookOptions = new FacebookOptions( "My Wall Post" );
    
    facebookOptions.setUrl( "foo" );
  }
  
  @Test
  public void testSetUrl() {
    FacebookOptions facebookOptions = new FacebookOptions( "My Wall Post" );
    
    facebookOptions.setUrl( "http://foo" );
    
    assertEquals( "http://foo", facebookOptions.getOptions().get( "url" ) );
  }
  
}

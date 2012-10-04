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
package com.eclipsesource.tabris.xcallbackurl;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;


public class XCallbackConfigurationTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testNullTarget() {
    new XCallbackConfiguration( null, "action", "source" );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testNullAction() {
    new XCallbackConfiguration( "target", null, "source" );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testNullSource() {
    new XCallbackConfiguration( "target", "action", null );
  }
  
  @Test
  public void testGetTarget() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    
    assertEquals( "target", configuration.getTargetApp() );
  }
  
  @Test
  public void testGetAction() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    
    assertEquals( "action", configuration.getAction() );
  }
  
  @Test
  public void testGetSource() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    
    assertEquals( "source", configuration.getXSource() );
  }
  
  @Test
  public void testAddParameters() {
    XCallbackConfiguration configuration = new XCallbackConfiguration( "target", "action", "source" );
    configuration.addActionParameter( "foo1", "bar1" );
    configuration.addActionParameter( "foo2", "bar2" );
    
    Map<String, String> actionParameters = configuration.getActionParameters();
    assertEquals( 2, actionParameters.size() );
    assertEquals( "bar1", actionParameters.get( "foo1" ) );
    assertEquals( "bar2", actionParameters.get( "foo2" ) );
  }
  
}

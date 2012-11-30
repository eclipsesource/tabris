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
package com.eclipsesource.tabris;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.interaction.AppLauncher;


public class TabrisClientTest {
  
  @Before
  public void setUp() {
    Fixture.setUp();
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testHasAppLauncherService() {
    TabrisClient client = new TabrisClient();
    
    AppLauncher launcher = client.getService( AppLauncher.class );
    
    assertNotNull( launcher );
  }
  
  @Test
  public void testAppLauncherIsSingleton() {
    TabrisClient client = new TabrisClient();
    
    AppLauncher launcher = client.getService( AppLauncher.class );
    
    assertSame( launcher, client.getService( AppLauncher.class ) );
  }
}

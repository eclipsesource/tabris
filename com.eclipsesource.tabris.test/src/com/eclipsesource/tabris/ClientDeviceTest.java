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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.TestRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.ClientDevice.Platform;
import com.eclipsesource.tabris.internal.Constants;


public class ClientDeviceTest {
  
  @Before
  public void setUp() {
    Fixture.setUp();
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testIsAndroid() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_ANDROID );
    
    ClientDevice device = ClientDevice.getCurrent();
    
    assertTrue( device.isPlatform( Platform.ANDROID ) );
  }
  
  @Test
  public void testIsIOS() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS );
    
    ClientDevice device = ClientDevice.getCurrent();
    
    assertTrue( device.isPlatform( Platform.IOS ) );
  }
  
  @Test
  public void testIsWebByDefault() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "Mozilla/bla" );
  
    ClientDevice device = ClientDevice.getCurrent();
    
    assertTrue( device.isPlatform( Platform.WEB ) );
  }

  @Test
  public void testGetPlatformIsAndroid() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_ANDROID );
    ClientDevice device = ClientDevice.getCurrent();
    
    Platform platform = device.getPlatform();
    
    assertSame( Platform.ANDROID, platform );
  }
  
  @Test
  public void testGetPlatformIsIOS() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, Constants.ID_IOS );
    ClientDevice device = ClientDevice.getCurrent();

    Platform platform = device.getPlatform();
    
    assertSame( Platform.IOS, platform );
  }
  
  @Test
  public void testGetPlatformIsWebByDefault() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "Mozilla/bla" );
    ClientDevice device = ClientDevice.getCurrent();
    
    Platform platform = device.getPlatform();
    
    assertSame( Platform.WEB, platform );
  }
  
}

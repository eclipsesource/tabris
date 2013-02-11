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

import org.eclipse.rap.rwt.client.service.ClientInfo;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.event.App;
import com.eclipsesource.tabris.interaction.AppLauncher;
import com.eclipsesource.tabris.test.TabrisTestUtil;


public class TabrisClientTest {

  @Before
  public void setUp() {
    Fixture.setUp();
    TabrisTestUtil.mockRemoteObject();
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
  public void testHasAppService() {
    TabrisClient client = new TabrisClient();

    App app = client.getService( App.class );

    assertNotNull( app );
  }

  @Test
  public void testHasClientDeviceService() {
    TabrisClient client = new TabrisClient();

    ClientDevice device = client.getService( ClientDevice.class );

    assertNotNull( device );
  }

  @Test
  public void testHasClientInfoService() {
    TabrisClient client = new TabrisClient();

    ClientInfo info = client.getService( ClientInfo.class );

    assertNotNull( info );
  }

  @Test
  public void testHasClientStoreService() {
    TabrisClient client = new TabrisClient();

    ClientStore store = client.getService( ClientStore.class );

    assertNotNull( store );
  }

  @Test
  public void testAppLauncherIsSingleton() {
    TabrisClient client = new TabrisClient();

    AppLauncher launcher = client.getService( AppLauncher.class );

    assertSame( launcher, client.getService( AppLauncher.class ) );
  }

  @Test
  public void testAppIsSingleton() {
    TabrisClient client = new TabrisClient();

    App app = client.getService( App.class );

    assertSame( app, client.getService( App.class ) );
  }

  @Test
  public void testClientDeviceIsSingleton() {
    TabrisClient client = new TabrisClient();

    ClientDevice device = client.getService( ClientDevice.class );

    assertSame( device, client.getService( ClientDevice.class ) );
  }

  @Test
  public void testClientInfoIsSingleton() {
    TabrisClient client = new TabrisClient();

    ClientInfo info = client.getService( ClientInfo.class );

    assertSame( info, client.getService( ClientInfo.class ) );
  }

  @Test
  public void testClientStoreIsSingleton() {
    TabrisClient client = new TabrisClient();

    ClientStore store = client.getService( ClientStore.class );

    assertSame( store, client.getService( ClientStore.class ) );
  }
}

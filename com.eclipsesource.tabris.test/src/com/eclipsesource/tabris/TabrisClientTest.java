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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.ServletContext;

import org.eclipse.rap.rwt.client.service.ClientInfo;
import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.eclipse.rap.rwt.internal.client.WidgetDataWhiteList;
import org.eclipse.rap.rwt.internal.lifecycle.PhaseListenerRegistry;
import org.eclipse.rap.rwt.internal.resources.ResourceRegistry;
import org.eclipse.rap.rwt.internal.theme.ThemeManager;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.app.App;
import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.device.ClientDevice;
import com.eclipsesource.tabris.geolocation.Geolocation;
import com.eclipsesource.tabris.interaction.AppLauncher;
import com.eclipsesource.tabris.internal.Constants;
import com.eclipsesource.tabris.internal.TabrisResourceLoader;
import com.eclipsesource.tabris.test.TabrisTestUtil;


@SuppressWarnings("restriction")
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
  public void testHasDataWhitelistService() {
    TabrisClient client = new TabrisClient();

    WidgetDataWhiteList list = client.getService( WidgetDataWhiteList.class );

    assertNotNull( list );
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
  public void testHasCameraService() {
    TabrisClient client = new TabrisClient();

    Camera camera = client.getService( Camera.class );

    assertNotNull( camera );
  }

  @Test
  public void testHasGeolocationService() {
    TabrisClient client = new TabrisClient();

    Geolocation geolocation = client.getService( Geolocation.class );

    assertNotNull( geolocation );
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

  @Test
  public void testCameraIsSingleton() {
    TabrisClient client = new TabrisClient();

    Camera camera = client.getService( Camera.class );
    Camera camera2 = client.getService( Camera.class );

    assertSame( camera, camera2 );
  }

  @Test
  public void testGeolocationIsSingleton() {
    TabrisClient client = new TabrisClient();

    Geolocation geolocation = client.getService( Geolocation.class );
    Geolocation geolocation2 = client.getService( Geolocation.class );

    assertSame( geolocation, geolocation2 );
  }

  @Test
  public void testDataWhitelistIsSingletong() {
    TabrisClient client = new TabrisClient();

    WidgetDataWhiteList list = client.getService( WidgetDataWhiteList.class );
    WidgetDataWhiteList list2 = client.getService( WidgetDataWhiteList.class );

    assertSame( list, list2 );
  }

  @Test
  public void testRegistersTheme() {
    ApplicationImpl application = mockConfiguration();
    TabrisClient client = new TabrisClient();

    client.install( application );

    verify( application ).addStyleSheet( eq( Constants.THEME_ID_ANDROID ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( Constants.THEME_ID_IOS ), anyString(), any( ResourceLoader.class ) );
  }

  @Test
  public void testRegistersTabrisLoader() {
    ApplicationImpl application = mockConfiguration();
    ApplicationContextImpl applicationContext = application.getApplicationContext();
    TabrisClient client = new TabrisClient();

    client.install( application );

    verify( applicationContext.getResourceRegistry() ).add( eq( "index.json" ), any( TabrisResourceLoader.class ) );
  }

  private ApplicationImpl mockConfiguration() {
    ApplicationImpl application = mock( ApplicationImpl.class );
    ApplicationContextImpl context = mock( ApplicationContextImpl.class );
    ThemeManager themeManager = mock( ThemeManager.class );
    when( context.getThemeManager() ).thenReturn( themeManager );
    PhaseListenerRegistry registry = mock( PhaseListenerRegistry.class );
    when( context.getPhaseListenerRegistry() ).thenReturn( registry );
    when( context.getServletContext() ).thenReturn( mock( ServletContext.class ) );
    when( application.getApplicationContext() ).thenReturn( context );
    ResourceRegistry resourceRegistry = mock( ResourceRegistry.class );
    when( context.getResourceRegistry() ).thenReturn( resourceRegistry );
    return application;
  }
}

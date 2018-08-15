/*******************************************************************************
 * Copyright (c) 2012, 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.eclipse.rap.rwt.client.service.ClientInfo;
import org.eclipse.rap.rwt.client.service.StartupParameters;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.ClientStore;
import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.app.App;
import com.eclipsesource.tabris.camera.Camera;
import com.eclipsesource.tabris.camera.PhotoAlbum;
import com.eclipsesource.tabris.device.ClientDevice;
import com.eclipsesource.tabris.geolocation.Geolocation;
import com.eclipsesource.tabris.interaction.AppLauncher;
import com.eclipsesource.tabris.print.Printer;
import com.eclipsesource.tabris.push.CloudPush;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class TabrisClientImplTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Test
  public void testHasAppLauncherService() {
    TabrisClient client = new TabrisClientImpl();

    AppLauncher launcher = client.getService( AppLauncher.class );

    assertNotNull( launcher );
  }

  @Test
  public void testHasAppService() {
    TabrisClient client = new TabrisClientImpl();

    App app = client.getService( App.class );

    assertNotNull( app );
  }

  @Test
  public void testHasClientDeviceService() {
    TabrisClient client = new TabrisClientImpl();

    ClientDevice device = client.getService( ClientDevice.class );

    assertNotNull( device );
  }

  @Test
  public void testHasClientInfoService() {
    TabrisClient client = new TabrisClientImpl();

    ClientInfo info = client.getService( ClientInfo.class );

    assertNotNull( info );
  }

  @Test
  public void testHasClientStoreService() {
    TabrisClient client = new TabrisClientImpl();

    ClientStore store = client.getService( ClientStore.class );

    assertNotNull( store );
  }

  @Test
  public void testHasCameraService() {
    TabrisClient client = new TabrisClientImpl();

    Camera camera = client.getService( Camera.class );

    assertNotNull( camera );
  }

  @Test
  public void testHasPhotoAlbumService() {
    TabrisClient client = new TabrisClientImpl();

    PhotoAlbum album = client.getService( PhotoAlbum.class );

    assertNotNull( album );
  }

  @Test
  public void testHasCloudPushService() {
    TabrisClient client = new TabrisClientImpl();

    CloudPush cloudPush = client.getService( CloudPush.class );

    assertNotNull( cloudPush );
  }

  @Test
  public void testHasGeolocationService() {
    TabrisClient client = new TabrisClientImpl();

    Geolocation geolocation = client.getService( Geolocation.class );

    assertNotNull( geolocation );
  }

  @Test
  public void testAppLauncherIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    AppLauncher launcher = client.getService( AppLauncher.class );

    assertSame( launcher, client.getService( AppLauncher.class ) );
  }

  @Test
  public void testAppIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    App app = client.getService( App.class );

    assertSame( app, client.getService( App.class ) );
  }

  @Test
  public void testClientDeviceIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    ClientDevice device = client.getService( ClientDevice.class );

    assertSame( device, client.getService( ClientDevice.class ) );
  }

  @Test
  public void testCloudPushIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    CloudPush cloudPush = client.getService( CloudPush.class );

    assertSame( cloudPush, client.getService( CloudPush.class ) );
  }

  @Test
  public void testClientInfoIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    ClientInfo info = client.getService( ClientInfo.class );

    assertSame( info, client.getService( ClientInfo.class ) );
  }

  @Test
  public void testClientStoreIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    ClientStore store = client.getService( ClientStore.class );

    assertSame( store, client.getService( ClientStore.class ) );
  }

  @Test
  public void testCameraIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    Camera camera = client.getService( Camera.class );
    Camera camera2 = client.getService( Camera.class );

    assertSame( camera, camera2 );
  }

  @Test
  public void testPhotoAlbumIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    PhotoAlbum album = client.getService( PhotoAlbum.class );
    PhotoAlbum album2 = client.getService( PhotoAlbum.class );

    assertSame( album, album2 );
  }

  @Test
  public void testGeolocationIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    Geolocation geolocation = client.getService( Geolocation.class );
    Geolocation geolocation2 = client.getService( Geolocation.class );

    assertSame( geolocation, geolocation2 );
  }

  @Test
  public void testHasPrintService() {
    TabrisClient client = new TabrisClientImpl();

    Printer print = client.getService( Printer.class );

    assertNotNull( print );
  }

  @Test
  public void testPrintServiceIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    Printer print1 = client.getService( Printer.class );
    Printer print2 = client.getService( Printer.class );

    assertSame( print1, print2 );
  }

  @Test
  public void testHasStartupParametersService() {
    TabrisClient client = new TabrisClientImpl();

    StartupParameters service = client.getService( StartupParameters.class );

    assertNotNull( service );
  }

  @Test
  public void testStartupParametersIsSingleton() {
    TabrisClient client = new TabrisClientImpl();

    StartupParameters service1 = client.getService( StartupParameters.class );
    StartupParameters service2 = client.getService( StartupParameters.class );

    assertSame( service1, service2 );
  }

}

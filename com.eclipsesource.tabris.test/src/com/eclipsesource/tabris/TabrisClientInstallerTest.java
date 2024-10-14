/*******************************************************************************
 * Copyright (c) 2012, 2017 EclipseSource and others.
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.eclipse.rap.rwt.internal.resources.ResourceRegistry;
import org.eclipse.rap.rwt.internal.theme.ThemeManager;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.Constants;
import com.eclipsesource.tabris.internal.DefaultVersionCheck;
import com.eclipsesource.tabris.internal.TabrisResourceLoader;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;

import jakarta.servlet.ServletContext;


@SuppressWarnings("restriction")
public class TabrisClientInstallerTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @After
  public void tearDown() {
    TabrisClientInstaller.VERSION_CHECK = new DefaultVersionCheck();
  }

  @Test
  public void testRegistersTheme() {
    ApplicationImpl application = mockConfiguration();

    TabrisClientInstaller.install( application );

    verify( application ).addStyleSheet( eq( Constants.THEME_ID_ANDROID ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( Constants.THEME_ID_IOS ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( Constants.THEME_ID_WINDOWS ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( Constants.THEME_ID_SWT ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( RWT.DEFAULT_THEME_ID ), anyString(), any( ResourceLoader.class ) );
  }

  @Test
  public void testRegistersTabrisLoader() {
    ApplicationImpl application = mockConfiguration();
    ApplicationContextImpl applicationContext = application.getApplicationContext();

    TabrisClientInstaller.install( application );

    verify( applicationContext.getResourceRegistry() ).add( eq( "index.json" ), any( TabrisResourceLoader.class ) );
  }

  @Test
  public void testRegistersThemeWithId() {
    ApplicationImpl application = mockConfiguration();

    TabrisClientInstaller.install( application, "foo" );

    verify( application ).addStyleSheet( eq( Constants.THEME_ID_ANDROID ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( Constants.THEME_ID_IOS ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( Constants.THEME_ID_WINDOWS ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( Constants.THEME_ID_SWT ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( RWT.DEFAULT_THEME_ID ), anyString(), any( ResourceLoader.class ) );
  }

  @Test
  public void testRegistersTabrisLoaderWithId() {
    ApplicationImpl application = mockConfiguration();
    ApplicationContextImpl applicationContext = application.getApplicationContext();

    TabrisClientInstaller.install( application, "foo" );

    verify( applicationContext.getResourceRegistry() ).add( eq( "index.json" ), any( TabrisResourceLoader.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() {
    ApplicationImpl application = mockConfiguration();

    TabrisClientInstaller.install( application, "" );
  }

  private ApplicationImpl mockConfiguration() {
    ApplicationImpl application = mock( ApplicationImpl.class );
    ApplicationContextImpl context = mock( ApplicationContextImpl.class );
    ThemeManager themeManager = mock( ThemeManager.class );
    when( context.getThemeManager() ).thenReturn( themeManager );
    when( context.getServletContext() ).thenReturn( mock( ServletContext.class ) );
    when( application.getApplicationContext() ).thenReturn( context );
    ResourceRegistry resourceRegistry = mock( ResourceRegistry.class );
    when( context.getResourceRegistry() ).thenReturn( resourceRegistry );
    return application;
  }

  @Test
  public void testSetsVersionCheck() {
    VersionCheck check = mock( VersionCheck.class );

    TabrisClientInstaller.setVersionCheck( check );

    assertSame( check, TabrisClientInstaller.VERSION_CHECK );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullVersionCheck() {
    TabrisClientInstaller.setVersionCheck( null );
  }

  @Test
  public void testHasDefaultVersionCheckByDefault() {
    assertTrue( TabrisClientInstaller.VERSION_CHECK instanceof DefaultVersionCheck );
  }
}

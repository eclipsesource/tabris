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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.ServletContext;

import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.eclipse.rap.rwt.internal.lifecycle.PhaseListenerRegistry;
import org.eclipse.rap.rwt.internal.resources.ResourceRegistry;
import org.eclipse.rap.rwt.internal.theme.ThemeManager;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.Constants;
import com.eclipsesource.tabris.internal.TabrisResourceLoader;
import com.eclipsesource.tabris.test.TabrisTestUtil;


@SuppressWarnings("restriction")
public class TabrisClientInstallerTest {

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
  public void testRegistersTheme() {
    ApplicationImpl application = mockConfiguration();

    TabrisClientInstaller.install( application );

    verify( application ).addStyleSheet( eq( Constants.THEME_ID_ANDROID ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( Constants.THEME_ID_IOS ), anyString(), any( ResourceLoader.class ) );
  }

  @Test
  public void testRegistersTabrisLoader() {
    ApplicationImpl application = mockConfiguration();
    ApplicationContextImpl applicationContext = application.getApplicationContext();

    TabrisClientInstaller.install( application );

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

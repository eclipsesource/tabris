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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.internal.application.ApplicationContextImpl;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.eclipse.rap.rwt.internal.lifecycle.PhaseListenerRegistry;
import org.eclipse.rap.rwt.internal.theme.ThemeManager;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.eclipsesource.tabris.internal.bootstrap.HttpServiceTracker;


@SuppressWarnings("restriction")
public class BootstrapperTest {
  
  private ApplicationImpl application;
  private PhaseListenerRegistry registry;
  private ThemeManager themeManager;
  private Bootstrapper bootstrapper;

  @Before
  public void setUp() {
    application = mockConfiguration();
    bootstrapper = new Bootstrapper( application );
  }
  
  private ApplicationImpl mockConfiguration() {
    ApplicationImpl application = mock( ApplicationImpl.class );
    ApplicationContextImpl context = mock( ApplicationContextImpl.class );
    themeManager = mock( ThemeManager.class );
    when( context.getThemeManager() ).thenReturn( themeManager );
    registry = mock( PhaseListenerRegistry.class );
    when( context.getPhaseListenerRegistry() ).thenReturn( registry );
    when( application.getApplicationContext() ).thenReturn( context );
    return application;
  }

  @Test
  public void testRegistersTheme() {
    bootstrapper.bootstrap();
    
    verify( application ).addStyleSheet( eq( Bootstrapper.THEME_ID_ANDROID ), anyString(), any( ResourceLoader.class ) );
    verify( application ).addStyleSheet( eq( Bootstrapper.THEME_ID_IOS ), anyString(), any( ResourceLoader.class ) );
  }
  
  @Test
  public void testOpensHttpTrackerAndRegistersLookupServlet() {
    Bundle bundle = mockBundle();
    
    bootstrapper.registerEntryPointLookup( bundle, "/index.json" );

    HttpServiceTracker tracker = bootstrapper.getHttpServiceTracker();
    assertEquals( "/index.json", tracker.getPath() );
  }
  
  private Bundle mockBundle() {
    Bundle bundle = mock( Bundle.class );
    BundleContext bundleContext = mock( BundleContext.class );
    when( bundle.getBundleContext() ).thenReturn( bundleContext );
    return bundle;
  }
}

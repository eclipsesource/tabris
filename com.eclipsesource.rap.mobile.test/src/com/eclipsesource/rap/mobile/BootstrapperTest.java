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
package com.eclipsesource.rap.mobile;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.rwt.application.ApplicationConfiguration;
import org.eclipse.rwt.internal.application.ApplicationContext;
import org.eclipse.rwt.internal.application.ApplicationImpl;
import org.eclipse.rwt.internal.lifecycle.PhaseListenerRegistry;
import org.eclipse.rwt.internal.theme.Theme;
import org.eclipse.rwt.internal.theme.ThemeManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.rap.mobile.internal.bootstrap.ProxyApplicationConfiguration;
import com.eclipsesource.rap.mobile.internal.bootstrap.ThemePhaseListener;


@SuppressWarnings("restriction")
public class BootstrapperTest {
  
  private ApplicationConfiguration configuration;
  private ApplicationImpl application;
  private PhaseListenerRegistry registry;
  private ThemeManager themeManager;

  @Before
  public void setUp() {
    configuration = mock( ProxyApplicationConfiguration.class );
    application = mockConfiguration();
  }
  
  @Test
  public void testRegistersTheme() {
    Bootstrapper.bootstrap( application );
    
    verify( application ).addStyleSheet( eq( Bootstrapper.THEME_ID_ANDROID ), anyString() );
    verify( application ).addStyleSheet( eq( Bootstrapper.THEME_ID_IOS ), anyString() );
  }
  
  @Test
  public void testRegistersPhaseListener() {
    Bootstrapper.bootstrap( application );
    
    verify( application ).addPhaseListener( any( ThemePhaseListener.class ) );
  }
  
  @Test
  public void testRegistersPhaseListenerWithWrapper() {
    ApplicationConfiguration original = mock( ApplicationConfiguration.class );
    when( application.getAdapter( ApplicationConfiguration.class ) ).thenReturn( original );
    
    Bootstrapper.bootstrap( application );
    
    verify( registry ).add( any( ThemePhaseListener.class ) );
  }
  
  @Test
  public void testRegistersThemesWithWrapper() {
    ApplicationConfiguration original = mock( ApplicationConfiguration.class );
    when( application.getAdapter( ApplicationConfiguration.class ) ).thenReturn( original );
    
    Bootstrapper.bootstrap( application );
    
    ArgumentCaptor<Theme> captor = ArgumentCaptor.forClass( Theme.class );
    verify( themeManager, times( 2 ) ).registerTheme( captor.capture() );
    List<Theme> values = captor.getAllValues();
    assertEquals( Bootstrapper.THEME_ID_ANDROID, values.get( 0 ).getId() );
    assertEquals( Bootstrapper.THEME_ID_IOS, values.get( 1 ).getId() );
  }
  
  private ApplicationImpl mockConfiguration() {
    ApplicationImpl application = mock( ApplicationImpl.class );
    ApplicationContext context = mock( ApplicationContext.class );
    themeManager = mock( ThemeManager.class );
    when( context.getThemeManager() ).thenReturn( themeManager );
    registry = mock( PhaseListenerRegistry.class );
    when( context.getPhaseListenerRegistry() ).thenReturn( registry );
    when( application.getAdapter( ApplicationContext.class ) ).thenReturn( context );
    when( application.getAdapter( ApplicationConfiguration.class ) ).thenReturn( configuration );
    return application;
  }
}

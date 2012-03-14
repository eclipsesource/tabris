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
package com.eclipsesource.rap.mobile.internal.bootstrap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rwt.application.ApplicationConfiguration;
import org.eclipse.rwt.application.ApplicationConfigurator;
import org.eclipse.rwt.internal.application.ApplicationConfigurationImpl;
import org.eclipse.rwt.internal.application.ApplicationContext;
import org.eclipse.rwt.internal.lifecycle.PhaseListenerRegistry;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.rap.mobile.Bootstrapper;


@SuppressWarnings("restriction")
public class ProxyApplicationConfiguratorTest {
  
  private ApplicationConfigurator original;
  private ProxyApplicationConfigurator configurator;
  private ApplicationConfiguration configuration;

  @Before
  public void setUp() {
    original = mock( ApplicationConfigurator.class );
    configurator = new ProxyApplicationConfigurator( original );
    configuration = mockConfiguration();
  }
  
  private ApplicationConfigurationImpl mockConfiguration() {
    ApplicationConfigurationImpl configuration = mock( ApplicationConfigurationImpl.class );
    ApplicationContext context = mock( ApplicationContext.class );
    PhaseListenerRegistry registry = mock( PhaseListenerRegistry.class );
    when( context.getPhaseListenerRegistry() ).thenReturn( registry );
    when( configuration.getAdapter( ApplicationContext.class ) ).thenReturn( context );
    when( configuration.getAdapter( ApplicationConfigurator.class ) ).thenReturn( configurator );
    return configuration;
  }

  @Test
  public void testDelegatesConfigure() {
    configurator.configure( configuration );
    
    verify( original ).configure( any( ConfigurationWrapper.class ) );
  }
  
  @Test
  public void testRegistersThemes() {
    configurator.configure( configuration );
    
    verify( configuration ).addStyleSheet( eq( Bootstrapper.THEME_ID_ANDROID ), anyString() );
    verify( configuration ).addStyleSheet( eq( Bootstrapper.THEME_ID_IOS ), anyString() );
  }
  
  @Test
  public void testRegistersPhaseListener() {
    configurator.configure( configuration );
    
    verify( configuration ).addPhaseListener( any( ThemePhaseListener.class ) );
  }
}

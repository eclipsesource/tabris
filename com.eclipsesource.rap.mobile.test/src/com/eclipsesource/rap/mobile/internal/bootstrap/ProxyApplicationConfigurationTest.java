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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rwt.application.Application;
import org.eclipse.rwt.application.ApplicationConfiguration;
import org.eclipse.rwt.internal.application.ApplicationContext;
import org.eclipse.rwt.internal.application.ApplicationImpl;
import org.eclipse.rwt.internal.lifecycle.PhaseListenerRegistry;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.eclipsesource.rap.mobile.Bootstrapper;


@SuppressWarnings("restriction")
public class ProxyApplicationConfigurationTest {
  
  private ApplicationConfiguration original;
  private ProxyApplicationConfiguration configuration;
  private Application application;

  @Before
  public void setUp() {
    original = mock( ApplicationConfiguration.class );
    ProxyApplicationConfiguration originalProxy = new ProxyApplicationConfiguration( original );
    configuration = spy( originalProxy );
    mockBundle();
    application = mockConfiguration();
  }

  private void mockBundle() {
    Bundle bundle = mock( Bundle.class );
    BundleContext bundleContext = mock( BundleContext.class );
    when( bundle.getBundleContext() ).thenReturn( bundleContext );
    doReturn( bundle ).when( configuration ).getBundle();
  }
  
  private ApplicationImpl mockConfiguration() {
    ApplicationImpl application = mock( ApplicationImpl.class );
    ApplicationContext context = mock( ApplicationContext.class );
    PhaseListenerRegistry registry = mock( PhaseListenerRegistry.class );
    when( context.getPhaseListenerRegistry() ).thenReturn( registry );
    when( application.getAdapter( ApplicationContext.class ) ).thenReturn( context );
    when( application.getAdapter( ApplicationConfiguration.class ) ).thenReturn( configuration );
    return application;
  }

  @Test
  public void testDelegatesConfigure() {
    configuration.configure( application );
    
    verify( original ).configure( any( ApplicationWrapper.class ) );
  }
  
  @Test
  public void testOpensTracker() {
    configuration.configure( application );
    
    verify( configuration ).registerEntryPointLookup( application );
  }
  
  @Test
  public void testRegistersThemes() {
    configuration.configure( application );
    
    verify( application ).addStyleSheet( eq( Bootstrapper.THEME_ID_ANDROID ), anyString() );
    verify( application ).addStyleSheet( eq( Bootstrapper.THEME_ID_IOS ), anyString() );
  }
  
  @Test
  public void testRegistersPhaseListener() {
    configuration.configure( application );
    
    verify( application ).addPhaseListener( any( ThemePhaseListener.class ) );
  }
}

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

import org.eclipse.rwt.application.ApplicationConfiguration;
import org.eclipse.rwt.application.ApplicationConfigurator;
import org.eclipse.rwt.internal.application.ApplicationConfigurationImpl;
import org.eclipse.rwt.internal.application.ApplicationContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.eclipsesource.rap.mobile.Bootstrapper;

@SuppressWarnings("restriction")
public class ProxyApplicationConfigurator implements ApplicationConfigurator {
  
  private final ApplicationConfigurator original;
  private HttpServiceTracker httpServiceTracker;

  public ProxyApplicationConfigurator( ApplicationConfigurator original ) {
    this.original = original;
  }

  public void configure( ApplicationConfiguration configuration ) {
    Bootstrapper.bootstrap( configuration );
    configureOriginal( configuration );
    registerEntryPointLookup( configuration );
  }

  private void configureOriginal( ApplicationConfiguration configuration ) {
    ConfigurationWrapper configurationWrapper 
      = new ConfigurationWrapper( ( ApplicationConfigurationImpl )configuration, original );
    original.configure( configurationWrapper );
  }
  
  void registerEntryPointLookup( ApplicationConfiguration configuration ) {
    ApplicationContext context 
      = ( ( ApplicationConfigurationImpl )configuration ).getAdapter( ApplicationContext.class );
    Bundle bundle = getBundle();
    BundleContext bundleContext = bundle.getBundleContext();
    httpServiceTracker = new HttpServiceTracker( bundleContext, context );
    httpServiceTracker.open();
  }

  public void unregister() {
    if( httpServiceTracker != null ) {
      httpServiceTracker.close();
    }
  }

  // For testing purpose
  Bundle getBundle() {
    return FrameworkUtil.getBundle( getClass() );
  }

}

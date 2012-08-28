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
package com.eclipsesource.tabris.internal.bootstrap;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.internal.application.ApplicationContext;
import org.eclipse.rap.rwt.internal.application.ApplicationImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.eclipsesource.tabris.Bootstrapper;

@SuppressWarnings("restriction")
public class ProxyApplicationConfiguration implements ApplicationConfiguration {
  
  private final ApplicationConfiguration original;
  private HttpServiceTracker httpServiceTracker;

  public ProxyApplicationConfiguration( ApplicationConfiguration original ) {
    this.original = original;
  }

  public void configure( Application application ) {
    Bootstrapper.bootstrap( application );
    configureOriginal( application );
    registerEntryPointLookup( application );
  }

  private void configureOriginal( Application configuration ) {
    ApplicationWrapper configurationWrapper 
      = new ApplicationWrapper( ( ApplicationImpl )configuration, original );
    original.configure( configurationWrapper );
  }
  
  void registerEntryPointLookup( Application configuration ) {
    ApplicationContext context 
      = ( ( ApplicationImpl )configuration ).getAdapter( ApplicationContext.class );
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

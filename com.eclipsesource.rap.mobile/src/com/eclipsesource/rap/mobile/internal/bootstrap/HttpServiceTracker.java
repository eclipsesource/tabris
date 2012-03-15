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

import org.eclipse.rwt.internal.application.ApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;



@SuppressWarnings("restriction")
public class HttpServiceTracker implements ServiceTrackerCustomizer<HttpService, HttpService>{
  
  static final String SERVLET_ALIAS = "/com.eclipsesource.rap.mobile/info.json";
  
  private ServiceTracker<HttpService, HttpService> httpServiceTracker;
  private BundleContext bundleContext;
  private ApplicationContext context;

  public HttpServiceTracker( BundleContext bundleContext, ApplicationContext context ) {
    this.bundleContext = bundleContext;
    this.context = context;
    httpServiceTracker = new ServiceTracker<HttpService, HttpService>( bundleContext, 
                                                                       HttpService.class.getName(), 
                                                                       this );
  }

  public HttpService addingService( ServiceReference<HttpService> reference ) {
    HttpService httpService = bundleContext.getService( reference );
    try {
      httpService.registerServlet( SERVLET_ALIAS, 
                                   new EntryPointLookupServlet( context ), 
                                   null, 
                                   null );
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
    return httpService;
  }
  
  void open() {
    getTracker().open();
  }

  void close() {
    getTracker().close();
  }
  
  public void modifiedService( ServiceReference<HttpService> reference, HttpService service ) {
    // do nothing
  }

  public void removedService( ServiceReference<HttpService> reference, HttpService service ) {
    service.unregister( SERVLET_ALIAS );
  }
  
  // For testing purpose
  ServiceTracker<HttpService, HttpService> getTracker() {
    return httpServiceTracker;
  }
}

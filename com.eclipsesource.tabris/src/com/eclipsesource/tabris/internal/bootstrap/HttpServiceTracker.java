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

import org.eclipse.rap.rwt.internal.application.ApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;



@SuppressWarnings("restriction")
public class HttpServiceTracker implements ServiceTrackerCustomizer<HttpService, HttpService>{
  
  private final ServiceTracker<HttpService, HttpService> httpServiceTracker;
  private final BundleContext bundleContext;
  private final ApplicationContext context;

  private final String path;

  public HttpServiceTracker( BundleContext bundleContext, ApplicationContext context, String path ) {
    this.bundleContext = bundleContext;
    this.context = context;
    this.path = path;
    httpServiceTracker = new ServiceTracker<HttpService, HttpService>( bundleContext, 
                                                                       HttpService.class.getName(), 
                                                                       this );
  }

  @Override
  public HttpService addingService( ServiceReference<HttpService> reference ) {
    HttpService httpService = bundleContext.getService( reference );
    try {
      httpService.registerServlet( path, 
                                   new EntryPointLookupServlet( context ), 
                                   null, 
                                   null );
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
    return httpService;
  }
  
  public void open() {
    getTracker().open();
  }

  void close() {
    getTracker().close();
  }
  
  @Override
  public void modifiedService( ServiceReference<HttpService> reference, HttpService service ) {
    // do nothing
  }

  @Override
  public void removedService( ServiceReference<HttpService> reference, HttpService service ) {
    service.unregister( path );
  }
  
  public String getPath() {
    return path;
  }

  // For testing purpose
  ServiceTracker<HttpService, HttpService> getTracker() {
    return httpServiceTracker;
  }
}

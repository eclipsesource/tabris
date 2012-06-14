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

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;

import javax.servlet.ServletException;

import org.eclipse.rwt.internal.application.ApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

import com.eclipsesource.tabris.internal.bootstrap.EntryPointLookupServlet;
import com.eclipsesource.tabris.internal.bootstrap.HttpServiceTracker;


@SuppressWarnings("restriction")
public class HttpServiceTrackerTest {
  
  private HttpServiceTracker tracker;
  private BundleContext bundleContext;

  @Before
  public void setUp() {
    bundleContext = mock( BundleContext.class );
    ApplicationContext context = mock( ApplicationContext.class );
    tracker = new HttpServiceTracker( bundleContext, context );
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void testRegistersServlet() throws ServletException, NamespaceException {
    ServiceReference<HttpService> reference = mock( ServiceReference.class );
    HttpService service = mock( HttpService.class );
    when( bundleContext.getService( reference ) ).thenReturn( service );
    
    tracker.addingService( reference );
    
    verify( service ).registerServlet( eq( HttpServiceTracker.SERVLET_ALIAS ), 
                                       any( EntryPointLookupServlet.class ), 
                                       any( Dictionary.class ), 
                                       any( HttpContext.class ) );
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void testReturnsService() {
    ServiceReference<HttpService> reference = mock( ServiceReference.class );
    HttpService service = mock( HttpService.class );
    when( bundleContext.getService( reference ) ).thenReturn( service );
    
    HttpService actualService = tracker.addingService( reference );
    
    assertSame( service, actualService );
  }
  
  @Test
  public void testOpensTracker() {
    HttpServiceTracker spyTracker = spy( tracker );
    ServiceTracker serviceTracker = mock( ServiceTracker.class );
    doReturn( serviceTracker ).when( spyTracker ).getTracker();
    
    spyTracker.open();
    
    verify( serviceTracker ).open();
  }
  
  @Test
  public void testClosesTracker() {
    HttpServiceTracker spyTracker = spy( tracker );
    ServiceTracker serviceTracker = mock( ServiceTracker.class );
    doReturn( serviceTracker ).when( spyTracker ).getTracker();
    
    spyTracker.close();
    
    verify( serviceTracker ).close();
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void testUnregistersServlet() {
    ServiceReference<HttpService> reference = mock( ServiceReference.class );
    HttpService service = mock( HttpService.class );
    
    tracker.removedService( reference, service );
    
    verify( service ).unregister( HttpServiceTracker.SERVLET_ALIAS );
  }
  
}

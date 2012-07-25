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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rwt.application.ApplicationConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.service.ListenerHook.ListenerInfo;


@RunWith( MockitoJUnitRunner.class )
@SuppressWarnings("unchecked")
public class ConfigurationHookTest {
  
  @Mock private BundleContext context;
  @Mock private BundleContext rwtContext;
  private ConfigurationHook hook;
  
  @Before
  public void setUp() {
    ConfigurationHook original = new ConfigurationHook( context );
    hook = spy( original );
    doReturn( rwtContext ).when( hook ).getRWTOSGiBundleContext();
  }
  
  @Test
  public void testUngetsIrrelevantServices() {
    Map<BundleContext, Collection<ListenerInfo>> listeners = createListeners();
    ServiceReference reference = mock( ServiceReference.class );
    ServiceEvent serviceEvent = new ServiceEvent( ServiceEvent.REGISTERED, reference );
    Object service = mock( Object.class );
    when( context.getService( reference ) ).thenReturn( service );
    
    hook.event( serviceEvent, listeners );
    
    verify( context ).ungetService( reference );
  }
  
  @Test
  public void testRegistersProxyAndRemovesRWTContext() {
    Map<BundleContext, Collection<ListenerInfo>> listeners = createListeners();
    ServiceReference reference = mock( ServiceReference.class );
    ServiceEvent serviceEvent = new ServiceEvent( ServiceEvent.REGISTERED, reference );
    Object service = mock( ApplicationConfiguration.class );
    when( context.getService( reference ) ).thenReturn( service );
    
    hook.event( serviceEvent, listeners );
    
    assertEquals( 0, listeners.size() );
    verify( context ).registerService( eq( ApplicationConfiguration.class.getName() ), 
                                       any( ProxyApplicationConfiguration.class ), 
                                       any( Dictionary.class ) );
  }
  
  @Test
  public void testRegistersProxyWithOriginalProperties() {
    Map<BundleContext, Collection<ListenerInfo>> listeners = createListeners();
    ServiceReference reference = mock( ServiceReference.class );
    when( reference.getPropertyKeys() ).thenReturn( new String[] { "test" } );
    when( reference.getProperty( eq( "test" ) ) ).thenReturn( "test" );
    ServiceEvent serviceEvent = new ServiceEvent( ServiceEvent.REGISTERED, reference );
    Object service = mock( ApplicationConfiguration.class );
    when( context.getService( reference ) ).thenReturn( service );
    
    hook.event( serviceEvent, listeners );

    ArgumentCaptor<Dictionary> captor = ArgumentCaptor.forClass( Dictionary.class );
    verify( context ).registerService( eq( ApplicationConfiguration.class.getName() ), 
                                       any( ProxyApplicationConfiguration.class ), 
                                       captor.capture() );
    assertEquals( 1, captor.getValue().size() );
    assertEquals( "test", captor.getValue().get( "test" ) );
  }
  
  @Test
  public void testDoesNotRegistersProxyAndRemovesRWTContextForProxy() {
    Map<BundleContext, Collection<ListenerInfo>> listeners = createListeners();
    ServiceReference reference = mock( ServiceReference.class );
    ServiceEvent serviceEvent = new ServiceEvent( ServiceEvent.REGISTERED, reference );
    Object service = mock( ProxyApplicationConfiguration.class );
    when( context.getService( reference ) ).thenReturn( service );
    
    hook.event( serviceEvent, listeners );
    
    assertEquals( 1, listeners.size() );
    verify( context, never() ).registerService( eq( ApplicationConfiguration.class.getName() ), 
                                                any( ProxyApplicationConfiguration.class ), 
                                                any( Dictionary.class ) );
  }
  
  @Test
  public void testUnregistersProxy() {
    Map<BundleContext, Collection<ListenerInfo>> listeners = createListeners();
    ServiceReference reference = mock( ServiceReference.class );
    ServiceEvent registerEvent = new ServiceEvent( ServiceEvent.REGISTERED, reference );
    ServiceEvent unregisterEvent = new ServiceEvent( ServiceEvent.UNREGISTERING, reference );
    Object service = mock( ApplicationConfiguration.class );
    when( context.getService( reference ) ).thenReturn( service );
    ServiceRegistration registration = mock( ServiceRegistration.class );
    when( context.registerService( anyString(), 
                                   any( Object.class ), 
                                   any( Dictionary.class ) ) ).thenReturn( registration );
    
    hook.event( registerEvent, listeners );
    hook.event( unregisterEvent, listeners );
    
    verify( registration ).unregister();
    verify( context ).ungetService( reference );
  }
  
  @Test
  public void testDoesNotUnregistersProxyForProxy() {
    Map<BundleContext, Collection<ListenerInfo>> listeners = createListeners();
    ServiceReference reference = mock( ServiceReference.class );
    ServiceEvent registerEvent = new ServiceEvent( ServiceEvent.REGISTERED, reference );
    ServiceEvent unregisterEvent = new ServiceEvent( ServiceEvent.UNREGISTERING, reference );
    Object service = mock( ProxyApplicationConfiguration.class );
    when( context.getService( reference ) ).thenReturn( service );
    ServiceRegistration registration = mock( ServiceRegistration.class );
    when( context.registerService( anyString(), 
                                   any( Object.class ), 
                                   any( Dictionary.class ) ) ).thenReturn( registration );
    
    hook.event( registerEvent, listeners );
    hook.event( unregisterEvent, listeners );
    
    verify( registration, never() ).unregister();
  }
  
  
  @Test
  public void testFiltersPlainConfigurators() {
    ServiceReference plainReference = mock( ServiceReference.class );
    ServiceReference proxyReference = mock( ServiceReference.class );
    ApplicationConfiguration plain = mock( ApplicationConfiguration.class );
    ApplicationConfiguration proxy = mock( ProxyApplicationConfiguration.class );
    when( context.getService( plainReference ) ).thenReturn( plain );
    when( context.getService( proxyReference ) ).thenReturn( proxy );
    Collection<ServiceReference<?>> references = new ArrayList<ServiceReference<?>>();
    references.add( plainReference );
    references.add( proxyReference );
    Bundle bundle = mock( Bundle.class );
    when( bundle.getSymbolicName() ).thenReturn( ConfigurationHook.RWT_OSGI_ID );
    when( rwtContext.getBundle() ).thenReturn( bundle );
    
    hook.find( rwtContext, ApplicationConfiguration.class.getName(), null, true, references );
    
    assertFalse( references.contains( plainReference ) );
  }

  private Map<BundleContext, Collection<ListenerInfo>> createListeners() {
    Map<BundleContext, Collection<ListenerInfo>> listeners = new HashMap<BundleContext, Collection<ListenerInfo>>();
    listeners.put( rwtContext, new ArrayList<ListenerInfo>() );
    return listeners;
  }
  
}

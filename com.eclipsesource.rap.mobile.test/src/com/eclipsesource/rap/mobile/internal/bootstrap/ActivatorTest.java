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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.service.EventListenerHook;
import org.osgi.framework.hooks.service.FindHook;


@SuppressWarnings("unchecked")
@RunWith( MockitoJUnitRunner.class )
public class ActivatorTest {
  
  private Activator activator;
  @Mock private BundleContext context;

  @Before
  public void setUp() {
    activator = new Activator();
  }
  
  @Test
  public void testRegisterServices() throws Exception {
    activator.start( context );
    
    InOrder order = inOrder( context );
    order.verify( context ).registerService( eq( FindHook.class.getName() ), 
                                             any( ConfiguratorHook.class ), 
                                             any( Dictionary.class ) );
    order.verify( context ).registerService( eq( EventListenerHook.class.getName() ), 
                                             any( ConfiguratorHook.class ), 
                                             any( Dictionary.class ) );
  }
  
  @Test
  public void testUnregistersServices() throws Exception {
    ServiceRegistration findRegistration = createFindRegistration();
    ServiceRegistration eventRegistration = createEventListenerRegistration();
    activator.start( context );

    activator.stop( context );
    
    verify( findRegistration ).unregister();
    verify( eventRegistration ).unregister();
  }

  private ServiceRegistration createFindRegistration() {
    ServiceRegistration findRegistration = mock( ServiceRegistration.class );
    when( context.registerService( eq( FindHook.class.getName() ), 
                                   any( ConfiguratorHook.class ), 
                                   any( Dictionary.class ) ) ).thenReturn( findRegistration );
    return findRegistration;
  }

  private ServiceRegistration createEventListenerRegistration() {
    ServiceRegistration eventRegistration = mock( ServiceRegistration.class );
    when( context.registerService( eq( EventListenerHook.class.getName() ), 
                                   any( ConfiguratorHook.class ), 
                                   any( Dictionary.class ) ) ).thenReturn( eventRegistration );
    return eventRegistration;
  }
}

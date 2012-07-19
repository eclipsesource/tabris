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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.osgi.ApplicationLauncher;
import org.eclipse.rwt.application.ApplicationConfiguration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.service.EventListenerHook;
import org.osgi.framework.hooks.service.FindHook;
import org.osgi.framework.hooks.service.ListenerHook.ListenerInfo;


public class ConfigurationHook implements EventListenerHook, FindHook {
  
  static final String RWT_OSGI_ID = "org.eclipse.rap.rwt.osgi";
  private final BundleContext bundleContext;
  private final Map<ApplicationConfiguration, ProxyApplicationConfiguration> proxyMap;
  private final Map<ProxyApplicationConfiguration, ServiceRegistration<?>> proxyRegistrationMap;

  public ConfigurationHook( BundleContext context ) {
    bundleContext = context;
    proxyMap = new HashMap<ApplicationConfiguration, ProxyApplicationConfiguration>();
    proxyRegistrationMap = new HashMap<ProxyApplicationConfiguration, ServiceRegistration<?>>();
  }

  public void event( ServiceEvent event, Map<BundleContext, Collection<ListenerInfo>> listeners ) {
    Object service = bundleContext.getService( event.getServiceReference() );
    if( isPlainConfiguration( service ) ) {
      dispatchEvent( event, service );
      listeners.remove( getRWTOSGiBundleContext() );
    } else {
      bundleContext.ungetService( event.getServiceReference() );
    }
  }

  private void dispatchEvent( ServiceEvent event, Object service ) {
    if( event.getType() == ServiceEvent.REGISTERED ) {
      registerProxy( ( ApplicationConfiguration ) service );
    } else if( event.getType() == ServiceEvent.UNREGISTERING ) {
      unregisterProxy( ( ApplicationConfiguration )service );
      bundleContext.ungetService( event.getServiceReference() );
    }
  }

  private void registerProxy( ApplicationConfiguration service ) {
    ProxyApplicationConfiguration proxy = new ProxyApplicationConfiguration( service );
    proxyMap.put( service, proxy );
    ServiceRegistration<?> registration 
      = bundleContext.registerService( ApplicationConfiguration.class.getName(), proxy, null );
    proxyRegistrationMap.put( proxy, registration );
  }

  private void unregisterProxy( ApplicationConfiguration service ) {
    ProxyApplicationConfiguration proxy = proxyMap.remove( service );
    if( proxy != null ) {
      unregisterService( proxy );
      proxy.unregister();
    }
  }

  private void unregisterService( ProxyApplicationConfiguration proxy ) {
    ServiceRegistration<?> registration = proxyRegistrationMap.remove( proxy );
    if( registration != null ) {
      registration.unregister();
    }
  }

  public void find( BundleContext context,
                    String name,
                    String filter,
                    boolean allServices,
                    Collection<ServiceReference<?>> references )
  {
    
    if( name != null && name.equals( ApplicationConfiguration.class.getName() ) ) {
      if( context.getBundle().getSymbolicName().equals( RWT_OSGI_ID ) ) {
        ArrayList<ServiceReference<?>> serviceReferences = new ArrayList<ServiceReference<?>>( references );
        for( ServiceReference<?> serviceReference : serviceReferences ) {
          removeApplicationConfigurations( references, serviceReference );
        }
      }
    }
  }

  private void removeApplicationConfigurations( Collection<ServiceReference<?>> references,
                                               ServiceReference<?> serviceReference )
  {
    Object service = bundleContext.getService( serviceReference );
    if( isPlainConfiguration( service ) ) {
      references.remove( serviceReference );
    }
    bundleContext.ungetService( serviceReference );
  }

  private boolean isPlainConfiguration( Object service ) {
    return service instanceof ApplicationConfiguration && !( service instanceof ProxyApplicationConfiguration );
  }

  // for testing purpose
  BundleContext getRWTOSGiBundleContext() {
    return FrameworkUtil.getBundle( ApplicationLauncher.class ).getBundleContext();
  }

}

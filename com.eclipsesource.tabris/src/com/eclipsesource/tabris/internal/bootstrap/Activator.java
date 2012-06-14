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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.service.EventListenerHook;
import org.osgi.framework.hooks.service.FindHook;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> findRegistration;
  private ServiceRegistration<?> eventListenerRegistration;

  public void start( BundleContext context ) throws Exception {
    ConfigurationHook hook = new ConfigurationHook( context );
    findRegistration = context.registerService( FindHook.class.getName(), hook, null );
    eventListenerRegistration = context.registerService( EventListenerHook.class.getName(), hook, null );
  }

  public void stop( BundleContext context ) throws Exception {
    findRegistration.unregister();
    eventListenerRegistration.unregister();
  }
}

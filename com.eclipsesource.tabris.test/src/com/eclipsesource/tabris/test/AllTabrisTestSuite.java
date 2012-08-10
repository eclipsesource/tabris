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
package com.eclipsesource.tabris.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.eclipsesource.tabris.BootstrapperTest;
import com.eclipsesource.tabris.ClientDeviceTest;
import com.eclipsesource.tabris.geolocation.GeolocationOptionsTest;
import com.eclipsesource.tabris.geolocation.GeolocationTest;
import com.eclipsesource.tabris.geolocation.PositionErrorTest;
import com.eclipsesource.tabris.geolocation.PositionTest;
import com.eclipsesource.tabris.geolocation.internal.GeolocationAdapterTest;
import com.eclipsesource.tabris.geolocation.internal.GeolocationSynchronizerTest;
import com.eclipsesource.tabris.internal.AbstractObjectSynchronizerTest;
import com.eclipsesource.tabris.internal.GCOperationDispatcherTest;
import com.eclipsesource.tabris.internal.VideoLifeCycleAdapterTest;
import com.eclipsesource.tabris.internal.bootstrap.ActivatorTest;
import com.eclipsesource.tabris.internal.bootstrap.ApplicationWrapperTest;
import com.eclipsesource.tabris.internal.bootstrap.ConfigurationHookTest;
import com.eclipsesource.tabris.internal.bootstrap.EntryPointLookupServletTest;
import com.eclipsesource.tabris.internal.bootstrap.HttpServiceTrackerTest;
import com.eclipsesource.tabris.internal.bootstrap.ProxyApplicationConfigurationTest;
import com.eclipsesource.tabris.internal.bootstrap.ThemePhaseListenerTest;
import com.eclipsesource.tabris.widgets.ClientCanvasTest;
import com.eclipsesource.tabris.widgets.VideoTest;


@RunWith( Suite.class )
@SuiteClasses( { 
  GeolocationTest.class,
  GeolocationOptionsTest.class,
  PositionErrorTest.class,
  PositionTest.class,
  GeolocationAdapterTest.class,
  GeolocationSynchronizerTest.class,
  AbstractObjectSynchronizerTest.class,
  ActivatorTest.class,
  ApplicationWrapperTest.class,
  ProxyApplicationConfigurationTest.class,
  ThemePhaseListenerTest.class,
  BootstrapperTest.class,
  ConfigurationHookTest.class,
  HttpServiceTrackerTest.class,
  EntryPointLookupServletTest.class,
  GCOperationDispatcherTest.class,
  ClientCanvasTest.class,
  ClientDeviceTest.class,
  VideoTest.class,
  VideoLifeCycleAdapterTest.class
} )
public class AllTabrisTestSuite {
}

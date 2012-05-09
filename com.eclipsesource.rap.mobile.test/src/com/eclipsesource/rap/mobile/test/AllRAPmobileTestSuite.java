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
package com.eclipsesource.rap.mobile.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.eclipsesource.rap.mobile.BootstrapperTest;
import com.eclipsesource.rap.mobile.geolocation.GeolocationOptionsTest;
import com.eclipsesource.rap.mobile.geolocation.GeolocationTest;
import com.eclipsesource.rap.mobile.geolocation.PositionErrorTest;
import com.eclipsesource.rap.mobile.geolocation.PositionTest;
import com.eclipsesource.rap.mobile.geolocation.internal.GeolocationAdapterTest;
import com.eclipsesource.rap.mobile.geolocation.internal.GeolocationSynchronizerTest;
import com.eclipsesource.rap.mobile.internal.AbstractObjectSynchronizerTest;
import com.eclipsesource.rap.mobile.internal.GCOperationDispatcherTest;
import com.eclipsesource.rap.mobile.internal.bootstrap.ActivatorTest;
import com.eclipsesource.rap.mobile.internal.bootstrap.ApplicationWrapperTest;
import com.eclipsesource.rap.mobile.internal.bootstrap.ConfigurationHookTest;
import com.eclipsesource.rap.mobile.internal.bootstrap.EntryPointLookupServletTest;
import com.eclipsesource.rap.mobile.internal.bootstrap.HttpServiceTrackerTest;
import com.eclipsesource.rap.mobile.internal.bootstrap.ProxyApplicationConfigurationTest;
import com.eclipsesource.rap.mobile.internal.bootstrap.ThemePhaseListenerTest;
import com.eclipsesource.rap.mobile.widgets.ClientCanvasTest;


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
  ClientCanvasTest.class
} )
public class AllRAPmobileTestSuite {
}

/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.analytics.request;

import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestValueProvider.values;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RequestValueProviderTest {

  @Test
  public void testStoresAppViewHit() {
    assertEquals( "appview", values.get( "HIT_APPVIEW" ) );
  }

  @Test
  public void testStoresPageViewHit() {
    assertEquals( "pageview", values.get( "HIT_PAGEVIEW" ) );
  }

  @Test
  public void testStoresTransaction() {
    assertEquals( "transaction", values.get( "HIT_TRANSACTION" ) );
  }

  @Test
  public void testStoresEventHit() {
    assertEquals( "event", values.get( "HIT_EVENT" ) );
  }

  @Test
  public void testStoresExceptionHit() {
    assertEquals( "exception", values.get( "HIT_EXCEPTION" ) );
  }

  @Test
  public void testStoresSocialHit() {
    assertEquals( "social", values.get( "HIT_SOCIAL" ) );
  }

  @Test
  public void testStoresTimingHit() {
    assertEquals( "timing", values.get( "HIT_TIMING" ) );
  }

  @Test
  public void testStoresSessionControlStart() {
    assertEquals( "start", values.get( "SESSION_CONTROL_START" ) );
  }

  @Test
  public void testStoresSessionControlEnd() {
    assertEquals( "end", values.get( "SESSION_CONTROL_END" ) );
  }
}

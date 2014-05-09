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
package com.eclipsesource.tabris.tracking.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;


public class EventDispatcherProviderTest {

  @Test
  public void testCreatesDispatcher() {
    EventDispatcher dispatcher = EventDispatcherProvider.getDispatcher();

    assertNotNull( dispatcher );
  }

  @Test
  public void testCreatesDispatcherAsSingleton() {
    EventDispatcher dispatcher1 = EventDispatcherProvider.getDispatcher();
    EventDispatcher dispatcher2 = EventDispatcherProvider.getDispatcher();

    assertSame( dispatcher1, dispatcher2 );
  }
}

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
package com.eclipsesource.tabris.xcallbackurl.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.eclipsesource.tabris.xcallbackurl.XCallback;


public class XCallbackSyncAdapterTest {
  
  @Test
  public void testWantsToCallDefault() {
    XCallbackSyncAdapter adapter = new XCallbackSyncAdapter();
    
    assertFalse( adapter.wantsToCall() );
  }
  
  @Test
  public void testWantsToCall() {
    XCallbackSyncAdapter adapter = new XCallbackSyncAdapter();
    
    adapter.setWantsToCall( true );

    assertTrue( adapter.wantsToCall() );
  }
  
  @Test
  public void testCallbackDefault() {
    XCallbackSyncAdapter adapter = new XCallbackSyncAdapter();
    
    assertNull( adapter.getCallback() );
  }
  
  @Test
  public void testCallback() {
    XCallbackSyncAdapter adapter = new XCallbackSyncAdapter();
    XCallback callback = mock( XCallback.class );
    
    adapter.setCallback( callback );

    assertSame( callback, adapter.getCallback() );
  }
}

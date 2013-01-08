/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.swipe;

import static org.mockito.Mockito.mock;

import org.junit.Test;


public class SwipeAdapterTest {
  
  @Test
  public void testActivationDoesNotFail() {
    SwipeAdapter adapter = new SwipeAdapter();
    
    adapter.itemActivated( mock( SwipeItem.class ), 0, mock( SwipeContext.class ) );
  }
  
  @Test
  public void testDeactivationDoesNotFail() {
    SwipeAdapter adapter = new SwipeAdapter();
    
    adapter.itemDeactivated( mock( SwipeItem.class ), 0, mock( SwipeContext.class ) );
  }
  
  @Test
  public void testLoadDoesNotFail() {
    SwipeAdapter adapter = new SwipeAdapter();
    
    adapter.itemLoaded( mock( SwipeItem.class ), 0 );
  }
  
  @Test
  public void testDisposeDoesNotFail() {
    SwipeAdapter adapter = new SwipeAdapter();
    
    adapter.disposed( mock( SwipeContext.class ) );
  }
}

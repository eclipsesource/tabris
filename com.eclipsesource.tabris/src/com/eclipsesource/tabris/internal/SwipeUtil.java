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
package com.eclipsesource.tabris.internal;

import java.util.List;

import com.eclipsesource.tabris.widgets.swipe.SwipeContext;
import com.eclipsesource.tabris.widgets.swipe.SwipeItem;
import com.eclipsesource.tabris.widgets.swipe.SwipeListener;


public class SwipeUtil {
  
  public static void notifyItemLoaded( List<SwipeListener> listeners, SwipeItem item, int index ) {
    for( SwipeListener listener : listeners ) {
      listener.itemLoaded( item, index );
    }
  }
  
  public static void notifyItemActivated( 
    List<SwipeListener> listeners, SwipeItem item, int index, SwipeContext context ) 
  {
    for( SwipeListener listener : listeners ) {
      listener.itemActivated( item, index, context );
    }
  }
  
  public static void notifyItemDeactivated( 
    List<SwipeListener> listeners, SwipeItem item, int index, SwipeContext context ) 
  {
    for( SwipeListener listener : listeners ) {
      listener.itemDeactivated( item, index, context );
    }
  }
  
  public static void notifyDisposed( List<SwipeListener> listeners, SwipeContext context ) {
    for( SwipeListener listener : listeners ) {
      listener.disposed( context );
    }
  }
  
  private SwipeUtil() {
    // prevent instantiation
  }
}

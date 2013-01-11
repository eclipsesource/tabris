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

import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNull;

import org.eclipse.swt.SWT;

import com.eclipsesource.tabris.widgets.swipe.SwipeContext;
import com.eclipsesource.tabris.widgets.swipe.SwipeItemProvider;


public class SwipeManager {

  public static final String TYPE = "tabris.Swipe";
  public static final String EVENT_SWIPED_TO_ITEM = "SwipedToItem";
  public static final String PROPERTY_ITEM = "item";
  public static final String PROPERTY_PARENT = "parent";
  public static final String METHOD_REMOVE_ITEMS = "removeItems";
  public static final String PROPERTY_ITEMS = "items";
  public static final String PROPERTY_ACTIVE_ITEM = "activeItem";
  public static final String METHOD_ITEM_LOADED = "itemLoaded";
  public static final String PROPERTY_CONTENT = "content";
  public static final String PROPERTY_INDEX = "index";
  public static final String METHOD_LOCK_LEFT = "lockLeft";
  public static final String METHOD_LOCK_RIGHT = "lockRight";
  public static final String METHOD_UNLOCK_LEFT = "unlockLeft";
  public static final String METHOD_UNLOCK_RIGHT = "unlockRight";

  private final SwipeItemProvider provider;
  private final SwipeContext context;
  private final SwipeItemHolder itemHolder;
  private final SwipeItemIndexer indexer;
  private int leftLock;
  private int rightLock;

  public SwipeManager( SwipeItemProvider provider ) {
    argumentNotNull( provider, "SwipeItemProvier" );
    this.provider = provider;
    this.context = new SwipeContext();
    this.itemHolder = new SwipeItemHolder();
    this.indexer = new SwipeItemIndexer();
    this.leftLock = -1;
    this.rightLock = -1;
  }

  public SwipeItemProvider getProvider() {
    return provider;
  }

  public SwipeContext getContext() {
    return context;
  }

  public SwipeItemHolder getItemHolder() {
    return itemHolder;
  }

  public SwipeItemIndexer getIndexer() {
    return indexer;
  }

  public void lock( int direction, int index, boolean locked ) {
    if( locked ) {
      lock( direction, index );
    } else {
      unlock( direction );
    }
  }

  public void unlock( int direction ) {
    if( direction == SWT.LEFT ) {
      leftLock = -1;
    } else {
      rightLock = -1;
    }
  }

  private void lock( int direction, int index ) {
    if( direction == SWT.LEFT ) {
      leftLock = index;
    } else {
      rightLock = index;
    }
  }

  public boolean isMoveAllowed( int fromIndex, int toIndex ) {
    int direction = SWT.LEFT;
    if( toIndex > fromIndex ) {
      direction = SWT.RIGHT;
    }
    return isValidMove( fromIndex, direction );
  }

  private boolean isValidMove( int fromIndex, int direction ) {
    boolean result;
    if( direction == SWT.LEFT ) {
      result = leftLock == -1 || fromIndex != leftLock;
    } else {
      result = rightLock == -1 || fromIndex != rightLock;
    }
    return result;
  }
}

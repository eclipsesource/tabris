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

import java.util.ArrayList;
import java.util.List;

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
  public static final String METHOD_LOCK_ITEM = "lockItem";
  public static final String METHOD_UNLOCK_ITEM = "unlockItem";
  public static final String PROPERTY_DIRECTION = "direction";

  private final SwipeItemProvider provider;
  private final SwipeContext context;
  private final SwipeItemHolder itemHolder;
  private final SwipeItemIndexer indexer;
  private final List<LockedSwipeIndex> lockedIndexes;

  public SwipeManager( SwipeItemProvider provider ) {
    argumentNotNull( provider, "SwipeItemProvier" );
    this.provider = provider;
    this.context = new SwipeContext();
    this.itemHolder = new SwipeItemHolder();
    this.indexer = new SwipeItemIndexer();
    this.lockedIndexes = new ArrayList<LockedSwipeIndex>();
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
    LockedSwipeIndex lockedIndex = new LockedSwipeIndex( direction, index );
    if( locked ) {
      addLockedIndex( lockedIndex );
    } else {
      lockedIndexes.remove( lockedIndex );
    }
  }

  private void addLockedIndex( LockedSwipeIndex lockedIndex ) {
    if( !lockedIndexes.contains( lockedIndex ) ) {
      lockedIndexes.add( lockedIndex );
    }
  }

  public boolean isMoveAllowed( int fromIndex, int toIndex ) {
    int direction = SWT.LEFT;
    if( toIndex > fromIndex ) {
      direction = SWT.RIGHT;
    }
    return !lockedIndexes.contains( new LockedSwipeIndex( direction, fromIndex ) );
  }
}

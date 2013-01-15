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

import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNull;
import static com.eclipsesource.tabris.internal.SwipeItemIndexer.getAsArray;
import static com.eclipsesource.tabris.internal.SwipeManager.METHOD_ITEM_LOADED;
import static com.eclipsesource.tabris.internal.SwipeManager.METHOD_LOCK_LEFT;
import static com.eclipsesource.tabris.internal.SwipeManager.METHOD_LOCK_RIGHT;
import static com.eclipsesource.tabris.internal.SwipeManager.METHOD_REMOVE_ITEMS;
import static com.eclipsesource.tabris.internal.SwipeManager.METHOD_UNLOCK_LEFT;
import static com.eclipsesource.tabris.internal.SwipeManager.METHOD_UNLOCK_RIGHT;
import static com.eclipsesource.tabris.internal.SwipeManager.PROPERTY_ACTIVE_ITEM;
import static com.eclipsesource.tabris.internal.SwipeManager.PROPERTY_CONTENT;
import static com.eclipsesource.tabris.internal.SwipeManager.PROPERTY_INDEX;
import static com.eclipsesource.tabris.internal.SwipeManager.PROPERTY_ITEMS;
import static com.eclipsesource.tabris.internal.SwipeManager.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.SwipeManager.TYPE;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyDisposed;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyItemActivated;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyItemDeactivated;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyItemLoaded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectFactory;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.eclipsesource.tabris.internal.SwipeItemHolder;
import com.eclipsesource.tabris.internal.SwipeLayout;
import com.eclipsesource.tabris.internal.SwipeManager;
import com.eclipsesource.tabris.internal.SwipeOperationHandler;


/**
 * @since 0.10
 */
@SuppressWarnings("restriction")
public class Swipe {

  private final Composite container;
  private final List<SwipeListener> listeners;
  private final RemoteObject remoteObject;
  private final SwipeManager manager;

  public Swipe( Composite parent, SwipeItemProvider itemProvider ) {
    argumentNotNull( parent, "Parent" );
    argumentNotNull( itemProvider, "SwipeItemProvider" );
    this.manager = new SwipeManager( itemProvider );
    this.listeners = new ArrayList<SwipeListener>();
    this.container = new Composite( parent, SWT.NONE );
    container.setData( RWT.CUSTOM_VARIANT, "swipe" );
    this.remoteObject = RemoteObjectFactory.getInstance().createRemoteObject( TYPE );
    initialize();
  }

  private void initialize() {
    remoteObject.set( PROPERTY_PARENT, WidgetUtil.getId( container ) );
    remoteObject.setHandler( new SwipeOperationHandler( this ) );
    container.setLayout( new SwipeLayout() );
    if( manager.getProvider().getItemCount() > 0 ) {
      show( 0 );
    }
  }

  public void setCacheSize( int size ) {
    verifyIsNotDisposed();
    manager.getIndexer().setRange( size );
    if( isValidIndex( manager.getIndexer().getCurrent() ) ) {
      refresh();
    }
  }

  public void refresh() throws IllegalStateException {
    int current = manager.getIndexer().getCurrent();
    if( isValidIndex( current ) ) {
      manager.getIndexer().reset();
      show( current, false );
    } else {
      throw new IllegalStateException( "Item at index " + current + " does not exist anymore." );
    }
  }

  public void show( int index ) throws IllegalArgumentException, IllegalStateException {
    show( index, hasCurrentIndexChanged( index ) );
  }

  private void show( int index, boolean needsToShow ) {
    verifyIsNotDisposed();
    verifyMove( index );
    if( isValidIndex( index ) ) {
      verifyLocks();
      showItemAtIndex( index, needsToShow );
    } else {
      throw new IllegalArgumentException( "Item at index " + index + " does not exist." );
    }
  }

  private void verifyMove( int index ) {
    if( !manager.isMoveAllowed( manager.getIndexer().getCurrent(), index ) ) {
      throw new IllegalStateException( "Move not allowed. Item " + index + " is locked." );
    }
  }

  private void verifyLocks() {
    removeLockIfOutOfBounds( manager.getLeftLock(), SWT.LEFT );
    removeLockIfOutOfBounds( manager.getRightLock(), SWT.RIGHT );
  }

  private void removeLockIfOutOfBounds( int lock, int direction ) {
    if( lock != -1 && !isValidIndex( lock ) ) {
      unlock( direction );
    }
  }

  private boolean isValidIndex( int index ) {
    return index >= 0 && manager.getProvider().getItemCount() > index;
  }

  private void showItemAtIndex( int index, boolean needsToShow ) {
    manager.getIndexer().setCurrent( index );
    removeOutOfRangeItems();
    handlePreviousItem();
    if( needsToShow ) {
      showCurrentItem();
    }
    initializeNextItem();
  }

  private boolean hasCurrentIndexChanged( int index ) {
    boolean needToShow = false;
    if( index != manager.getIndexer().getCurrent() ) {
      needToShow = true;
    }
    return needToShow;
  }

  private void removeOutOfRangeItems() {
    int[] outOfRangeIndexes = filterRespectingBounds( manager.getIndexer().popOutOfRangeIndexes() );
    for( int index : outOfRangeIndexes ) {
      if( wasActiveItem( index ) ) {
        manager.getItemHolder().getItem( index ).deactivate( manager.getContext() );
      }
      manager.getItemHolder().removeItem( index );
    }
    callRemoveItems( outOfRangeIndexes );
  }

  private int[] filterRespectingBounds( int[] outOfRangeIndexes ) {
    List<Integer> indexes = new ArrayList<Integer>();
    for( int index : outOfRangeIndexes ) {
      if( index < manager.getProvider().getItemCount() ) {
        indexes.add( Integer.valueOf( index ) );
      }
    }
    addLoadedOutOfBoundsItems( indexes );
    return getAsArray( indexes );
  }

  private void addLoadedOutOfBoundsItems( List<Integer> indexes ) {
    List<Integer> loadedItems = manager.getItemHolder().getLoadedItems();
    for( Integer item : loadedItems ) {
      if( item.intValue() > ( manager.getProvider().getItemCount() -1 ) || isOutOfRange( item ) ) {
        indexes.add( item );
        manager.getItemHolder().removeItem( item.intValue() );
      }
    }
  }

  private boolean isOutOfRange( Integer item ) {
    return manager.getIndexer().getRange() == 0 && item.intValue() != manager.getIndexer().getCurrent();
  }

  private boolean wasActiveItem( int index ) {
    boolean result = false;
    Control topControl = ( ( SwipeLayout ) container.getLayout() ).getOnTopControl();
    if( manager.getItemHolder().isLoaded( index ) ) {
      if( manager.getItemHolder().getContentForItem( index ).equals( topControl ) ) {
        result = true;
      }
    }
    return result;
  }

  private void callRemoveItems( int[] outOfRangeIndexes ) {
    if( outOfRangeIndexes.length > 0 ) {
      Map<String, Object> properties = new HashMap<String, Object>();
      properties.put( PROPERTY_ITEMS, outOfRangeIndexes );
      remoteObject.call( METHOD_REMOVE_ITEMS, properties );
    }
  }

  private void handlePreviousItem() {
    int[] previousItems = manager.getIndexer().getPrevious();
    for( int previousItemIndex : previousItems ) {
      if( manager.getProvider().getItemCount() > previousItemIndex && previousItemIndex >= 0 ) {
        ensureItemExists( previousItemIndex );
        preloadItem( previousItemIndex );
      }
    }
  }

  private void showCurrentItem() {
    int currentIndex = manager.getIndexer().getCurrent();
    ensureItemExists( currentIndex );
    ensureItemIsLoaded( currentIndex );
    deactivateLastActiveItem();
    activateItem( currentIndex );
    setOnTopControl( manager.getItemHolder().getContentForItem( currentIndex ) );
  }

  private void deactivateLastActiveItem() {
    int oldIndex = manager.getIndexer().getOld();
    if( oldIndex != -1 ) {
      ensureItemExists( oldIndex );
      SwipeItem item = manager.getItemHolder().getItem( oldIndex );
      item.deactivate( manager.getContext() );
      notifyItemDeactivated( listeners, item, oldIndex, manager.getContext() );
    }
  }

  private void activateItem( int currentIndex ) {
    SwipeItem currentItem = manager.getItemHolder().getItem( currentIndex );
    currentItem.activate( manager.getContext() );
    remoteObject.set( PROPERTY_ACTIVE_ITEM, currentIndex );
    notifyItemActivated( listeners, currentItem, currentIndex, manager.getContext() );
  }

  private void setOnTopControl( Control control ) {
    SwipeLayout layout = ( SwipeLayout )container.getLayout();
    layout.setOnTopControl( control );
  }

  private void initializeNextItem() {
    int[] nextItems = manager.getIndexer().getNext();
    for( int nextItemIndex : nextItems ) {
      if( manager.getProvider().getItemCount() > nextItemIndex && nextItemIndex >= 0 ) {
        ensureItemExists( nextItemIndex );
        preloadItem( nextItemIndex );
      }
    }
  }

  private void preloadItem( int index ) {
    SwipeItem item = manager.getProvider().getItem( index );
    if( item.isPreloadable() ) {
      ensureItemIsLoaded( index );
    }
  }

  private void ensureItemExists( int index ) {
    if( !manager.getItemHolder().hasItem( index ) ) {
      SwipeItem item = manager.getProvider().getItem( index );
      manager.getItemHolder().addItem( index, item );
    }
  }

  private void ensureItemIsLoaded( int index ) {
    if( !manager.getItemHolder().isLoaded( index ) ) {
      SwipeItem item = manager.getItemHolder().getItem( index );
      Control content = item.load( container );
      container.layout( true );
      manager.getItemHolder().setContentForItem( index, content );
      remoteObject.call( METHOD_ITEM_LOADED, createLoadProperties( index, content ) );
      notifyItemLoaded( listeners, item, index );
    }
  }

  private Map<String, Object> createLoadProperties( int index, Control content ) {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put( PROPERTY_INDEX, Integer.valueOf( index ) );
    result.put( PROPERTY_CONTENT, WidgetUtil.getId( content ) );
    return result;
  }

  public void addSwipeListener( SwipeListener listener ) {
    verifyIsNotDisposed();
    listeners.add( listener );
  }

  public void removeSwipeListener( SwipeListener listener ) {
    verifyIsNotDisposed();
    listeners.remove( listener );
  }

  public Control getControl() {
    return container;
  }

  public SwipeContext getContext() {
    return manager.getContext();
  }

  public void lock( int direction ) throws IllegalArgumentException {
    verifyDirection( direction );
    int indexToLock = manager.getIndexer().getCurrent();
    manager.lock( direction, indexToLock, true );
    String method = direction == SWT.LEFT ? METHOD_LOCK_LEFT : METHOD_LOCK_RIGHT;
    remoteObject.call( method, createLockProperties( direction, indexToLock ) );
  }

  public void unlock( int direction ) throws IllegalArgumentException {
    verifyDirection( direction );
    manager.unlock( direction );
    String method = direction == SWT.LEFT ? METHOD_UNLOCK_LEFT : METHOD_UNLOCK_RIGHT;
    remoteObject.call( method, null );
  }

  private Map<String, Object> createLockProperties( int direction, int index ) {
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( PROPERTY_INDEX, Integer.valueOf( index ) );
    return properties;
  }

  private void verifyDirection( int direction ) {
    if( !( direction == SWT.LEFT || direction == SWT.RIGHT ) ) {
      throw new IllegalArgumentException( "Invalid lock direction. Only SWT.LEFT and SWT.RIGHT are supported." );
    }
  }

  private void verifyIsNotDisposed() {
    if( container.isDisposed() ) {
      throw new IllegalStateException( "Swipe is already disposed" );
    }
  }

  public void dispose() {
    manager.getItemHolder().removeAllItems();
    container.dispose();
    notifyDisposed( listeners, manager.getContext() );
  }

  SwipeItemHolder getItemHolder() {
    return manager.getItemHolder();
  }

}

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

import static com.eclipsesource.tabris.internal.Constants.METHOD_ADD;
import static com.eclipsesource.tabris.internal.Constants.METHOD_LOCK_LEFT;
import static com.eclipsesource.tabris.internal.Constants.METHOD_LOCK_RIGHT;
import static com.eclipsesource.tabris.internal.Constants.METHOD_REMOVE;
import static com.eclipsesource.tabris.internal.Constants.METHOD_UNLOCK_LEFT;
import static com.eclipsesource.tabris.internal.Constants.METHOD_UNLOCK_RIGHT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ACTIVE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CONTROL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_INDEX;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ITEMS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.TYPE_SWIPE;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.SWIPE;
import static com.eclipsesource.tabris.internal.Preconditions.checkArgument;
import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;
import static com.eclipsesource.tabris.internal.Preconditions.checkState;
import static com.eclipsesource.tabris.internal.SwipeItemIndexer.getAsArray;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyDisposed;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyItemActivated;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyItemDeactivated;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyItemLoaded;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.eclipsesource.tabris.internal.JsonUtil;
import com.eclipsesource.tabris.internal.SwipeItemHolder;
import com.eclipsesource.tabris.internal.SwipeManager;
import com.eclipsesource.tabris.internal.SwipeOperationHandler;
import com.eclipsesource.tabris.internal.ZIndexStackLayout;


/**
 * <p>
 * A <code>Swipe</code> object can be used to navigate through a stack of {@link Composite} objects (represented by a
 * {@link SwipeItem}using a swipe gesture. The content of a <code>Swipe</code> can be modified in a dynamic way using
 * a {@link SwipeItemProvider}. To increase the user experience the content of each item can be pre-loaded.
 * </p>
 * <p>
 * E.g. the default size of pre loaded items is 1. This means when you show item 1, item 0 an 2 will be pre loaded. The
 * size of this pre loading cache is configurable and can change during runtime.
 * </p>
 * <p>
 * It's also possible to lock item in two directions. This means when a item 1 is locked with <code>SWT.LEFT</code> a
 * user is not able to swipe to item 0.
 * </p>
 *
 * @see SwipeItemProvider
 * @see SwipeListener
 * @see SwipeContext
 *
 * @since 0.10
 */
public class Swipe implements Serializable {

  private final Composite container;
  private final List<SwipeListener> listeners;
  private final RemoteObject remoteObject;
  private final SwipeManager manager;

  public Swipe( Composite parent, SwipeItemProvider itemProvider ) {
    checkArgumentNotNull( parent, "Parent" );
    checkArgumentNotNull( itemProvider, "SwipeItemProvider" );
    this.manager = new SwipeManager( itemProvider );
    this.listeners = new ArrayList<SwipeListener>();
    this.container = new Composite( parent, SWT.NONE );
    container.setData( SWIPE.getKey(), Boolean.TRUE );
    this.remoteObject = RWT.getUISession().getConnection().createRemoteObject( TYPE_SWIPE );
    initialize();
  }

  private void initialize() {
    remoteObject.set( PROPERTY_PARENT, WidgetUtil.getId( container ) );
    remoteObject.setHandler( new SwipeOperationHandler( this ) );
    container.setLayout( new ZIndexStackLayout() );
    if( manager.getProvider().getItemCount() > 0 ) {
      show( 0 );
    }
  }

  /**
   * <p>
   * Configures the amount of pre loaded items. A size of 2 means that when showing item 3, item 1, 2, 4 and 5 will
   * be loaded. The cache size must be > 0.
   * </p>
   *
   * @param size The amount of item to pre loading each direction.
   * @throws IllegalArgumentException when cache size is < 0
   */
  public void setCacheSize( int size ) throws IllegalArgumentException {
    verifyIsNotDisposed();
    manager.getIndexer().setRange( size );
    if( isValidIndex( manager.getIndexer().getCurrent() ) ) {
      refresh();
    }
  }

  /**
   * <p>
   * Triggers a refresh to get new input from the {@link SwipeItemProvider}. This is like calling the show method with
   * the current index.
   * </p>
   *
   * @throws IllegalStateException when the current item was removed in the {@link SwipeItemProvider}.
   */
  public void refresh() throws IllegalStateException {
    int current = manager.getIndexer().getCurrent();
    if( isValidIndex( current ) ) {
      manager.getIndexer().reset();
      show( current, false );
    } else {
      throw new IllegalStateException( "Item at index " + current + " does not exist anymore." );
    }
  }

  /**
   * <p>
   * Shows the item at the given index. Calling this method is comparable with a programmatic swiping to a given item.
   * </p>
   *
   * @param index the index of the item to swipe to.
   *
   * @throws IllegalArgumentException when the passed index is negative or does not exist in the
   *                                  {@link SwipeItemProvider}.
   * @throws IllegalStateException when already disposed or the move is not valid e.g. when a item is locked.
   */
  public void show( int index ) throws IllegalArgumentException, IllegalStateException {
    show( index, hasCurrentIndexChanged( index ) );
  }

  private void show( int index, boolean needsToShow ) {
    verifyIsNotDisposed();
    checkState( manager.isMoveAllowed( manager.getIndexer().getCurrent(), index ),
                "Move not allowed. Item " + index + " is locked." );
    if( isValidIndex( index ) ) {
      verifyLocks();
      showItemAtIndex( index, needsToShow );
    } else {
      throw new IllegalArgumentException( "Item at index " + index + " does not exist." );
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
    Control topControl = ( ( ZIndexStackLayout ) container.getLayout() ).getOnTopControl();
    if( manager.getItemHolder().isLoaded( index ) ) {
      if( manager.getItemHolder().getContentForItem( index ).equals( topControl ) ) {
        result = true;
      }
    }
    return result;
  }

  private void callRemoveItems( int[] outOfRangeIndexes ) {
    if( outOfRangeIndexes.length > 0 ) {
      JsonObject properties = new JsonObject();
      properties.add( PROPERTY_ITEMS, JsonUtil.createJsonArray( outOfRangeIndexes ) );
      remoteObject.call( METHOD_REMOVE, properties );
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
    remoteObject.set( PROPERTY_ACTIVE, currentIndex );
    notifyItemActivated( listeners, currentItem, currentIndex, manager.getContext() );
  }

  private void setOnTopControl( Control control ) {
    ZIndexStackLayout layout = ( ZIndexStackLayout )container.getLayout();
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
      remoteObject.call( METHOD_ADD, createLoadProperties( index, content ) );
      notifyItemLoaded( listeners, item, index );
    }
  }

  private JsonObject createLoadProperties( int index, Control content ) {
    JsonObject result = new JsonObject();
    result.add( PROPERTY_INDEX, index );
    result.add( PROPERTY_CONTROL, WidgetUtil.getId( content ) );
    return result;
  }

  /**
   * <p>
   * Adds a {@link SwipeListener} to get notified about swipping events.
   * </p>
   */
  public void addSwipeListener( SwipeListener listener ) {
    verifyIsNotDisposed();
    listeners.add( listener );
  }

  /**
   * <p>
   * Removes the given {@link SwipeListener}.
   * </p>
   */
  public void removeSwipeListener( SwipeListener listener ) {
    checkState( !container.isDisposed(), "Swipe is already disposed" );
    listeners.remove( listener );
  }

  /**
   * <p>
   * Returns the underlying control. This control is the parent of all {@link SwipeItem}s. Should only be used to do
   * layouting stuff.
   * </p>
   */
  public Control getControl() {
    return container;
  }

  /**
   * <p>
   * Returns the {@link SwipeContext} that is shared during all swipping events.
   * </p>
   */
  public SwipeContext getContext() {
    return manager.getContext();
  }

  /**
   * <p>
   * Locks the current item in the given direction. Allowed directions are SWT.LEFT and SWT.RIGHT.
   * <p>
   *
   * @throws IllegalArgumentException when not SWT.LEFT or SWT.RIGHT
   */
  public void lock( int direction ) throws IllegalArgumentException {
    checkArgument( direction == SWT.LEFT || direction == SWT.RIGHT,
                   "Invalid lock direction. Only SWT.LEFT and SWT.RIGHT are supported." );
    int indexToLock = manager.getIndexer().getCurrent();
    manager.lock( direction, indexToLock, true );
    String method = direction == SWT.LEFT ? METHOD_LOCK_LEFT : METHOD_LOCK_RIGHT;
    remoteObject.call( method, createLockProperties( direction, indexToLock ) );
  }

  /**
   * <p>
   * Unlocks the current item in the given direction. Allowed directions are SWT.LEFT and SWT.RIGHT.
   * <p>
   *
   * @throws IllegalArgumentException when not SWT.LEFT or SWT.RIGHT
   */
  public void unlock( int direction ) throws IllegalArgumentException {
    checkArgument( direction == SWT.LEFT || direction == SWT.RIGHT,
                   "Invalid lock direction. Only SWT.LEFT and SWT.RIGHT are supported." );
    manager.unlock( direction );
    String method = direction == SWT.LEFT ? METHOD_UNLOCK_LEFT : METHOD_UNLOCK_RIGHT;
    remoteObject.call( method, null );
  }

  private JsonObject createLockProperties( int direction, int index ) {
    JsonObject properties = new JsonObject();
    properties.add( PROPERTY_INDEX, index );
    return properties;
  }

  private void verifyIsNotDisposed() {
    checkState( !container.isDisposed(), "Swipe is already disposed" );
  }

  /**
   * <p>
   * Disposes the complete {@link Swipe} object and all it's children.
   * </p>
   */
  public void dispose() {
    manager.getItemHolder().removeAllItems();
    container.dispose();
    notifyDisposed( listeners, manager.getContext() );
  }

  SwipeItemHolder getItemHolder() {
    return manager.getItemHolder();
  }

  Composite getContainer() {
    return container;
  }

}

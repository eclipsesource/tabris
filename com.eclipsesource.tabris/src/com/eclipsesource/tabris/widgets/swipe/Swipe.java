/*******************************************************************************
 * Copyright (c) 2013, 2017 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.swipe;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNot;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.METHOD_ADD;
import static com.eclipsesource.tabris.internal.Constants.METHOD_REMOVE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ACTIVE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CONTROL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_INDEX;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ITEMS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ITEM_COUNT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.TYPE_SWIPE;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.SWIPE;
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
import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
@SuppressWarnings("restriction")
public class Swipe implements Serializable {

  private final SwipeOperationHandler operationHandler;
  private final Composite container;
  private final List<SwipeListener> listeners;
  private final RemoteObject remoteObject;
  private final SwipeManager manager;
  private int oldCount;

  public Swipe( Composite parent, SwipeItemProvider itemProvider ) {
    whenNull( parent ).throwIllegalArgument( "Parent must not be null" );
    whenNull( itemProvider ).throwIllegalArgument( "SwipeItemProvider must not be null" );
    this.operationHandler = new SwipeOperationHandler( this );
    this.manager = new SwipeManager( itemProvider );
    this.listeners = new ArrayList<SwipeListener>();
    this.container = new Composite( parent, SWT.NONE );
    container.setData( SWIPE.getKey(), Boolean.TRUE );
    addDisposeListener();
    this.remoteObject = RWT.getUISession().getConnection().createRemoteObject( TYPE_SWIPE );
    initialize();
  }

  private void addDisposeListener() {
    container.addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent event ) {
        dispose();
      }
    } );
  }

  private void initialize() {
    remoteObject.set( PROPERTY_PARENT, WidgetUtil.getId( container ) );
    updateItemCount();
    remoteObject.setHandler( operationHandler );
    container.setLayout( new ZIndexStackLayout() );
    if( manager.getProvider().getItemCount() > 0 ) {
      show( 0 );
    }
  }

  /**
   * <p>
   * Configures the amount of pre loaded items. A size of 2 means that when showing item 3, item 1, 2, 4 and 5 will
   * be loaded. The cache size must be &gt; 0.
   * </p>
   * <p>
   * <strong>Note:</strong> On iOS at least two pre loaded items are needed to make swipe work as intended.
   * </p>
   *
   * @param size The amount of item to pre loading each direction.
   * @throws IllegalArgumentException when cache size is &lt;= 0
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
    whenNot( manager.isMoveAllowed( manager.getIndexer().getCurrent(), index ) )
      .throwIllegalState( "Move not allowed. Item " + index + " is locked." );
    if( isValidIndex( index ) ) {
      showItemAtIndex( index, needsToShow );
    } else {
      throw new IllegalArgumentException( "Item at index " + index + " does not exist." );
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
      operationHandler.setActiveClientItem( index );
    }
    initializeNextItem();
    updateItemCount();
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
      if( index < manager.getProvider().getItemCount() && manager.getItemHolder().isLoaded( index ) ) {
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
    if( currentIndex != operationHandler.getActiveClientItem() ) {
      remoteObject.set( PROPERTY_ACTIVE, currentIndex );
    }
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
      updateItemCount();
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
    when( container.isDisposed() ).throwIllegalState( "Swipe is already disposed" );
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

  private void verifyIsNotDisposed() {
    when( container.isDisposed() ).throwIllegalState( "Swipe is already disposed" );
  }

  private void updateItemCount() {
    int newCount = manager.getProvider().getItemCount();
    if( newCount != oldCount ) {
      oldCount = newCount;
      remoteObject.set( PROPERTY_ITEM_COUNT, newCount );
    }
  }

  /**
   * <p>
   * Disposes the complete {@link Swipe} object and all it's children.
   * </p>
   */
  public void dispose() {
    manager.getItemHolder().removeAllItems();
    if( !container.isDisposed() ) {
      container.dispose();
    }
    notifyDisposed( listeners, manager.getContext() );
    remoteObject.destroy();
  }

  SwipeItemHolder getItemHolder() {
    return manager.getItemHolder();
  }

  Composite getContainer() {
    return container;
  }

  SwipeOperationHandler getHandler() {
    return operationHandler;
  }

}

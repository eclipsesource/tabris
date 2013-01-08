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
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyDisposed;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyItemActivated;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyItemDeactivated;
import static com.eclipsesource.tabris.internal.SwipeUtil.notifyItemLoaded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  
  private static final String TYPE = "tabris.Swipe";
  private static final String METHOD_REMOVE_ITEMS = "removeItems";
  private static final String PROPERTY_ITEMS = "items";
  private static final String PROPERTY_ACTIVE_ITEM = "activeItem";
  private static final String METHOD_ITEM_LOADED = "itemLoaded";
  private static final String PROPERTY_CONTENT = "content";
  private static final String PROPERTY_INDEX = "index";
  
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
    this.remoteObject = RemoteObjectFactory.getInstance().createRemoteObject( TYPE );
    initialize();
  }

  private void initialize() {
    remoteObject.setHandler( new SwipeOperationHandler( this ) );
    container.setLayout( new SwipeLayout() );
  }

  public void show( int index ) throws IllegalArgumentException {
    verifyIsNotDisposed();
    if( isValidIndex( index ) ) {
      showItemAtIndex( index );
    } else {
      throw new IllegalArgumentException( "Item at index " + index + " does not exist." );
    }
  }

  private boolean isValidIndex( int index ) {
    return index >= 0 && manager.getProvider().getItemCount() > index;
  }

  private void showItemAtIndex( int index ) {
    if( index != manager.getIndexer().getCurrent() ) {
      manager.getIndexer().setCurrent( index );
      removeOutOfRangeItems();
      handlePreviousItem();
      showCurrentItem();
      initializeNextItem();
    }
  }
  
  private void removeOutOfRangeItems() {
    int[] outOfRangeIndexes = manager.getIndexer().popOutOfRangeIndexes();
    for( int index : outOfRangeIndexes ) {
      if( wasActiveItem( index ) ) {
        manager.getItemHolder().getItem( index ).deactivate( manager.getContext() );
      }
      manager.getItemHolder().removeItem( index );
    }
    callRemoveItems( outOfRangeIndexes );
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
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( PROPERTY_ITEMS, outOfRangeIndexes );
    remoteObject.call( METHOD_REMOVE_ITEMS, properties );
  }

  private void handlePreviousItem() {
    int previousItemIndex = manager.getIndexer().getPrevious();
    if( manager.getProvider().getItemCount() > previousItemIndex && previousItemIndex >= 0 ) {
      ensureItemExists( previousItemIndex );
      preloadItem( previousItemIndex );
      deactivateItem( previousItemIndex );
    }
  }

  private void deactivateItem( int previousItemIndex ) {
    SwipeItem item = manager.getItemHolder().getItem( previousItemIndex );
    item.deactivate( manager.getContext() );
    notifyItemDeactivated( listeners, item, previousItemIndex, manager.getContext() );
  }

  private void showCurrentItem() {
    int currentIndex = manager.getIndexer().getCurrent();
    ensureItemExists( currentIndex );
    ensureItemIsLoaded( currentIndex );
    activateItem( currentIndex );
    setOnTopControl( manager.getItemHolder().getContentForItem( currentIndex ) );
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
    int nextItemIndex = manager.getIndexer().getNext();
    if( manager.getProvider().getItemCount() > nextItemIndex && nextItemIndex >= 0 ) {
      ensureItemExists( nextItemIndex );
      preloadItem( nextItemIndex );
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

  public void setCacheSize( int size ) {
    verifyIsNotDisposed();
    // TODO: Make cache size configurable.
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

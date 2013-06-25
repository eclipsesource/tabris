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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.eclipsesource.tabris.widgets.swipe.SwipeItem;


public class SwipeItemHolder implements Serializable {

  private final Map<Integer, SwipeItem> items;
  private final Map<Integer, Control> contentHolder;

  public SwipeItemHolder() {
    items = new HashMap<Integer, SwipeItem>();
    contentHolder = new HashMap<Integer, Control>();
  }

  public void addItem( int index, SwipeItem item, Composite content ) {
    addItem( index, item );
    setContentForItem( index, content );
  }

  public void addItem( int index, SwipeItem item ) {
    when( items.containsKey( getKey( index ) ) ).thenIllegalState( "Item for index " + index + " already exists." );
    items.put( getKey( index ), item );
  }

  public void removeItem( int index ) {
    items.remove( getKey( index ) );
    removeContentForItem( index );
  }

  public void removeAllItems() {
    HashMap<Integer, SwipeItem> itemsCopy = new HashMap<Integer, SwipeItem>( items );
    for( Entry<Integer, SwipeItem> entry : itemsCopy.entrySet() ) {
      removeItem( entry.getKey().intValue() );
    }
  }

  public SwipeItem getItem( int index ) {
    return items.get( getKey( index ) );
  }

  public boolean hasItem( int index ) {
    return items.containsKey( getKey( index ) );
  }

  public void setContentForItem( int index, Control content ) {
    whenNot( items.containsKey( getKey( index ) ) ).thenIllegalState( "Item for index " + index + " does not exist." );
    contentHolder.put( getKey( index ), content );
  }

  public boolean isLoaded( int index ) {
    return contentHolder.containsKey( getKey( index ) );
  }

  public List<Integer> getLoadedItems() {
    Set<Integer> keySet = contentHolder.keySet();
    return new ArrayList<Integer>( keySet );
  }

  public Control getContentForItem( int index ) {
    return contentHolder.get( getKey( index ) );
  }

  public void removeContentForItem( int index ) {
    Control content = contentHolder.remove( getKey( index ) );
    if( content != null ) {
      content.dispose();
    }
  }

  private Integer getKey( int index ) {
    return Integer.valueOf( index );
  }

}

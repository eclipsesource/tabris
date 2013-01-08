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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.widgets.swipe.SwipeItem;


public class SwipeItemHolderTest {
  
  private SwipeItemHolder swipeItemHolder;

  @Before
  public void setUp() {
    swipeItemHolder = new SwipeItemHolder();
  }
  
  @Test
  public void testGetNonExistingItemReturnsNull() {
    SwipeItem item = swipeItemHolder.getItem( 0 );
    
    assertNull( item );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testAddItemTwiceFails() {
    swipeItemHolder.addItem( 0, mock( SwipeItem.class ) );
    swipeItemHolder.addItem( 0, mock( SwipeItem.class ) );
  }
  
  @Test
  public void testGetItem() {
    SwipeItem item = mock( SwipeItem.class );
    swipeItemHolder.addItem( 0, item );
    
    SwipeItem actualItem = swipeItemHolder.getItem( 0 );
    
    assertSame( item, actualItem );
  }
  
  @Test
  public void testHasItem() {
    SwipeItem item = mock( SwipeItem.class );
    swipeItemHolder.addItem( 0, item );
    
    assertTrue( swipeItemHolder.hasItem( 0 ) );
    assertFalse( swipeItemHolder.hasItem( 1 ) );
  }
  
  @Test
  public void testGetItemAfterRemoveReturnsNull() {
    SwipeItem item = mock( SwipeItem.class );
    swipeItemHolder.addItem( 0, item );
    
    swipeItemHolder.removeItem( 0 );
    
    SwipeItem actualItem = swipeItemHolder.getItem( 0 );
    assertNull( actualItem );
  }
  
  @Test( expected = IllegalStateException.class )
  public void testSetContentForNonExistingItemFails() {
    swipeItemHolder.setContentForItem( 0, mock( Composite.class ) );
  }
  
  @Test
  public void testAddWithContentSetsContent() {
    SwipeItem item = mock( SwipeItem.class );
    Composite content = mock( Composite.class );
    
    swipeItemHolder.addItem( 0, item, content );
    
    Control actualContent = swipeItemHolder.getContentForItem( 0 );
    assertSame( content, actualContent );
    assertTrue( swipeItemHolder.isLoaded( 0 ) );
  }
  
  @Test
  public void testSetContentSetsLoadedState() {
    SwipeItem item = mock( SwipeItem.class );
    swipeItemHolder.addItem( 0, item );
    
    swipeItemHolder.setContentForItem( 0, mock( Composite.class ) );
    
    assertTrue( swipeItemHolder.isLoaded( 0 ) );
  }
  
  @Test
  public void testGetContent() {
    SwipeItem item = mock( SwipeItem.class );
    swipeItemHolder.addItem( 0, item );
    Composite content = mock( Composite.class );
    swipeItemHolder.setContentForItem( 0, content );
    
    Control actualContent = swipeItemHolder.getContentForItem( 0 );
    
    assertSame( content, actualContent );
  }
  
  @Test
  public void testRemoveItemAlsoRemovesContent() {
    SwipeItem item = mock( SwipeItem.class );
    Composite content = mock( Composite.class );
    swipeItemHolder.addItem( 0, item, content );

    swipeItemHolder.removeItem( 0 );
    
    Control actualContent = swipeItemHolder.getContentForItem( 0 );
    assertNull( actualContent );
  }
  
  @Test
  public void testGetContentReturnsNullAfterRemove() {
    SwipeItem item = mock( SwipeItem.class );
    swipeItemHolder.addItem( 0, item );
    Composite content = mock( Composite.class );
    swipeItemHolder.setContentForItem( 0, content );
    
    swipeItemHolder.removeContentForItem( 0 );
    
    Control actualContent = swipeItemHolder.getContentForItem( 0 );
    assertNull( actualContent );
  }
  
  @Test
  public void testRemoveContentDisposesContent() {
    SwipeItem item = mock( SwipeItem.class );
    swipeItemHolder.addItem( 0, item );
    Composite content = mock( Composite.class );
    swipeItemHolder.setContentForItem( 0, content );
    
    swipeItemHolder.removeContentForItem( 0 );
    
    verify( content ).dispose();
  }
  
  @Test
  public void testRemovAllItems() {
    SwipeItem item = mock( SwipeItem.class );
    SwipeItem item2 = mock( SwipeItem.class );
    Composite content = mock( Composite.class );
    swipeItemHolder.addItem( 0, item, content );
    swipeItemHolder.addItem( 1, item2, content );
    
    swipeItemHolder.removeAllItems();
    
    assertNull( swipeItemHolder.getItem( 0 ) );
    assertNull( swipeItemHolder.getItem( 1 ) );
  }
}

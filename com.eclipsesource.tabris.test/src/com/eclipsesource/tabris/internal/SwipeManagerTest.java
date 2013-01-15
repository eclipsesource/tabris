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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.swt.SWT;
import org.junit.Test;

import com.eclipsesource.tabris.widgets.swipe.SwipeContext;
import com.eclipsesource.tabris.widgets.swipe.SwipeItemProvider;


public class SwipeManagerTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullProvider() {
    new SwipeManager( null );
  }

  @Test
  public void testGetProvider() {
    SwipeItemProvider provider = mock( SwipeItemProvider.class );

    SwipeManager manager = new SwipeManager( provider );

    assertSame( provider, manager.getProvider() );
  }

  @Test
  public void testGetContext() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );

    assertNotNull( manager.getContext() );
  }

  @Test
  public void testGetContextDoesNotCreateNewInstance() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );

    SwipeContext context = manager.getContext();
    SwipeContext context2 = manager.getContext();

    assertSame( context, context2 );
  }

  @Test
  public void testGetItemHolder() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );

    assertNotNull( manager.getItemHolder() );
  }

  @Test
  public void testGetItemHolderDoesNotCreateNewInstance() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );

    SwipeItemHolder holder1 = manager.getItemHolder();
    SwipeItemHolder holder2 = manager.getItemHolder();

    assertSame( holder1, holder2 );
  }

  @Test
  public void testGetItemIndexer() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );

    assertNotNull( manager.getIndexer() );
  }

  @Test
  public void testGetItemIndexerDoesNotCreateNewInstance() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );

    SwipeItemIndexer indexer1 = manager.getIndexer();
    SwipeItemIndexer indexer2 = manager.getIndexer();

    assertSame( indexer1, indexer2 );
  }

  @Test
  public void testMoveToRightIsAllowedWithoutLock() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );

    boolean allowed = manager.isMoveAllowed( 0, 1 );

    assertTrue( allowed );
  }

  @Test
  public void testMoveToRightIsNotAllowedWithLock() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );
    manager.lock( SWT.RIGHT, 0, true );

    boolean allowed = manager.isMoveAllowed( 0, 1 );

    assertFalse( allowed );
  }

  @Test
  public void testMoveToLeftIsAllowedWithoutLock() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );

    boolean allowed = manager.isMoveAllowed( 1, 0 );

    assertTrue( allowed );
  }

  @Test
  public void testMoveToLefttIsNotAllowedWithLock() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );
    manager.lock( SWT.LEFT, 1, true );

    boolean allowed = manager.isMoveAllowed( 1, 0 );

    assertFalse( allowed );
  }

  @Test
  public void testMoveToLefttIsAllowedAfterUnlock() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );
    manager.lock( SWT.LEFT, 1, true );
    manager.lock( SWT.LEFT, 1, false );

    boolean allowed = manager.isMoveAllowed( 1, 0 );

    assertTrue( allowed );
  }

  @Test
  public void testGetLeftLock() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );
    manager.lock( SWT.LEFT, 1, true );

    int leftLock = manager.getLeftLock();

    assertEquals( 1, leftLock );
  }

  @Test
  public void testGetRightLock() {
    SwipeManager manager = new SwipeManager( mock( SwipeItemProvider.class ) );
    manager.lock( SWT.RIGHT, 1, true );

    int rightLock = manager.getRightLock();

    assertEquals( 1, rightLock );
  }
}

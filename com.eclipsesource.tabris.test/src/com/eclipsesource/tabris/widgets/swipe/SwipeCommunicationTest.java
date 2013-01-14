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

import static com.eclipsesource.tabris.widgets.swipe.SwipeTest.mockProvider;
import static com.eclipsesource.tabris.widgets.swipe.SwipeTest.mockProviderSize;
import static com.eclipsesource.tabris.widgets.swipe.SwipeTest.mockSwipeItem;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.eclipsesource.tabris.test.TabrisTestUtil;
import com.eclipsesource.tabris.widgets.swipe.SwipeTest.TestItem;


@SuppressWarnings("restriction")
public class SwipeCommunicationTest {

  private RemoteObject remoteObject;
  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    shell = new Shell( new Display() );
    remoteObject = TabrisTestUtil.mockRemoteObject();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testSetsParent() {
    SwipeItemProvider itemProvider = mockProvider( 0 );

    Swipe swipe = new Swipe( shell, itemProvider );

    verify( remoteObject ).set( "parent", WidgetUtil.getId( swipe.getControl() ) );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testSendsMethodsOnInitialization() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    TestItem firstItem = mockSwipeItem( itemProvider, 0, true );
    TestItem secondItem = mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject ).call( eq( "itemLoaded" ), captor.capture() );
    order.verify( remoteObject ).set( "activeItem", 0 );
    order.verify( remoteObject ).call( eq( "itemLoaded" ), captor.capture() );
    assertLoadProperties( captor.getAllValues(), firstItem, secondItem );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testSendsNotEmptyOutOfRange() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );

    verify( remoteObject, never() ).call( eq( "removeItems" ), anyMap() );
  }

  private void assertLoadProperties( List<Map> allValues, TestItem firstItem, TestItem secondItem ) {
    Map properties1 = allValues.get( 0 );
    assertEquals( Integer.valueOf( 0 ), properties1.get( "index" ) );
    assertEquals( WidgetUtil.getId( firstItem.getLoadedComposite() ), properties1.get( "content" ) );
    Map properties2 = allValues.get( 1 );
    assertEquals( Integer.valueOf( 1 ), properties2.get( "index" ) );
    assertEquals( WidgetUtil.getId( secondItem.getLoadedComposite() ), properties2.get( "content" ) );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testSendsOutOfRangeItems() {
    SwipeItemProvider itemProvider = mockProvider( 4 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 1 );
    swipe.show( 2 );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject ).call( eq( "removeItems" ), captor.capture() );
    int[] items = ( int[] )captor.getValue().get( "items" );
    assertArrayEquals( new int[] { 0 }, items );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testSendsLockLeft() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.lock( 0, SWT.LEFT );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject ).call( eq( "lockLeft" ), captor.capture() );
    assertEquals( Integer.valueOf( 0 ), captor.getValue().get( "index" ) );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testSendsLockRight() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.lock( 0, SWT.RIGHT );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject ).call( eq( "lockRight" ), captor.capture() );
    assertEquals( Integer.valueOf( 0 ), captor.getValue().get( "index" ) );
  }

  @Test
  public void testSendsUnlockLeft() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.unlock( SWT.LEFT );

    verify( remoteObject ).call( "unlockLeft", null );
  }

  @Test
  public void testSendsUnlockRight() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.unlock( SWT.RIGHT );

    verify( remoteObject ).call( "unlockRight", null );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testDoesRespectItemsSizeWhenShiftingLeft() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.setCacheSize( 2 );

    swipe.show( 2 );
    swipe.show( 1 );

    verify( remoteObject, never() ).call( eq( "removeItems" ), anyMap() );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testDoesNotRemoveUnrelatedItemAndLoadsNext() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    mockSwipeItem( itemProvider, 4, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    mockProviderSize( itemProvider, 5 );
    swipe.show( 4 );

    mockSwipeItem( itemProvider, 5, true );
    mockProviderSize( itemProvider, 6 );
    swipe.refresh();

    verify( remoteObject, never() ).call( eq( "removeItems" ), anyMap() );
    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject, times( 3 ) ).call( eq( "itemLoaded" ), captor.capture() );
    assertEquals( Integer.valueOf( 5 ), captor.getAllValues().get( 2 ).get( "index" ) );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testJumpSkipsLoadingOfUnneededItem() {
    SwipeItemProvider itemProvider = mockProvider( 5 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    mockSwipeItem( itemProvider, 4, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );
    swipe.show( 4 );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject, times( 4 ) ).call( eq( "itemLoaded" ), captor.capture() );
    assertEquals( Integer.valueOf( 0 ), captor.getAllValues().get( 0 ).get( "index" ) );
    assertEquals( Integer.valueOf( 1 ), captor.getAllValues().get( 1 ).get( "index" ) );
    assertEquals( Integer.valueOf( 3 ), captor.getAllValues().get( 2 ).get( "index" ) );
    assertEquals( Integer.valueOf( 4 ), captor.getAllValues().get( 3 ).get( "index" ) );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testJumpRemovesOutOfRangeItem() {
    SwipeItemProvider itemProvider = mockProvider( 5 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    mockSwipeItem( itemProvider, 4, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );
    swipe.show( 4 );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject, times( 1 ) ).call( eq( "removeItems" ), captor.capture() );
    int[] actualIndexes = ( int[] )captor.getValue().get( "items" );
    assertArrayEquals( new int[] { 0, 1 }, actualIndexes );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testIncrementalDeleteSendsRemoveMessages() {
    SwipeItemProvider itemProvider = mockProvider( 5 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    mockSwipeItem( itemProvider, 4, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );
    mockProviderSize( itemProvider, 2 );
    swipe.refresh();
    mockProviderSize( itemProvider, 1 );
    swipe.refresh();

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( remoteObject, times( 1 ) ).call( eq( "removeItems" ), captor.capture() );
    int[] actualIndexes = ( int[] )captor.getValue().get( "items" );
    assertArrayEquals( new int[] { 1 }, actualIndexes );
  }

  @Test
  public void testActivatesItemOnlyOnceOnRefresh() {
    SwipeItemProvider itemProvider = mockProvider( 5 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    mockSwipeItem( itemProvider, 4, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );
    mockProviderSize( itemProvider, 2 );
    swipe.refresh();
    mockProviderSize( itemProvider, 1 );
    swipe.refresh();

    verify( remoteObject ).set( "activeItem", 0 );
  }

}

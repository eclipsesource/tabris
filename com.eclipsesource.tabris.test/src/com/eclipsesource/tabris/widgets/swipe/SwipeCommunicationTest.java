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
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.RemoteObject;
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
  public void testSendsMethodsOnInitialization() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    TestItem firstItem = mockSwipeItem( itemProvider, 0, true );
    TestItem secondItem = mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    InOrder order = inOrder( remoteObject );
    order.verify( remoteObject ).call( eq( "add" ), captor.capture() );
    order.verify( remoteObject ).set( "active", 0 );
    order.verify( remoteObject ).call( eq( "add" ), captor.capture() );
    assertLoadProperties( captor.getAllValues(), firstItem, secondItem );
  }

  @Test
  public void testSendsNotEmptyOutOfRange() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );

    verify( remoteObject, never() ).call( eq( "remove" ), any( JsonObject.class ) );
  }

  private void assertLoadProperties( List<JsonObject> list, TestItem firstItem, TestItem secondItem ) {
    JsonObject properties1 = list.get( 0 );
    assertEquals( 0, properties1.get( "index" ).asInt() );
    assertEquals( WidgetUtil.getId( firstItem.getLoadedComposite() ), properties1.get( "control" ).asString() );
    JsonObject properties2 = list.get( 1 );
    assertEquals( 1, properties2.get( "index" ).asInt() );
    assertEquals( WidgetUtil.getId( secondItem.getLoadedComposite() ), properties2.get( "control" ).asString() );
  }

  @Test
  public void testSendsOutOfRangeItems() {
    SwipeItemProvider itemProvider = mockProvider( 4 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 1 );
    swipe.show( 2 );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "remove" ), captor.capture() );
    JsonArray items = captor.getValue().get( "items" ).asArray();
    assertEquals( new JsonArray().add( 0 ), items );
  }

  @Test
  public void testSendsLockLeft() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.lock( SWT.LEFT );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "lockLeft" ), captor.capture() );
    assertEquals( 0, captor.getValue().get( "index" ).asInt() );
  }

  @Test
  public void testSendsLockRight() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.lock( SWT.RIGHT );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject ).call( eq( "lockRight" ), captor.capture() );
    assertEquals( 0, captor.getValue().get( "index" ).asInt() );
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
  public void testSendsUnlockRightWhenItemRemoved() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 2 );
    swipe.lock( SWT.RIGHT );
    swipe.show( 1 );
    mockProviderSize( itemProvider, 2 );
    swipe.refresh();

    verify( remoteObject ).call( "unlockRight", null );
  }

  @Test
  public void testDoesRespectItemsSizeWhenShiftingLeft() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.setCacheSize( 2 );

    swipe.show( 2 );
    swipe.show( 1 );

    verify( remoteObject, never() ).call( eq( "removeItems" ), any( JsonObject.class ) );
  }

  @Test
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

    verify( remoteObject, never() ).call( eq( "remove" ), any( JsonObject.class ) );
    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject, times( 3 ) ).call( eq( "add" ), captor.capture() );
    assertEquals( 5, captor.getAllValues().get( 2 ).get( "index" ).asInt() );
  }

  @Test
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

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject, times( 4 ) ).call( eq( "add" ), captor.capture() );
    assertEquals( 0, captor.getAllValues().get( 0 ).get( "index" ).asInt() );
    assertEquals( 1, captor.getAllValues().get( 1 ).get( "index" ).asInt() );
    assertEquals( 3, captor.getAllValues().get( 2 ).get( "index" ).asInt() );
    assertEquals( 4, captor.getAllValues().get( 3 ).get( "index" ).asInt() );
  }

  @Test
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

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject, times( 1 ) ).call( eq( "remove" ), captor.capture() );
    JsonArray actualIndexes = captor.getValue().get( "items" ).asArray();
    assertEquals( new JsonArray().add( 0 ).add( 1 ), actualIndexes );
  }

  @Test
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

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject, times( 1 ) ).call( eq( "remove" ), captor.capture() );
    JsonArray actualIndexes = captor.getValue().get( "items" ).asArray();
    assertEquals( new JsonArray().add( 1 ), actualIndexes );
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

    verify( remoteObject ).set( "active", 0 );
  }

}

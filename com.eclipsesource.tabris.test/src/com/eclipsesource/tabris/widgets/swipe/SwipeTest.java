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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.SWIPE;
import static com.eclipsesource.tabris.test.TabrisTestUtil.mockRemoteObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import com.eclipsesource.tabris.internal.SwipeItemHolder;
import com.eclipsesource.tabris.internal.ZIndexStackLayout;
import com.eclipsesource.tabris.test.RWTRunner;


@RunWith( RWTRunner.class )
public class SwipeTest {

  private Shell shell;
  private RemoteObject remoteObject;

  @Before
  public void setUp() {
    remoteObject = mockRemoteObject();
    shell = new Shell( new Display() );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( Swipe.class ) );
  }

  @Test
  public void testSwipeListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( SwipeListener.class ) );
  }

  @Test
  public void testSwipeItemProviderIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( SwipeItemProvider.class ) );
  }

  @Test
  public void testSwipeItemIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( SwipeItem.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParent() {
    new Swipe( null, mock( SwipeItemProvider.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullProvider() {
    new Swipe( mock( Composite.class ), null );
  }

  @Test
  public void testSetsSwipeData() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );
    Composite container = swipe.getContainer();

    Object data = container.getData( SWIPE.getKey() );

    assertEquals( Boolean.TRUE, data );
  }

  @Test
  public void testGetControl() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );

    Control control = swipe.getControl();

    assertEquals( shell, control.getParent() );
  }

  @Test
  public void testControlUsesSwipeLayout() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );
    Composite control = ( Composite )swipe.getControl();

    assertTrue( control.getLayout() instanceof ZIndexStackLayout );
  }

  @Test
  public void testGetControlDoesNotCreateNewControl() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );

    Control control = swipe.getControl();
    Control control2 = swipe.getControl();

    assertSame( control, control2 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testShowWithNegativeValueFails() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( -1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testShowWithNonExistingItemFails() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 23 );
  }

  @Test
  public void testShowsZeroByDefault() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    SwipeItem firstItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem secondItem = mockSwipeItem( itemProvider, 1, true );
    new Swipe( shell, itemProvider );

    InOrder order = inOrder( firstItem, secondItem );
    order.verify( firstItem ).load( any( Composite.class ) );
    order.verify( firstItem ).activate( any( SwipeContext.class ) );
    order.verify( secondItem ).load( any( Composite.class ) );
  }

  @Test
  public void testInitializesWithFirstTwoItemsOnShowZero() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    SwipeItem firstItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem secondItem = mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );

    InOrder order = inOrder( firstItem, secondItem );
    order.verify( firstItem ).load( any( Composite.class ) );
    order.verify( firstItem ).activate( any( SwipeContext.class ) );
    order.verify( secondItem ).load( any( Composite.class ) );
  }

  @Test
  public void testInitializesWithFirstTwoItemsOnShowZeroWithBiggerCache() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    SwipeItem firstItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem secondItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem thirdItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    mockProviderSize( itemProvider, 3 );

    swipe.setCacheSize( 2 );
    swipe.show( 0 );

    InOrder order = inOrder( firstItem, secondItem, thirdItem );
    order.verify( firstItem ).load( any( Composite.class ) );
    order.verify( firstItem ).activate( any( SwipeContext.class ) );
    order.verify( secondItem ).load( any( Composite.class ) );
    order.verify( thirdItem ).load( any( Composite.class ) );
  }

  @Test
  public void testLoadsItemsOnlyOnce() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    mockProviderSize( itemProvider, 2 );

    swipe.show( 0 );
    swipe.show( 0 );

    verify( nextItem ).isPreloadable();
    verify( currentItem ).load( any( Composite.class ) );
    verify( currentItem ).activate( any( SwipeContext.class ) );
    verify( nextItem ).load( any( Composite.class ) );
  }

  @Test
  public void testPreloadsNextItem() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    mockSwipeItem( itemProvider, 0, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );

    verify( nextItem ).isPreloadable();
    verify( nextItem ).load( any( Composite.class ) );
  }

  @Test
  public void testPreloadsNextItemsWithBiggerCache() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    mockSwipeItem( itemProvider, 0, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem thridItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    mockProviderSize( itemProvider, 3 );

    swipe.setCacheSize( 2 );
    swipe.show( 0 );

    verify( nextItem ).isPreloadable();
    verify( nextItem ).load( any( Composite.class ) );
    verify( thridItem ).isPreloadable();
    verify( thridItem ).load( any( Composite.class ) );
  }

  @Test
  public void testRefreshLoadsNewItems() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );
    mockProviderSize( itemProvider, 2 );
    swipe.refresh();

    verify( nextItem ).isPreloadable();
    verify( nextItem ).load( any( Composite.class ) );
  }

  @Test
  public void testSetCacheSizeTriggersRefresh() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    SwipeItem itemToLoad = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.setCacheSize( 2 );

    verify( itemToLoad ).isPreloadable();
    verify( itemToLoad ).load( any( Composite.class ) );
  }

  @Test
  public void testDeactivatesPreviousItemAndLoadsNextFromLeft() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );
    swipe.show( 1 );

    InOrder order = inOrder( previousItem, currentItem, nextItem );
    order.verify( previousItem ).deactivate( any( SwipeContext.class ) );
    order.verify( currentItem ).activate( any( SwipeContext.class ) );
    order.verify( nextItem ).load( any( Composite.class ) );
  }

  @Test
  public void testDeactivatesPreviousItemAndLoadsNextFromLeftWithBiggerCache() {
    SwipeItemProvider itemProvider = mockProvider( 5 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    SwipeItem thirdItem = mockSwipeItem( itemProvider, 2, true );
    SwipeItem fourthItem = mockSwipeItem( itemProvider, 3, true );
    SwipeItem fifthItem = mockSwipeItem( itemProvider, 4, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.setCacheSize( 2 );

    swipe.show( 3 );
    swipe.show( 4 );

    InOrder order = inOrder( thirdItem, fourthItem, fifthItem );
    order.verify( fourthItem ).load( any( Composite.class ) );
    order.verify( fourthItem ).activate( any( SwipeContext.class ) );
    order.verify( fifthItem ).load( any( Composite.class ) );
    order.verify( fourthItem ).deactivate( any( SwipeContext.class ) );
  }

  @Test
  public void testDeactivatesPreviousItemAndLoadsNextFromRight() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    mockProviderSize( itemProvider, 3 );

    swipe.show( 2 );
    swipe.show( 1 );

    verify( previousItem ).deactivate( any( SwipeContext.class ) );
    verify( currentItem ).load( any( Composite.class ) );
    verify( currentItem ).activate( any( SwipeContext.class ) );
    verify( nextItem ).load( any( Composite.class ) );
  }

  @Test
  public void testDeactivatesPreviousItemAndLoadsNextFromRightWithBiggerCache() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    SwipeItem zeroItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem firstItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem secondItem = mockSwipeItem( itemProvider, 2, true );
    SwipeItem thirdItem = mockSwipeItem( itemProvider, 3, true );
    SwipeItem fourthItem = mockSwipeItem( itemProvider, 4, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.setCacheSize( 2 );
    mockProviderSize( itemProvider, 5 );

    swipe.show( 3 );
    swipe.show( 2 );

    verify( thirdItem ).load( any( Composite.class ) );
    verify( thirdItem ).activate( any( SwipeContext.class ) );
    verify( firstItem ).load( any( Composite.class ) );
    verify( secondItem ).load( any( Composite.class ) );
    verify( fourthItem ).load( any( Composite.class ) );
    verify( thirdItem ).deactivate( any( SwipeContext.class ) );
    verify( secondItem ).activate( any( SwipeContext.class ) );
    verify( zeroItem ).load( any( Composite.class ) );
  }

  @Test
  public void testDisposesItemContentWhenOutOfRangeFromLeft() {
    SwipeItemProvider itemProvider = mockProvider( 4 );
    TestItem outOfRangeItem = mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 1 );
    swipe.show( 2 );

    verify( outOfRangeItem ).load( any( Composite.class ) );
    assertTrue( outOfRangeItem.getLoadedComposite().isDisposed() );
  }

  @Test
  public void testDisposesItemContentWhenOutOfRangeFromRight() {
    SwipeItemProvider itemProvider = mockProvider( 4 );
    TestItem outOfRangeItem = mockSwipeItem( itemProvider, 3, true );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 2 );
    swipe.show( 1 );

    verify( outOfRangeItem ).load( any( Composite.class ) );
    assertTrue( outOfRangeItem.getLoadedComposite().isDisposed() );
  }

  @Test
  public void testLoadsPreviousAndNextItem() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 1 );

    InOrder order = inOrder( previousItem, currentItem, nextItem );
    order.verify( previousItem ).load( any( Composite.class ) );
    order.verify( currentItem ).activate( any( SwipeContext.class ) );
    order.verify( nextItem ).load( any( Composite.class ) );
  }

  @Test
  public void testDoesNotPreLoadPreviousAndNextItemWhenForbidden() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 0, false );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 2, false );
    Swipe swipe = new Swipe( shell, itemProvider );
    mockProviderSize( itemProvider, 3 );

    swipe.show( 1 );

    InOrder order = inOrder( previousItem, currentItem, nextItem );
    order.verify( previousItem, never() ).load( any( Composite.class ) );
    order.verify( currentItem ).load( any( Composite.class ) );
    order.verify( currentItem ).activate( any( SwipeContext.class ) );
    order.verify( nextItem, never() ).load( any( Composite.class ) );
  }

  @Test
  public void testDoesRespectItemsSize() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    mockSwipeItem( itemProvider, 0, true );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 2 );

    InOrder order = inOrder( previousItem, currentItem, itemProvider );
    order.verify( previousItem ).load( any( Composite.class ) );
    order.verify( currentItem ).load( any( Composite.class ) );
    order.verify( currentItem ).activate( any( SwipeContext.class ) );
    order.verify( itemProvider, never() ).getItem( 3 );
  }

  @Test
  public void testDoesRespectItemsSizeWhenShiftingLeft() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    mockSwipeItem( itemProvider, 0, true );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.setCacheSize( 2 );

    swipe.show( 2 );
    swipe.show( 1 );

    InOrder order = inOrder( previousItem, currentItem, itemProvider );
    order.verify( previousItem ).load( any( Composite.class ) );
    order.verify( currentItem ).load( any( Composite.class ) );
    order.verify( currentItem ).activate( any( SwipeContext.class ) );
    order.verify( itemProvider, never() ).getItem( 3 );
    order.verify( itemProvider, never() ).getItem( 4 );
  }

  @Test
  public void testRotateLoadsItemOnlyOnce() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    SwipeItem middleItem = mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );
    swipe.show( 1 );
    swipe.show( 2 );
    swipe.show( 1 );
    swipe.show( 0 );

    verify( middleItem, times( 1 ) ).load( any( Composite.class ) );
  }

  @Test
  public void testRotateLoadsItemMultipleTimesWhenItWasOutOfRange() {
    SwipeItemProvider itemProvider = mockProvider( 4 );
    SwipeItem middleItem = mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );
    swipe.show( 1 );
    swipe.show( 2 );
    swipe.show( 3 );
    swipe.show( 2 );
    swipe.show( 1 );

    verify( middleItem, times( 2 ) ).load( any( Composite.class ) );
  }

  @Test
  public void testRotateActivatesItemTwice() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    SwipeItem middleItem = mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );
    swipe.show( 1 );
    swipe.show( 2 );
    swipe.show( 1 );
    swipe.show( 0 );

    verify( middleItem, times( 2 ) ).activate( any( SwipeContext.class ) );
  }

  @Test
  public void testFlipFlopActivatesItemMultipleTimesButLoadsItOnlyOnce() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    SwipeItem middleItem = mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    for( int i = 0; i < 10; i++ ) {
      swipe.show( 0 );
      swipe.show( 1 );
    }

    verify( middleItem, times( 1 ) ).load( any( Composite.class ) );
    verify( middleItem, times( 10 ) ).activate( any( SwipeContext.class ) );
  }

  @Test
  public void testShowSetsTopControlOnStack() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    TestItem item = mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 0 );

    Composite control = ( Composite )swipe.getControl();
    ZIndexStackLayout layout = ( ZIndexStackLayout )control.getLayout();
    assertSame( item.getLoadedComposite(), layout.getOnTopControl() );
  }

  @Test
  public void testShowSetsTopControlOnStackWithMoultipleItems() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    TestItem item = mockSwipeItem( itemProvider, 0, true );
    TestItem item2 = mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    Composite control = ( Composite )swipe.getControl();
    ZIndexStackLayout layout = ( ZIndexStackLayout )control.getLayout();

    swipe.show( 0 );
    assertSame( item.getLoadedComposite(), layout.getOnTopControl() );

    swipe.show( 1 );
    assertSame( item2.getLoadedComposite(), layout.getOnTopControl() );
  }

  @Test
  public void testGetSwipeContextDoesNotCreateNewContext() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );

    SwipeContext context1 = swipe.getContext();
    SwipeContext context2 = swipe.getContext();

    assertSame( context1, context2 );
  }

  @Test
  public void testUsesSameContextForActivation() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeContext context = swipe.getContext();

    swipe.show( 0 );
    swipe.show( 1 );
    swipe.show( 2 );

    InOrder order = inOrder( previousItem, currentItem, nextItem );
    order.verify( previousItem ).activate( context );
    order.verify( currentItem ).activate( context );
    order.verify( nextItem ).activate( context );
  }

  @Test
  public void testUsesSameContextForDeactivation() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeContext context = swipe.getContext();

    swipe.show( 0 );
    swipe.show( 1 );
    swipe.show( 2 );
    swipe.show( 1 );

    InOrder order = inOrder( previousItem, currentItem, nextItem );
    order.verify( previousItem ).deactivate( context );
    order.verify( currentItem ).deactivate( context );
    order.verify( nextItem ).deactivate( context );
  }

  @Test
  public void testHandlesJumpCorreclty() {
    SwipeItemProvider itemProvider = mockProvider( 6 );
    SwipeItem firstPreviousItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem firstCurrentItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem firstNextItem = mockSwipeItem( itemProvider, 2, true );
    SwipeItem secondPreviousItem = mockSwipeItem( itemProvider, 3, true );
    SwipeItem secondCurrentItem = mockSwipeItem( itemProvider, 4, true );
    SwipeItem secondNextItem = mockSwipeItem( itemProvider, 5, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeContext context = swipe.getContext();

    swipe.show( 1 );
    swipe.show( 4 );

    InOrder order = inOrder( firstPreviousItem, firstCurrentItem, firstNextItem,
                             secondPreviousItem, secondCurrentItem, secondNextItem );
    order.verify( firstPreviousItem ).load( any( Composite.class ) );
    order.verify( firstCurrentItem ).load( any( Composite.class ) );
    order.verify( firstCurrentItem ).activate( context );
    order.verify( firstNextItem ).load( any( Composite.class ) );
    order.verify( firstCurrentItem ).deactivate( context );
    order.verify( secondPreviousItem ).load( any( Composite.class ) );
    order.verify( secondCurrentItem ).load( any( Composite.class ) );
    order.verify( secondCurrentItem ).activate( context );
    order.verify( secondNextItem ).load( any( Composite.class ) );
  }

  @Test
  public void testRegistersDisposeListenerOnControl() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );
    Control control = swipe.getControl();

    control.dispose();

    verify( remoteObject ).destroy();
  }

  @Test
  public void testDisposeDisposesControl() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );
    Control control = swipe.getControl();

    swipe.dispose();

    assertTrue( control.isDisposed() );
  }

  @Test
  public void testDisposeDestroysRemoteObject() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.dispose();

    verify( remoteObject ).destroy();
  }

  @Test
  public void testDisposeRemovesItems() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeItemHolder itemHolder = swipe.getItemHolder();
    swipe.show( 0 );

    swipe.dispose();

    assertNull( itemHolder.getItem( 0 ) );
  }

  @Test
  public void testDisposeNotifiesListeners() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeListener listener = mock( SwipeListener.class );
    swipe.addSwipeListener( listener );
    swipe.show( 0 );

    swipe.dispose();

    verify( listener ).disposed( swipe.getContext() );
  }

  @Test( expected = IllegalStateException.class )
  public void testShowAfterDisposeFails() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.dispose();
    swipe.show( 0 );
  }

  @Test( expected = IllegalStateException.class )
  public void testSetCacheSizeAfterDisposeFails() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.dispose();
    swipe.setCacheSize( 23 );
  }

  @Test( expected = IllegalStateException.class )
  public void testAddListenerAfterDisposeFails() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.dispose();
    swipe.addSwipeListener( mock( SwipeListener.class ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testRemoveListenerAfterDisposeFails() {
    SwipeItemProvider itemProvider = mockProvider( 1 );
    mockSwipeItem( itemProvider, 0, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeListener listener = mock( SwipeListener.class );
    swipe.addSwipeListener( listener );

    swipe.dispose();
    swipe.removeSwipeListener( listener );
  }

  @Test
  public void testNotifiesListenerAboutLoadingAndActivation() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeListener listener = mock( SwipeListener.class );
    swipe.addSwipeListener( listener );
    mockProviderSize( itemProvider, 3 );

    swipe.show( 1 );

    InOrder order = inOrder( listener );
    order.verify( listener ).itemLoaded( previousItem, 0 );
    order.verify( listener ).itemActivated( eq( currentItem ), eq( 1 ), any( SwipeContext.class ) );
    order.verify( listener ).itemLoaded( nextItem, 2 );
  }

  @Test
  public void testNotifiesListenerAboutActivationFromLeft() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeListener listener = mock( SwipeListener.class );
    swipe.addSwipeListener( listener );
    mockProviderSize( itemProvider, 3 );

    swipe.show( 1 );
    swipe.show( 0 );

    InOrder order = inOrder( listener );
    order.verify( listener ).itemActivated( eq( currentItem ), eq( 1 ), any( SwipeContext.class ) );
    order.verify( listener ).itemActivated( eq( previousItem ), eq( 0 ), any( SwipeContext.class ) );
  }

  @Test
  public void testNotifiesListenerAboutDeactivation() {
    SwipeItemProvider itemProvider = mockProvider( 3 );
    SwipeItem previousItem = mockSwipeItem( itemProvider, 0, true );
    SwipeItem currentItem = mockSwipeItem( itemProvider, 1, true );
    SwipeItem nextItem = mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeListener listener = mock( SwipeListener.class );
    swipe.addSwipeListener( listener );

    swipe.show( 0 );
    swipe.show( 1 );

    InOrder order = inOrder( listener );
    order.verify( listener ).itemDeactivated( eq( previousItem ), eq( 0 ), any( SwipeContext.class ) );
    order.verify( listener ).itemActivated( eq( currentItem ), eq( 1 ), any( SwipeContext.class ) );
    order.verify( listener ).itemLoaded( nextItem, 2 );
  }

  @Test
  public void testNotifiesListenerAboutDeactivationWithBiggerCacheSize() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    mockSwipeItem( itemProvider, 4, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    SwipeListener listener = mock( SwipeListener.class );
    swipe.addSwipeListener( listener );
    swipe.setCacheSize( 2 );
    mockProviderSize( itemProvider, 5 );

    swipe.show( 2 );
    swipe.show( 3 );

    verify( listener, times( 1 ) ).itemDeactivated( any( SwipeItem.class ), anyInt(), any( SwipeContext.class ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testShowRightLockedFails() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.show( 0 );

    swipe.lock( SWT.RIGHT );

    swipe.show( 1 );
  }

  @Test
  public void testShowRightUnlockedFailsNot() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.show( 0 );

    swipe.lock( SWT.RIGHT );
    swipe.unlock( SWT.RIGHT );

    swipe.show( 1 );
  }

  @Test( expected = IllegalStateException.class )
  public void testShowLeftLockedFails() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.show( 1 );

    swipe.lock( SWT.LEFT );

    swipe.show( 0 );
  }

  @Test( expected = IllegalStateException.class )
  public void testShowLeftLockedFailsWithJump() {
    SwipeItemProvider itemProvider = mockProvider( 4 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.show( 2 );

    swipe.lock( SWT.LEFT );
    swipe.show( 3 );

    swipe.show( 0 );
  }

  @Test
  public void testShowLeftLockedFailsNotWhenJumpToLockedItem() {
    SwipeItemProvider itemProvider = mockProvider( 4 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    mockSwipeItem( itemProvider, 3, true );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.show( 1 );
    swipe.lock( SWT.LEFT );
    swipe.show( 2 );
    swipe.show( 3 );
    swipe.lock( SWT.RIGHT );
    swipe.show( 1 );
  }

  @Test
  public void testShowLeftUnlockedFailsNot() {
    SwipeItemProvider itemProvider = mockProvider( 2 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    swipe.show( 1 );

    swipe.lock( SWT.LEFT );
    swipe.unlock( SWT.LEFT );

    swipe.show( 0 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testLockFailsWIthInvalidDirection() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.lock( SWT.ABORT );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testUnLockFailsWIthInvalidDirection() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    Swipe swipe = new Swipe( shell, itemProvider );

    swipe.unlock( SWT.ABORT );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWhenRemovingActiveItem() {
    SwipeItemProvider itemProvider = mockProvider( 0 );
    mockSwipeItem( itemProvider, 0, true );
    mockSwipeItem( itemProvider, 1, true );
    mockSwipeItem( itemProvider, 2, true );
    Swipe swipe = new Swipe( shell, itemProvider );
    mockProviderSize( itemProvider, 3 );

    swipe.show( 1 );
    mockProviderSize( itemProvider, 1 );
    swipe.refresh();
  }

  public static TestItem mockSwipeItem( SwipeItemProvider itemProvider, int itemIndex, boolean preloadable ) {
    TestItem swipeItem = spy( new TestItem() );
    when( itemProvider.getItem( itemIndex ) ).thenReturn( swipeItem );
    doReturn( Boolean.valueOf( preloadable ) ).when( swipeItem ).isPreloadable();
    return swipeItem;
  }

  public static SwipeItemProvider mockProvider( int itemCount ) {
    SwipeItemProvider provider = mock( SwipeItemProvider.class );
    doReturn( Integer.valueOf( itemCount ) ).when( provider ).getItemCount();
    return provider;
  }

  public static void mockProviderSize( SwipeItemProvider provider, int itemCount ) {
    doReturn( Integer.valueOf( itemCount ) ).when( provider ).getItemCount();
  }

  public static class TestItem implements SwipeItem {

    private Composite composite;

    public Composite getLoadedComposite() {
      return composite;
    }

    @Override
    public Composite load( Composite parent ) {
      composite = new Composite( parent, SWT.NONE );
      return composite;
    }

    @Override
    public void activate( SwipeContext context ) {
      // do nothing, used for mocking
    }

    @Override
    public void deactivate( SwipeContext context ) {
      // do nothing, used for mocking
    }

    @Override
    public boolean isPreloadable() {
      return true;
    }

  }

}

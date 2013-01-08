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
import static com.eclipsesource.tabris.widgets.swipe.SwipeTest.mockSwipeItem;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
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
    verify( remoteObject, times( 2 ) ).call( eq( "removeItems" ), captor.capture() );
    int[] items = ( int[] )captor.getAllValues().get( 1 ).get( "items" );
    assertArrayEquals( new int[] { 0 }, items );
  }

}

/*******************************************************************************
 * Copyright (c) 2013, 2019 EclipseSource and others.
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.widgets.Control;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.widgets.swipe.Swipe;


public class SwipeOperationHandlerTest {

  private Swipe swipe;
  private Control control;

  @Before
  public void setUp() {
    swipe = mock( Swipe.class );
    control = mock( Control.class );
    when( swipe.getControl() ).thenReturn( control );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSwipe() {
    new SwipeOperationHandler( null );
  }

  @Test
  public void testNotifiesAboutSwipeEvent() {
    SwipeOperationHandler handler = new SwipeOperationHandler( swipe );
    JsonObject properties = new JsonObject();
    properties.add( "item", 0 );

    handler.handleNotify( "Swipe", properties );

    verify( swipe ).show( 0 );
  }

  @Test
  public void testNotifiesAboutSwipeEvent_withDisposedControl() {
    doReturn( Boolean.TRUE ).when( control ).isDisposed();
    SwipeOperationHandler handler = new SwipeOperationHandler( swipe );
    JsonObject properties = new JsonObject();
    properties.add( "item", 0 );

    handler.handleNotify( "Swipe", properties );

    verify( swipe, never() ).show( 0 );
  }

  @Test
  public void testSetsActiveClientItem() {
    SwipeOperationHandler handler = new SwipeOperationHandler( swipe );
    JsonObject properties = new JsonObject();
    properties.add( "item", 0 );

    handler.handleNotify( "Swipe", properties );

    int activeClientItem = handler.getActiveClientItem();
    assertEquals( activeClientItem, 0 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSwipeEventFailsWithoutItemProperty() {
    SwipeOperationHandler handler = new SwipeOperationHandler( swipe );
    JsonObject properties = new JsonObject();

    handler.handleNotify( "Swipe", properties );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSwipeEventFailsWithNoIntegerItemProperty() {
    SwipeOperationHandler handler = new SwipeOperationHandler( swipe );
    JsonObject properties = new JsonObject();
    properties.add( "item", "0" );

    handler.handleNotify( "Swipe", properties );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSwipeEventFailsWithNullProperties() {
    SwipeOperationHandler handler = new SwipeOperationHandler( swipe );

    handler.handleNotify( "Swipe", null );
  }

  @Test
  public void testCanSetActiveClientItem() {
    SwipeOperationHandler handler = new SwipeOperationHandler( swipe );

    handler.setActiveClientItem( 23 );

    int activeClientItem = handler.getActiveClientItem();
    assertEquals( 23, activeClientItem );
  }
}

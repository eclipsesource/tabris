/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.widgets.RefreshListener;


@SuppressWarnings("restriction")
public class RefreshHandlerTest {

  @Rule
  public TabrisEnvironment enironment = new TabrisEnvironment();

  @Test
  public void testHasId() {
    RefreshHandler handler = new RefreshHandler();

    String id = handler.getId();

    assertEquals( enironment.getRemoteObject().getId(), id );
  }

  @Test
  public void testHookItSelfToWidget() {
    Shell shell = new Shell( new Display() );
    RefreshHandler handler = new RefreshHandler();

    handler.hookToWidget( shell );

    verify( enironment.getRemoteObject() ).set( "widget", WidgetUtil.getId( shell ) );
  }

  @Test
  public void testHasListeners() {
    RefreshHandler handler = new RefreshHandler();
    RefreshListener listener1 = mock( RefreshListener.class );
    RefreshListener listener2 = mock( RefreshListener.class );

    handler.addRefreshListener( listener1 );
    handler.addRefreshListener( listener2 );

    List<RefreshListener> refreshListeners = handler.getRefreshListeners();
    assertEquals( 2, refreshListeners.size() );
    assertTrue( refreshListeners.contains( listener1 ) );
    assertTrue( refreshListeners.contains( listener2 ) );
  }

  @Test
  public void testAddListenerCreatesListen() {
    RefreshHandler handler = new RefreshHandler();

    handler.addRefreshListener( mock( RefreshListener.class ) );

    verify( enironment.getRemoteObject() ).listen( "Refresh", true );
  }

  @Test
  public void testAddListenersCreatesListenOnce() {
    RefreshHandler handler = new RefreshHandler();

    handler.addRefreshListener( mock( RefreshListener.class ) );
    handler.addRefreshListener( mock( RefreshListener.class ) );

    verify( enironment.getRemoteObject(), times( 1 ) ).listen( "Refresh", true );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddListenerFailsWithNullListener() {
    RefreshHandler handler = new RefreshHandler();

    handler.addRefreshListener( null );
  }

  @Test
  public void testRemovesListenerCreatesListen() {
    RefreshHandler handler = new RefreshHandler();
    RefreshListener listener = mock( RefreshListener.class );

    handler.addRefreshListener( listener );
    handler.removeRefreshListener( listener );

    verify( enironment.getRemoteObject() ).listen( "Refresh", false );
  }

  @Test
  public void testRemovesListenerCreatesListenOnlyIfNoListenersPresent() {
    RefreshHandler handler = new RefreshHandler();
    RefreshListener listener = mock( RefreshListener.class );
    handler.addRefreshListener( listener );
    handler.addRefreshListener( mock( RefreshListener.class ) );

    handler.removeRefreshListener( listener );

    verify( enironment.getRemoteObject(), never() ).listen( "Refresh", false );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemovesListenerFailsWithNullListener() {
    RefreshHandler handler = new RefreshHandler();

    handler.removeRefreshListener( null );
  }

  @Test
  public void testSetMessage() {
    RefreshHandler handler = new RefreshHandler();

    handler.setMessage( "foo" );

    verify( enironment.getRemoteObject() ).set( "message", JsonValue.valueOf( "foo" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetMessageFailsWithNull() {
    RefreshHandler handler = new RefreshHandler();

    handler.setMessage( null );
  }

  @Test
  public void testHasMessage() {
    RefreshHandler handler = new RefreshHandler();

    handler.setMessage( "foo" );
    String message = handler.getMessage();

    assertEquals( message, "foo" );
  }

  @Test
  public void testDoneCreateCall() {
    RefreshHandler handler = new RefreshHandler();

    handler.done();

    verify( enironment.getRemoteObject() ).call( "done", null );
  }

  @Test
  public void testNotifyNotofiesListeners() {
    RefreshHandler handler = new RefreshHandler();
    RefreshListener listener = mock( RefreshListener.class );
    handler.addRefreshListener( listener );

    enironment.dispatchNotify( "Refresh", null );

    verify( listener ).refresh();
  }
}

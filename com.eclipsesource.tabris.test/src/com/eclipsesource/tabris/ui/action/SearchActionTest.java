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
package com.eclipsesource.tabris.ui.action;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.remote.RemoteObject;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.RemoteAction;
import com.eclipsesource.tabris.internal.ui.RemoteActionHolder;
import com.eclipsesource.tabris.internal.ui.TestSearchAction;
import com.eclipsesource.tabris.ui.UI;

public class SearchActionTest {

  @Test
  public void testCallsOpenWithoutQuery() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteActionHolder.class ).setRemoteAction( createAction( remoteObject ) );

    searchAction.open();

    verify( remoteObject ).call( "open", null );
  }

  @Test
  public void testCallsExecuteOnOpen() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = spy( new TestSearchAction() );
    RemoteAction remoteAction = createAction( remoteObject );
    UI ui = mock( UI.class );
    when( remoteAction.getUI() ).thenReturn( ui );
    searchAction.getAdapter( RemoteActionHolder.class ).setRemoteAction( remoteAction );

    searchAction.open();

    verify( searchAction ).execute( ui );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetMessageFailsWithNull() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteActionHolder.class ).setRemoteAction( createAction( remoteObject ) );

    searchAction.setMessage( null );
  }

  @Test
  public void testSetMessage() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteActionHolder.class ).setRemoteAction( createAction( remoteObject ) );

    searchAction.setMessage( "foo" );

    verify( remoteObject ).set( "message", "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetQueryFailsWithNull() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteActionHolder.class ).setRemoteAction( createAction( remoteObject ) );

    searchAction.setQuery( null );
  }

  @Test
  public void testSetQuery() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    TestSearchAction searchAction = new TestSearchAction();
    searchAction.getAdapter( RemoteActionHolder.class ).setRemoteAction( createAction( remoteObject ) );

    searchAction.setQuery( "foo" );

    verify( remoteObject ).set( "query", "foo" );
  }

  @Test
  public void testHasRemoteObjectHolder() {
    TestSearchAction searchAction = new TestSearchAction();

    RemoteActionHolder remoteObjectHolder = searchAction.getAdapter( RemoteActionHolder.class );

    assertNotNull( remoteObjectHolder );
  }

  @Test
  public void testHasOneRemoteObjectHolder() {
    TestSearchAction searchAction = new TestSearchAction();

    RemoteActionHolder remoteObjectHolder = searchAction.getAdapter( RemoteActionHolder.class );
    RemoteActionHolder remoteObjectHolder2 = searchAction.getAdapter( RemoteActionHolder.class );

    assertSame( remoteObjectHolder, remoteObjectHolder2 );
  }

  private RemoteAction createAction( RemoteObject remoteObject ) {
    RemoteAction action = mock( RemoteAction.class );
    when( action.getRemoteObject() ).thenReturn( remoteObject );
    return action;
  }
}

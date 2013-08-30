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

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.TestSearchAction;
import com.eclipsesource.tabris.internal.ui.rendering.SearchActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.SearchActionRendererHolder;
import com.eclipsesource.tabris.ui.UI;

public class SearchActionTest {

  private TestSearchAction searchAction;
  private SearchActionRenderer searchActionRenderer;

  @Before
  public void setUp() {
    searchAction = new TestSearchAction();
    searchActionRenderer = mock( SearchActionRenderer.class );
  }

  @Test
  public void testCallsOpenWithoutQuery() {
    searchAction.getAdapter( SearchActionRendererHolder.class ).setSearchActionRenderer( searchActionRenderer );

    searchAction.open();

    verify( searchActionRenderer ).open();
  }

  @Test
  public void testCallsExecuteOnOpen() {
    searchAction = spy( new TestSearchAction() );
    UI ui = mock( UI.class );
    when( searchActionRenderer.getUI() ).thenReturn( ui );
    searchAction.getAdapter( SearchActionRendererHolder.class ).setSearchActionRenderer( searchActionRenderer );

    searchAction.open();

    verify( searchAction ).execute( ui );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetMessageFailsWithNull() {
    searchAction.getAdapter( SearchActionRendererHolder.class ).setSearchActionRenderer( searchActionRenderer );

    searchAction.setMessage( null );
  }

  @Test
  public void testSetMessage() {
    searchAction.getAdapter( SearchActionRendererHolder.class ).setSearchActionRenderer( searchActionRenderer );

    searchAction.setMessage( "foo" );

    verify( searchActionRenderer ).setMessage( "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetQueryFailsWithNull() {
    searchAction.getAdapter( SearchActionRendererHolder.class ).setSearchActionRenderer( searchActionRenderer );

    searchAction.setQuery( null );
  }

  @Test
  public void testSetQuery() {
    searchAction.getAdapter( SearchActionRendererHolder.class ).setSearchActionRenderer( searchActionRenderer );

    searchAction.setQuery( "foo" );

    verify( searchActionRenderer ).setQuery( "foo" );
  }

  @Test
  public void testHasRendererHolder() {
    SearchActionRendererHolder rendererHolder = searchAction.getAdapter( SearchActionRendererHolder.class );

    assertNotNull( rendererHolder );
  }

  @Test
  public void testHasOneRemoteObjectHolder() {
    SearchActionRendererHolder remoteObjectHolder = searchAction.getAdapter( SearchActionRendererHolder.class );
    SearchActionRendererHolder remoteObjectHolder2 = searchAction.getAdapter( SearchActionRendererHolder.class );

    assertSame( remoteObjectHolder, remoteObjectHolder2 );
  }

}

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

import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_QUERY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.PropertyChangeHandler;
import com.eclipsesource.tabris.internal.ui.PropertyChangeNotifier;
import com.eclipsesource.tabris.internal.ui.TestSearchAction;

public class SearchActionTest {

  private TestSearchAction searchAction;
  private PropertyChangeHandler handler;

  @Before
  public void setUp() {
    searchAction = new TestSearchAction();
    handler = mock( PropertyChangeHandler.class );
    searchAction.getAdapter( PropertyChangeNotifier.class ).setPropertyChangeHandler( handler );
  }

  @Test
  public void testCallsOpenWithoutQuery() {
    searchAction.open();

    verify( handler ).propertyChanged( METHOD_OPEN, null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetMessageFailsWithNull() {
    searchAction.setMessage( null );
  }

  @Test
  public void testSetMessage() {
    searchAction.setMessage( "foo" );

    verify( handler ).propertyChanged( PROPERTY_MESSAGE, "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetQueryFailsWithNull() {
    searchAction.setQuery( null );
  }

  @Test
  public void testSetQuery() {
    searchAction.setQuery( "foo" );

    verify( handler ).propertyChanged( PROPERTY_QUERY, "foo" );
  }

}

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
package com.eclipsesource.tabris.internal.ui;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;


public class PropertyChangeNotifierTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullHandler() {
    PropertyChangeNotifier notifier = new PropertyChangeNotifier();

    notifier.setPropertyChangeHandler( null );
  }

  @Test
  public void testSetsChangeHandler() {
    PropertyChangeNotifier notifier = new PropertyChangeNotifier();
    PropertyChangeHandler handler = mock( PropertyChangeHandler.class );

    notifier.setPropertyChangeHandler( handler );

    PropertyChangeHandler actualHandler = notifier.getPropertyChangeHandler();
    assertSame( handler, actualHandler );
  }

  @Test
  public void testNotifiesHandler() {
    PropertyChangeNotifier notifier = new PropertyChangeNotifier();
    PropertyChangeHandler handler = mock( PropertyChangeHandler.class );
    notifier.setPropertyChangeHandler( handler );

    notifier.firePropertyChange( "foo", "bar" );

    verify( handler ).propertyChanged( "foo", "bar" );
  }

  @Test( expected = IllegalStateException.class )
  public void testFireFailsWithoutHandler() {
    PropertyChangeNotifier notifier = new PropertyChangeNotifier();

    notifier.firePropertyChange( "foo", "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFireFailsWithNullKey() {
    PropertyChangeNotifier notifier = new PropertyChangeNotifier();
    PropertyChangeHandler handler = mock( PropertyChangeHandler.class );
    notifier.setPropertyChangeHandler( handler );

    notifier.firePropertyChange( null, "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFireFailsWithEmptyKey() {
    PropertyChangeNotifier notifier = new PropertyChangeNotifier();
    PropertyChangeHandler handler = mock( PropertyChangeHandler.class );
    notifier.setPropertyChangeHandler( handler );

    notifier.firePropertyChange( "", "bar" );
  }
}

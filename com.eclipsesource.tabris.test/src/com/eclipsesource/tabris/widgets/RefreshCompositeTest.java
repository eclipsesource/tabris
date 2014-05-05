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
package com.eclipsesource.tabris.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.eclipse.rap.rwt.internal.lifecycle.WidgetLifeCycleAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.RefreshCompositeLCA;
import com.eclipsesource.tabris.internal.RefreshCompositeLCA.RefreshAdapter;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


@SuppressWarnings("restriction")
public class RefreshCompositeTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Shell shell;

  @Before
  public void setUp() {
    shell = new Shell( new Display() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParent() {
    new RefreshComposite( null, SWT.NONE );
  }

  @Test
  public void testHasRefreshCompositeLCA() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    WidgetLifeCycleAdapter adapter = composite.getAdapter( WidgetLifeCycleAdapter.class );

    assertTrue( adapter instanceof RefreshCompositeLCA );
  }

  @Test
  public void testHasRefreshAdapter() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    RefreshAdapter adapter = composite.getAdapter( RefreshAdapter.class );

    assertNotNull( adapter );
  }

  @Test
  public void testHasOneRefreshAdapter() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    RefreshAdapter adapter = composite.getAdapter( RefreshAdapter.class );
    RefreshAdapter adapter2 = composite.getAdapter( RefreshAdapter.class );

    assertSame( adapter, adapter2 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullMessage() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    composite.setMessage( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddNullListener() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    composite.addRefreshListener( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToRemoveNullListener() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    composite.removeRefreshListener( null );
  }

  @Test
  public void testMessageIsNullByDefault() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    String message = composite.getMessage();

    assertNull( message );
  }

  @Test
  public void testListenersAreEmptyByDefault() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    List<RefreshListener> listeners = composite.getRefreshListeners();

    assertTrue( listeners.isEmpty() );
  }

  @Test
  public void testCanAddListeners() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );
    RefreshListener listener = mock( RefreshListener.class );

    composite.addRefreshListener( listener );

    List<RefreshListener> listeners = composite.getRefreshListeners();
    assertTrue( listeners.contains( listener ) );
  }

  @Test
  public void testCanRemoveListeners() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );
    RefreshListener listener = mock( RefreshListener.class );

    composite.addRefreshListener( listener );
    composite.removeRefreshListener( listener );

    List<RefreshListener> listeners = composite.getRefreshListeners();
    assertTrue( listeners.isEmpty() );
  }

  @Test
  public void testListenersAreSafeCopy() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    List<RefreshListener> listeners = composite.getRefreshListeners();
    List<RefreshListener> listeners2 = composite.getRefreshListeners();

    assertNotSame( listeners, listeners2 );
  }

  @Test
  public void testCanSetMessage() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    composite.setMessage( "foo" );

    String message = composite.getMessage();
    assertEquals( "foo", message );
  }

  @Test
    public void testDoneChangesResetAdapter() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );

    composite.done();

    RefreshAdapter adapter = composite.getAdapter( RefreshAdapter.class );
    assertTrue( adapter.isDone() );
  }

}

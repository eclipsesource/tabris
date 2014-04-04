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
package com.eclipsesource.tabris.internal;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.eclipsesource.tabris.widgets.RefreshComposite;
import com.eclipsesource.tabris.widgets.RefreshListener;


@SuppressWarnings("restriction")
public class RefreshCompositeOperationHandlerTest {

  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    shell = new Shell( new Display() );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testNotifiesNoRefreshListenerForOtherEvent() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );
    RefreshListener listener = mock( RefreshListener.class );
    composite.addRefreshListener( listener );
    RefreshCompositeOperationHandler handler = new RefreshCompositeOperationHandler( composite );

    handler.handleNotify( "FocusIn", null );

    verify( listener, never() ).refreshed();
  }

  @Test
  public void testNotifiesRefreshListener() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );
    RefreshListener listener = mock( RefreshListener.class );
    composite.addRefreshListener( listener );
    RefreshCompositeOperationHandler handler = new RefreshCompositeOperationHandler( composite );

    handler.handleNotify( "Refresh", null );

    verify( listener ).refreshed();
  }

  @Test
  public void testNotifiesRefreshListeners() {
    RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );
    RefreshListener listener = mock( RefreshListener.class );
    RefreshListener listener2 = mock( RefreshListener.class );
    composite.addRefreshListener( listener );
    composite.addRefreshListener( listener2 );
    RefreshCompositeOperationHandler handler = new RefreshCompositeOperationHandler( composite );

    handler.handleNotify( "Refresh", null );

    InOrder order = inOrder( listener, listener2 );
    order.verify( listener ).refreshed();
    order.verify( listener2 ).refreshed();
  }
}

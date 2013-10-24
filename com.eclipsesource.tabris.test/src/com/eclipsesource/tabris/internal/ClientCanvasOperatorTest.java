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
package com.eclipsesource.tabris.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.widgets.ClientCanvas;


public class ClientCanvasOperatorTest {

  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    shell = new Shell( display );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCanvas() {
    new ClientCanvasOperator( null );
  }

  @Test
  public void testRedrawsOnDrawingEvent() {
    ClientCanvas clientCanvas = new ClientCanvas( shell, SWT.NONE );
    PaintListener listener = mock( PaintListener.class );
    clientCanvas.addPaintListener( listener );
    ClientCanvasOperator operator = new ClientCanvasOperator( clientCanvas );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );

    operator.handleNotify( clientCanvas, ClientCanvasOperator.DRAWING_EVENT, null );

    verify( listener ).paintControl( any( PaintEvent.class ) );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testDispatchesUnknownEventsToControlOperator() {
    ClientCanvas clientCanvas = new ClientCanvas( shell, SWT.NONE );
    ClientCanvasOperator operator = new ClientCanvasOperator( clientCanvas );

    operator.handleNotify( clientCanvas, "foobar", null );
  }

}

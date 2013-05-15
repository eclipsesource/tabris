/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.CLIENT_CANVAS;
import static org.eclipse.rap.rwt.lifecycle.WidgetUtil.getId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TypedListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.internal.ClientCanvasTestUtil;
import com.eclipsesource.tabris.internal.DrawingsCache;


public class ClientCanvasTest {

  private ClientCanvas clientCanvas;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    Shell shell = new Shell( display );
    clientCanvas = new ClientCanvas( shell, SWT.NONE );
    Fixture.fakeNewRequest();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( ClientCanvas.class ) );
  }

  @Test
  public void testClientDrawListenerIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( ClientDrawListener.class ) );
  }

  @Test
  public void testDrawingCacheIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( DrawingsCache.class ) );
  }

  @Test
  public void testHasCustomVariant() {
    Object data = clientCanvas.getData( CLIENT_CANVAS.getKey() );

    assertEquals( Boolean.TRUE, data );
  }

  private class CheckPaintListener implements PaintListener {

    private boolean wasCalled;

    public synchronized boolean wasCalled() {
      return wasCalled;
    }

    @Override
    public synchronized void paintControl( PaintEvent event ) {
      wasCalled = true;
    }
  }

  @Test
  public void testRedraws() {
    CheckPaintListener listener = new CheckPaintListener();
    clientCanvas.addPaintListener( listener );

    fakeDrawEvent();

    assertTrue( listener.wasCalled() );
  }

  @Test
  public void testAddDispatchListener() {
    Listener[] listeners = clientCanvas.getListeners( SWT.Paint );

    assertEquals( 1, listeners.length );
  }

  @Test
  public void testAddsDispatchListenerLast() {
    PaintListener listener = mock( PaintListener.class );

    clientCanvas.addPaintListener( listener );

    Listener[] listeners = clientCanvas.getListeners( SWT.Paint );

    assertEquals( listener, ( ( TypedListener )listeners[ 0 ] ).getEventListener() );
    assertEquals( 2, listeners.length );
  }

  @Test
  public void testCachesDrawings() {
    PaintListener paintListener = mock( PaintListener.class );
    clientCanvas.addPaintListener( paintListener );

    fakeDrawEvent();
    JsonObject parameters = new JsonObject();
    parameters.add( ClientCanvas.DRAWINGS_PROPERTY, ClientCanvasTestUtil.createDrawingsWithoutLineWidth() );
    Fixture.fakeNewRequest();
    Fixture.fakeNotifyOperation( getId( clientCanvas ), ClientCanvas.DRAWING_EVENT, parameters );
    Fixture.executeLifeCycleFromServerThread();

    ArgumentCaptor<PaintEvent> captor = ArgumentCaptor.forClass( PaintEvent.class );
    verify( paintListener, times( 2 ) ).paintControl( captor.capture() );
  }

  @Test
  public void testClearTriggersRedraw() {
    CheckPaintListener listener = new CheckPaintListener();
    clientCanvas.addPaintListener( listener );

    clientCanvas.clear();
    fakeDrawEvent();

    assertTrue( listener.wasCalled() );
  }

  @Test
  public void testHasUndo() {
    fakeDrawEvent();

    assertTrue( clientCanvas.hasUndo() );
    assertFalse( clientCanvas.hasRedo() );
  }

  @Test
  public void testUndo() {
    fakeDrawEvent();

    clientCanvas.undo();

    assertFalse( clientCanvas.hasUndo() );
    assertTrue( clientCanvas.hasRedo() );
  }

  @Test
  public void testUndoRedraws() {
    CheckPaintListener listener = new CheckPaintListener();
    clientCanvas.addPaintListener( listener );
    fakeDrawEvent();

    clientCanvas.undo();

    assertTrue( listener.wasCalled() );
  }

  @Test
  public void testHasRedo() {
    fakeDrawEvent();

    clientCanvas.undo();

    assertTrue( clientCanvas.hasRedo() );
  }

  @Test
  public void testRedo() {
    fakeDrawEvent();
    clientCanvas.undo();

    clientCanvas.redo();

    assertTrue( clientCanvas.hasUndo() );
    assertFalse( clientCanvas.hasRedo() );
  }

  @Test
  public void testRedoRedraws() {
    CheckPaintListener listener = new CheckPaintListener();
    clientCanvas.addPaintListener( listener );
    fakeDrawEvent();
    clientCanvas.undo();

    clientCanvas.redo();

    assertTrue( listener.wasCalled() );
  }

  @Test
  public void testDrawDeletesRedoStack() {
    clientCanvas.undo();
    fakeDrawEvent();

    assertFalse( clientCanvas.hasRedo() );
  }

  @Test
  public void testFiresDrawingReceived() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );

    fakeDrawEvent();

    verify( listener ).receivedDrawing();
  }

  @Test
  public void testFiresDrawingReceivedOnUndo() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );

    fakeDrawEvent();
    clientCanvas.undo();

    verify( listener, times( 2 ) ).receivedDrawing();
  }

  @Test
  public void testFiresDrawingReceivedOnRedo() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );

    fakeDrawEvent();
    clientCanvas.undo();
    clientCanvas.redo();

    verify( listener, times( 3 ) ).receivedDrawing();
  }

  @Test
  public void testFiresDrawingReceivedOnClear() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );

    fakeDrawEvent();
    clientCanvas.clear();

    verify( listener, times( 2 ) ).receivedDrawing();
  }

  @Test
  public void testClearDeletsRedo() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );

    fakeDrawEvent();
    clientCanvas.undo();
    assertTrue( clientCanvas.hasRedo() );
    clientCanvas.clear();
    assertFalse( clientCanvas.hasRedo() );
  }

  @Test
  public void testFiresDrawingReceivedOnRemoved() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );
    clientCanvas.removeClientDrawListener( listener );

    fakeDrawEvent();

    verify( listener, never() ).receivedDrawing();
  }

  private void fakeDrawEvent() {
    JsonObject parameters = new JsonObject();
    parameters.add( ClientCanvas.DRAWINGS_PROPERTY, ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.fakeNewRequest();
    Fixture.fakeNotifyOperation( getId( clientCanvas ), ClientCanvas.DRAWING_EVENT, parameters );
    Fixture.executeLifeCycleFromServerThread();
  }
}

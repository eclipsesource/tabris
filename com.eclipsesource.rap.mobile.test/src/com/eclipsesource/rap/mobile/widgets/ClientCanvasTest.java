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
package com.eclipsesource.rap.mobile.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.EventListener;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rwt.internal.events.IEventAdapter;
import org.eclipse.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.rap.mobile.internal.ClientCanvasTestUtil;


@SuppressWarnings("restriction")
public class ClientCanvasTest {
  
  private ClientCanvas clientCanvas;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    Shell shell = new Shell( display );
    clientCanvas = new ClientCanvas( shell, SWT.NONE );
  }
  
  @After
  public void tearDown() {
    Fixture.tearDown();
  }
  
  @Test
  public void testHasCustomVariant() {
    Object data = clientCanvas.getData( WidgetUtil.CUSTOM_VARIANT );
    
    assertEquals( ClientCanvas.CLIENT_CANVAS, data );
  }
  
  private class CheckPaintListener implements PaintListener {
    
    private boolean wasCalled;

    public boolean wasCalled() {
      return wasCalled;
    }
    
    public void paintControl( PaintEvent event ) {
      wasCalled = true;
    }
  }
  
  @Test
  public void testRedraws() {
    CheckPaintListener listener = new CheckPaintListener();
    clientCanvas.addPaintListener( listener );
    
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 3 ) );
    Fixture.executeLifeCycleFromServerThread();
    
    assertTrue( listener.wasCalled() );
  }
  
  @Test
  public void testAddDispatchListener() {
    IEventAdapter adapter = clientCanvas.getAdapter( IEventAdapter.class );
    EventListener[] listeners = adapter.getListener( PaintListener.class );
    
    assertEquals( 1, listeners.length );
  }
  
  @Test
  public void testAddsDispatchListenerLast() {
    PaintListener listener = mock( PaintListener.class );
    
    clientCanvas.addPaintListener( listener );
    
    IEventAdapter adapter = clientCanvas.getAdapter( IEventAdapter.class );
    EventListener[] listeners = adapter.getListener( PaintListener.class );
    
    assertEquals( listener, listeners[ 0 ] );
    assertEquals( 2, listeners.length );
  }
  
  @Test
  public void testCachesDrawings() {
    PaintListener paintListener = mock( PaintListener.class );
    clientCanvas.addPaintListener( paintListener );
    
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawingsWithoutLineWidth() );
    Fixture.executeLifeCycleFromServerThread();
    
    ArgumentCaptor<PaintEvent> captor = ArgumentCaptor.forClass( PaintEvent.class );
    verify( paintListener, times( 2 ) ).paintControl( captor.capture() );
  }
  
  @Test
  public void testClear() {
    CheckPaintListener listener = new CheckPaintListener();
    clientCanvas.addPaintListener( listener );
    
    clientCanvas.clear();
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    
    assertTrue( listener.wasCalled() );
  }
  
  @Test
  public void testHasUndo() {
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    
    assertTrue( clientCanvas.hasUndo() );
    assertFalse( clientCanvas.hasRedo() );
  }
  
  @Test
  public void testUndo() {
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    
    clientCanvas.undo();
    
    assertFalse( clientCanvas.hasUndo() );
    assertTrue( clientCanvas.hasRedo() );
  }
  
  @Test
  public void testUndoRedraws() {
    CheckPaintListener listener = new CheckPaintListener();
    clientCanvas.addPaintListener( listener );
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    
    clientCanvas.undo();
    
    assertTrue( listener.wasCalled() );
  }
  
  @Test
  public void testHasRedo() {
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    
    clientCanvas.undo();

    assertTrue( clientCanvas.hasRedo() );
  }
  
  @Test
  public void testRedo() {
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    clientCanvas.undo();
    
    clientCanvas.redo();
    
    assertTrue( clientCanvas.hasUndo() );
    assertFalse( clientCanvas.hasRedo() );
  }
  
  @Test
  public void testRedoRedraws() {
    CheckPaintListener listener = new CheckPaintListener();
    clientCanvas.addPaintListener( listener );
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    clientCanvas.undo();
    
    clientCanvas.redo();
    
    assertTrue( listener.wasCalled() );
  }
  
  @Test
  public void testDrawDeletesRedoStack() {
    clientCanvas.undo();
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    
    assertFalse( clientCanvas.hasRedo() );
  }
  
  @Test
  public void testFiresDrawingReceived() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );
    
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    
    verify( listener ).receivedDrawing();
  }
  
  @Test
  public void testFiresDrawingReceivedOnUndo() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );
    
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    clientCanvas.undo();
    
    verify( listener, times( 2 ) ).receivedDrawing();
  }
  
  @Test
  public void testFiresDrawingReceivedOnRedo() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );
    
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    clientCanvas.undo();
    clientCanvas.redo();
    
    verify( listener, times( 3 ) ).receivedDrawing();
  }
  
  @Test
  public void testFiresDrawingReceivedOnRemoved() {
    ClientDrawListener listener = mock( ClientDrawListener.class );
    clientCanvas.addClientDrawListener( listener );
    clientCanvas.removeClientDrawListener( listener );
    
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings( 2 ) );
    Fixture.executeLifeCycleFromServerThread();
    
    verify( listener, never() ).receivedDrawing();
  }
}

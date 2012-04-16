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

import org.eclipse.rap.rwt.testfixture.Fixture;
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
  
  
  @Test
  public void testRedraws() {
    clientCanvas.addPaintListener( new PaintListener() {
      
      public void paintControl( PaintEvent event ) {
        // means that the gc.setLineWidth method was called
        assertEquals( ClientCanvasTestUtil.LINE_WITH, event.gc.getLineWidth() );
      }
    } );
    
    IClientObjectAdapter adapter = clientCanvas.getAdapter( IClientObjectAdapter.class );
    Fixture.fakeRequestParam( adapter.getId() + ".drawings", ClientCanvasTestUtil.createDrawings() );
    Fixture.executeLifeCycleFromServerThread();
  }
}

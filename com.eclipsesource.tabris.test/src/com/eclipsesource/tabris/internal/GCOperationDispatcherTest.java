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
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.io.Serializable;

import org.eclipse.rap.json.ParseException;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;


public class GCOperationDispatcherTest {

  private GC gc;
  private GCOperationDispatcher dispatcher;

  @Before
  public void setUp() {
    Fixture.setUp();
    new Display();
    gc = mock( GC.class );
    dispatcher = new GCOperationDispatcher( gc, ClientCanvasTestUtil.createDrawings( 3 ) );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( GCOperationDispatcher.class ) );
  }

  @Test
  public void testDispatches() {
    dispatcher.dispatch();

    InOrder order = inOrder( gc );
    order.verify( gc ).drawPolyline( aryEq( new int[] { 0, 1, 5, 5} ) );
    order.verify( gc ).setLineWidth( 3 );
    ArgumentCaptor<Color> captor = ArgumentCaptor.forClass( Color.class );
    order.verify( gc ).setForeground( captor.capture() );
    order.verify( gc ).setAlpha( 10 );
    Color color = captor.getValue();
    assertEquals( 50, color.getRed() );
    assertEquals( 100, color.getGreen() );
    assertEquals( 200, color.getBlue() );
    order.verify( gc ).drawPolyline( aryEq( new int[] { 0, 1, 5, 5} ) );
  }

  @Test( expected = ParseException.class )
  public void testInvalidJson() {
    new GCOperationDispatcher( gc, "[" );
  }

  @Test( expected = ParseException.class )
  public void testInvalidJsonContent() {
    GCOperationDispatcher dispatcher2 = new GCOperationDispatcher( gc, "[{ 'test' : 'test' }]" );

    dispatcher2.dispatch();
  }

  @Test
  public void testRestoresState() {
    dispatcher.dispatch();

    assertEquals( 0, gc.getAlpha() );
    assertEquals( 0, gc.getLineWidth() );
    assertNull( gc.getForeground() );
  }

}

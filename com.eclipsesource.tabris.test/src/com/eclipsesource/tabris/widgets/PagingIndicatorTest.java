/*******************************************************************************
 * Copyright (c) 2014, 2016 EclipseSource and others.
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
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.internal.graphics.GCAdapter;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;


@SuppressWarnings("restriction")
public class PagingIndicatorTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Shell parent;
  private PagingIndicator indicator;


  @Before
  public void setUp() {
    parent = new Shell( new Display() );
    indicator = new PagingIndicator( parent );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParent() {
    new PagingIndicator( null );
  }

  @Test
  public void testHasDefaultForCount() {
    assertEquals( 1, indicator.getCount() );
  }

  @Test
  public void testSavesSettedCount() {
    indicator.setCount( 10 );

    assertEquals( 10, indicator.getCount() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeCount() {
    indicator.setCount( -1 );
  }

  @Test
  public void testHasDefaultForActive() {
    assertEquals( 0, indicator.getActive() );
  }

  @Test
  public void testSavesSettedActive() {
    indicator.setCount( 2 );

    indicator.setActive( 1 );

    assertEquals( 1, indicator.getActive() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetActiveWithNegativeIndex() {
    indicator.setActive( -1 );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToSetActiveWithNonExistingIndex() {
    indicator.setActive( 2 );
  }

  @Test
  public void testHasDefaultForSpacing() {
    assertEquals( 9, indicator.getSpacing() );
  }

  @Test
  public void testSavesSettedSpacing() {
    indicator.setSpacing( 5 );

    assertEquals( 5, indicator.getSpacing() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeSpacing() {
    indicator.setSpacing( -1 );
  }

  @Test
  public void testHasDefaultForDiameter() {
    assertEquals( 7, indicator.getDiameter() );
  }

  @Test
  public void testSavesSettedDiameter() {
    indicator.setDiameter( 5 );

    assertEquals( 5, indicator.getDiameter() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeDiameter() {
    indicator.setDiameter( -1 );
  }

  @Test
  public void testHasDefaultForActiveColor() {
    assertEquals( indicator.getActiveColor().getRGB(), new RGB( 0, 122, 255 ) );
  }

  @Test
  public void testSavesSettedActiveColor() {
    indicator.setActiveColor( new Color( parent.getDisplay(), 0, 0, 0 ) );

    assertEquals( indicator.getActiveColor().getRGB(), new RGB( 0, 0, 0 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullActiveColor() {
    indicator.setActiveColor( null );
  }

  @Test
  public void testHasDefaultForInactiveColor() {
    assertEquals( indicator.getInactiveColor().getRGB(), new RGB( 192, 192, 192 ) );
  }

  @Test
  public void testSavesSettedInactiveColor() {
    indicator.setInactiveColor( new Color( parent.getDisplay(), 255, 0, 0 ) );

    assertEquals( indicator.getInactiveColor().getRGB(), new RGB( 255, 0, 0 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullInactiveColor() {
    indicator.setInactiveColor( null );
  }

  @Test
  public void testSetActiveDrawsIndicator() {
    indicator.setCount( 2 );

    indicator.setActive( 1 );

    verifyDraw( indicator );
  }

  @Test
  public void testSetCountDrawsIndicator() {
    indicator.setCount( 2 );

    verifyDraw( indicator );
  }

  @Test
  public void testSetSpacingDrawsIndicator() {
    indicator.setSpacing( 2 );

    verifyDraw( indicator );
  }

  @Test
  public void testSetDiameterDrawsIndicator() {
    indicator.setDiameter( 10 );

    verifyDraw( indicator );
  }

  @Test
  public void testSetActiveColorDrawsIndicator() {
    indicator.setActiveColor( new Color( parent.getDisplay(), 0, 0, 0 ) );

    verifyDraw( indicator );
  }

  @Test
  public void testSetInactiveColorDrawsIndicator() {
    indicator.setInactiveColor( new Color( parent.getDisplay(), 0, 0, 0 ) );

    verifyDraw( indicator );
  }

  @Test
  public void testUpdateDrawsIndicator() {
    indicator.update();

    verifyDraw( indicator );
  }

  @Test
  public void testAddsPaintListenerThatRedrawsIndicator() {
    Canvas canvas = indicator.getCanvas();
    Listener listener = canvas.getListeners( SWT.Paint )[ 0 ];
    Event event = new Event();
    event.type = SWT.Paint;
    event.widget = canvas;
    event.gc = new GC( canvas );

    listener.handleEvent( event );

    verifyDraw( indicator );
  }

  private void verifyDraw( PagingIndicator indicator ) {
    GCAdapter gcAdapter = indicator.getCanvas().getAdapter( GCAdapter.class );

    assertTrue( gcAdapter.getGCOperations().length > 0 );
  }

}

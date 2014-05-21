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

import static com.eclipsesource.tabris.test.util.MessageUtil.OperationType.CALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.internal.widgets.canvaskit.CanvasLCA;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.MessageUtil;
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
    renderInitialization( indicator );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParent() {
    new PagingIndicator( null );
  }

  @Test
  public void testHasDefaultForCount() {
    int count = indicator.getCount();

    assertEquals( 1, count );
  }

  @Test
  public void testSavesSettedCount() {
    indicator.setCount( 10 );

    int count = indicator.getCount();
    assertEquals( 10, count );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeCount() {
    indicator.setCount( -1 );
  }

  @Test
  public void testHasDefaultForActive() {
    int active = indicator.getActive();

    assertEquals( 0, active );
  }

  @Test
  public void testSavesSettedActive() {
    indicator.setCount( 2 );

    indicator.setActive( 1 );

    int active = indicator.getActive();
    assertEquals( 1, active );
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
    int spacing = indicator.getSpacing();

    assertEquals( 9, spacing );
  }

  @Test
  public void testSavesSettedSpacing() {
    indicator.setSpacing( 5 );

    int spacing = indicator.getSpacing();
    assertEquals( 5, spacing );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeSpacing() {
    indicator.setSpacing( -1 );
  }

  @Test
  public void testHasDefaultForDiameter() {
    int diameter = indicator.getDiameter();

    assertEquals( 7, diameter );
  }

  @Test
  public void testSavesSettedDiameter() {
    indicator.setDiameter( 5 );

    int diameter = indicator.getDiameter();
    assertEquals( 5, diameter );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeDiameter() {
    indicator.setDiameter( -1 );
  }

  @Test
  public void testHasDefaultForActiveColor() {
    Color activeColor = indicator.getActiveColor();

    assertEquals( activeColor.getRGB(), new RGB( 0, 122, 255 ) );
  }

  @Test
  public void testSavesSettedActiveColor() {
    indicator.setActiveColor( new Color( parent.getDisplay(), 0, 0, 0 ) );

    Color activeColor = indicator.getActiveColor();
    assertEquals( activeColor.getRGB(), new RGB( 0, 0, 0 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullActiveColor() {
    indicator.setActiveColor( null );
  }

  @Test
  public void testHasDefaultForInactiveColor() {
    Color inactiveColor = indicator.getInactiveColor();

    assertEquals( inactiveColor.getRGB(), new RGB( 192, 192, 192 ) );
  }

  @Test
  public void testSavesSettedInactiveColor() {
    indicator.setInactiveColor( new Color( parent.getDisplay(), 255, 0, 0 ) );

    Color inactiveColor = indicator.getInactiveColor();
    assertEquals( inactiveColor.getRGB(), new RGB( 255, 0, 0 ) );
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

  private void verifyDraw( PagingIndicator indicator ) {
    renderChanges( indicator );
    String gcId = WidgetUtil.getId( indicator.getCanvas() ) + ".gc";
    JsonObject properties = MessageUtil.getOperationProperties( gcId, CALL, "init" );
    assertNotNull( properties );
  }

  @SuppressWarnings("deprecation")
  private void renderChanges( PagingIndicator indicator ) {
    CanvasLCA adapter = ( CanvasLCA )indicator.getCanvas().getAdapter( org.eclipse.rap.rwt.lifecycle.WidgetLifeCycleAdapter.class );
    try {
      adapter.renderChanges( indicator.getCanvas() );
    } catch( IOException shouldNotHappen ) {
      fail( shouldNotHappen.getMessage() );
    }
  }

  @SuppressWarnings("deprecation")
  private void renderInitialization( PagingIndicator indicator ) {
    CanvasLCA adapter = ( CanvasLCA )indicator.getCanvas().getAdapter( org.eclipse.rap.rwt.lifecycle.WidgetLifeCycleAdapter.class );
    try {
      adapter.renderInitialization( indicator.getCanvas() );
    } catch( IOException shouldNotHappen ) {
      fail( shouldNotHappen.getMessage() );
    }
  }
}

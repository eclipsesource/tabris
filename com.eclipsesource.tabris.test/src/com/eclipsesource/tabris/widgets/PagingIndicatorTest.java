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
import static org.junit.Assert.assertTrue;

import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CallOperation;
import org.eclipse.rap.rwt.testfixture.Message.Operation;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PagingIndicatorTest {

  private Shell parent;

  @Before
  public void setUp() {
    Fixture.setUp();
    parent = new Shell( new Display() );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParent() {
    new PagingIndicator( null );
  }

  @Test
  public void testHasDefaultForCount() {
    PagingIndicator indicator = new PagingIndicator( parent );

    int count = indicator.getCount();

    assertEquals( 1, count );
  }

  @Test
  public void testSavesSettedCount() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setCount( 10 );

    int count = indicator.getCount();
    assertEquals( 10, count );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeCount() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setCount( -1 );
  }

  @Test
  public void testHasDefaultForActive() {
    PagingIndicator indicator = new PagingIndicator( parent );

    int active = indicator.getActive();

    assertEquals( 0, active );
  }

  @Test
  public void testSavesSettedActive() {
    PagingIndicator indicator = new PagingIndicator( parent );
    indicator.setCount( 2 );

    indicator.setActive( 1 );

    int active = indicator.getActive();
    assertEquals( 1, active );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetActiveWithNegativeIndex() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setActive( -1 );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToSetActiveWithNonExistingIndex() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setActive( 2 );
  }

  @Test
  public void testHasDefaultForSpacing() {
    PagingIndicator indicator = new PagingIndicator( parent );

    int spacing = indicator.getSpacing();

    assertEquals( 9, spacing );
  }

  @Test
  public void testSavesSettedSpacing() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setSpacing( 5 );

    int spacing = indicator.getSpacing();
    assertEquals( 5, spacing );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeSpacing() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setSpacing( -1 );
  }

  @Test
  public void testHasDefaultForDiameter() {
    PagingIndicator indicator = new PagingIndicator( parent );

    int diameter = indicator.getDiameter();

    assertEquals( 7, diameter );
  }

  @Test
  public void testSavesSettedDiameter() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setDiameter( 5 );

    int diameter = indicator.getDiameter();
    assertEquals( 5, diameter );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeDiameter() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setDiameter( -1 );
  }

  @Test
  public void testHasDefaultForActiveColor() {
    PagingIndicator indicator = new PagingIndicator( parent );

    Color activeColor = indicator.getActiveColor();

    assertEquals( activeColor.getRGB(), new RGB( 0, 122, 255 ) );
  }

  @Test
  public void testSavesSettedActiveColor() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setActiveColor( new Color( parent.getDisplay(), 0, 0, 0 ) );

    Color activeColor = indicator.getActiveColor();
    assertEquals( activeColor.getRGB(), new RGB( 0, 0, 0 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullActiveColor() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setActiveColor( null );
  }

  @Test
  public void testHasDefaultForInactiveColor() {
    PagingIndicator indicator = new PagingIndicator( parent );

    Color inactiveColor = indicator.getInactiveColor();

    assertEquals( inactiveColor.getRGB(), new RGB( 192, 192, 192 ) );
  }

  @Test
  public void testSavesSettedInactiveColor() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setInactiveColor( new Color( parent.getDisplay(), 255, 0, 0 ) );

    Color inactiveColor = indicator.getInactiveColor();
    assertEquals( inactiveColor.getRGB(), new RGB( 255, 0, 0 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullInactiveColor() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setInactiveColor( null );
  }

  @Test
  public void testSetActiveDrawsIndicator() {
    PagingIndicator indicator = new PagingIndicator( parent );
    indicator.setCount( 2 );

    indicator.setActive( 1 );

    verifyDraw( indicator );
  }

  @Test
  public void testSetCountDrawsIndicator() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setCount( 2 );

    verifyDraw( indicator );
  }

  @Test
  public void testSetSpacingDrawsIndicator() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setSpacing( 2 );

    verifyDraw( indicator );
  }

  @Test
  public void testSetDiameterDrawsIndicator() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setDiameter( 10 );

    verifyDraw( indicator );
  }

  @Test
  public void testSetActiveColorDrawsIndicator() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setActiveColor( new Color( parent.getDisplay(), 0, 0, 0 ) );

    verifyDraw( indicator );
  }

  @Test
  public void testSetInactiveColorDrawsIndicator() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.setInactiveColor( new Color( parent.getDisplay(), 0, 0, 0 ) );

    verifyDraw( indicator );
  }

  @Test
  public void testUpdateDrawsIndicator() {
    PagingIndicator indicator = new PagingIndicator( parent );

    indicator.update();

    verifyDraw( indicator );
  }

  private void verifyDraw( PagingIndicator indicator ) {
    boolean found = false;
    String gcId = WidgetUtil.getId( indicator.getCanvas() ) + ".gc";
    Fixture.executeLifeCycleFromServerThread();
    Message message = Fixture.getProtocolMessage();
    for( int i = 0; i < message.getOperationCount(); i++ ) {
      Operation operation = message.getOperation( i );
      if( operation instanceof CallOperation && operation.getTarget().equals( gcId ) ) {
        CallOperation call = ( CallOperation )operation;
        assertEquals( "init", call.getMethodName() );
        found = true;
        break;
      }
    }
    assertTrue( found );
  }
}

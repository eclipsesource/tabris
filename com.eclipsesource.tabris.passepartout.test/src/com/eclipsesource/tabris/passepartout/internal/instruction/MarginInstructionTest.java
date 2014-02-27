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
package com.eclipsesource.tabris.passepartout.internal.instruction;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.Unit;
import com.eclipsesource.tabris.passepartout.internal.instruction.MarginInstruction;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;
import com.eclipsesource.tabris.passepartout.internal.unit.Pixel;


public class MarginInstructionTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTop() {
    new MarginInstruction( null, mock( Unit.class ), mock( Unit.class ), mock( Unit.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullRight() {
    new MarginInstruction( mock( Unit.class ), null, mock( Unit.class ), mock( Unit.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBottom() {
    new MarginInstruction( mock( Unit.class ), mock( Unit.class ), null, mock( Unit.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullLeft() {
    new MarginInstruction( mock( Unit.class ), mock( Unit.class ), mock( Unit.class ), null );
  }

  @Test
  public void testUsesOriginalBoundsWithUnsupportedUnit() {
    MarginInstruction instruction = new MarginInstruction( mock( Unit.class ),
                                                                     mock( Unit.class ),
                                                                     mock( Unit.class ),
                                                                     mock( Unit.class ) );

    Bounds bounds = instruction.computeBounds( new Bounds( 0, 0, 100, 100 ), 16 );

    assertEquals( 0, bounds.getX() );
    assertEquals( 0, bounds.getY() );
    assertEquals( 100, bounds.getWidth() );
    assertEquals( 100, bounds.getHeight() );
  }

  @Test
  public void testUsesPixelToComputeMargins() {
    MarginInstruction instruction = new MarginInstruction( new Pixel( 5 ),
                                                                     new Pixel( 5 ),
                                                                     new Pixel( 5 ),
                                                                     new Pixel( 5 ) );

    Bounds bounds = instruction.computeBounds( new Bounds( 0, 0, 100, 100 ), 16 );

    assertEquals( 5, bounds.getX() );
    assertEquals( 5, bounds.getY() );
    assertEquals( 90, bounds.getWidth() );
    assertEquals( 90, bounds.getHeight() );
  }

  @Test
  public void testUsesEmToComputeMargins() {
    MarginInstruction instruction = new MarginInstruction( new Em( BigDecimal.ONE ),
                                                                     new Em( BigDecimal.ONE ),
                                                                     new Em( BigDecimal.ONE ),
                                                                     new Em( BigDecimal.ONE ) );

    Bounds bounds = instruction.computeBounds( new Bounds( 0, 0, 100, 100 ), 10 );

    assertEquals( 10, bounds.getX() );
    assertEquals( 10, bounds.getY() );
    assertEquals( 80, bounds.getWidth() );
    assertEquals( 80, bounds.getHeight() );
  }

  @Test
  public void testUsesPercentageToComputeMargins() {
    MarginInstruction instruction = new MarginInstruction( new Percentage( BigDecimal.valueOf( 10 ) ),
                                                                     new Percentage( BigDecimal.valueOf( 10 ) ),
                                                                     new Percentage( BigDecimal.valueOf( 10 ) ),
                                                                     new Percentage( BigDecimal.valueOf( 10 ) ) );

    Bounds bounds = instruction.computeBounds( new Bounds( 0, 0, 100, 100 ), 16 );

    assertEquals( 10, bounds.getX() );
    assertEquals( 10, bounds.getY() );
    assertEquals( 80, bounds.getWidth() );
    assertEquals( 80, bounds.getHeight() );
  }

  @Test
  public void testUsesMixedUnitsToComputeMargins() {
    MarginInstruction instruction = new MarginInstruction( new Percentage( BigDecimal.valueOf( 10 ) ),
                                                                     new Em( BigDecimal.ONE ),
                                                                     new Percentage( BigDecimal.valueOf( 10 ) ),
                                                                     new Pixel( 10 ) );

    Bounds bounds = instruction.computeBounds( new Bounds( 0, 0, 100, 100 ), 10 );

    assertEquals( 10, bounds.getX() );
    assertEquals( 10, bounds.getY() );
    assertEquals( 80, bounds.getWidth() );
    assertEquals( 80, bounds.getHeight() );
  }
}

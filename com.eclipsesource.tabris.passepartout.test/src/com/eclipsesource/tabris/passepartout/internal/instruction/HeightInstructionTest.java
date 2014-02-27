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
import com.eclipsesource.tabris.passepartout.PassePartout;
import com.eclipsesource.tabris.passepartout.Unit;
import com.eclipsesource.tabris.passepartout.internal.instruction.HeightInstruction;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;


public class HeightInstructionTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUnit() {
    new HeightInstruction( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParentBounds() {
    HeightInstruction instruction = new HeightInstruction( mock( Unit.class ) );

    instruction.computeHeight( null, 16 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeFontSize() {
    HeightInstruction instruction = new HeightInstruction( mock( Unit.class ) );

    instruction.computeHeight( mock( Bounds.class ), -1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithZeroFontSize() {
    HeightInstruction instruction = new HeightInstruction( mock( Unit.class ) );

    instruction.computeHeight( mock( Bounds.class ), 0 );
  }

  @Test
  public void testCalculatesHeightWithPixels() {
    HeightInstruction instruction = new HeightInstruction( PassePartout.px( 23 ) );

    int height = instruction.computeHeight( mock( Bounds.class ), 16 );

    assertEquals( 23, height );
  }

  @Test
  public void testCalculatesHeightWithEm() {
    Em height = new Em( BigDecimal.valueOf( 1.5 ) );
    HeightInstruction instruction = new HeightInstruction( height );

    int actualHeight = instruction.computeHeight( mock( Bounds.class ), 100 );

    assertEquals( 150, actualHeight );
  }

  @Test
  public void testCalculatesHeightWithPercentage() {
    HeightInstruction instruction = new HeightInstruction( new Percentage( BigDecimal.valueOf( 50 ) ) );

    int height = instruction.computeHeight( new Bounds( 0, 0, 100, 100 ), 16 );

    assertEquals( 50, height );
  }

  @Test
  public void testUsesDefaultHeightForUnsuportedBounds() {
    HeightInstruction instruction = new HeightInstruction( mock( Unit.class ) );

    int height = instruction.computeHeight( new Bounds( 0, 0, 100, 100 ), 16 );

    assertEquals( 0, height );
  }
}

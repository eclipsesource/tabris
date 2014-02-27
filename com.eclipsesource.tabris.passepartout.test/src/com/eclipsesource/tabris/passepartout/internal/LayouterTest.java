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
package com.eclipsesource.tabris.passepartout.internal;

import static com.eclipsesource.tabris.passepartout.PassePartout.px;
import static com.eclipsesource.tabris.passepartout.test.PassPartoutTestUtil.createConfig;
import static com.eclipsesource.tabris.passepartout.test.PassPartoutTestUtil.createEnvironment;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.FluidGridConfiguration;
import com.eclipsesource.tabris.passepartout.Instruction;
import com.eclipsesource.tabris.passepartout.LayoutMode;
import com.eclipsesource.tabris.passepartout.PassePartout;
import com.eclipsesource.tabris.passepartout.internal.instruction.ColumnsInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ExcludeInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.HeightInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.MarginInstruction;
import com.eclipsesource.tabris.passepartout.test.PassPartoutTestUtil;


public class LayouterTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullEnvironment() {
    new Layouter( null, new FluidGridConfiguration( LayoutMode.NONE, 100, 200 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullConfiguration() {
    new Layouter( PassPartoutTestUtil.createEnvironment(), null );
  }

  @Test
  public void testHas16ColumnsForWidth_1872() {
    Layouter layouter = new Layouter( createEnvironment( new Bounds( 0, 0, 1872, 100 ) ), createConfig() );

    int columns = layouter.getColumns();

    assertEquals( 16, columns );
  }

  @Test
  public void testHas8ColumnsForWidth_720() {
    Layouter layouter = new Layouter( createEnvironment( new Bounds( 0, 0, 720, 100 ) ), createConfig() );

    int columns = layouter.getColumns();

    assertEquals( 8, columns );
  }

  @Test
  public void testHas4ColumnsForWidthSmaller_720() {
    Layouter layouter = new Layouter( createEnvironment( new Bounds( 0, 0, 719, 100 ) ), createConfig() );

    int columns = layouter.getColumns();

    assertEquals( 4, columns );
  }

  @Test
  public void testComputesBoundsWithoutInstrutions() {
    Layouter layouter = new Layouter( createEnvironment( new Bounds( 0, 0, 600, 200 ) ), createConfig() );

    Bounds bounds = layouter.computeBounds( new Bounds( 0, 0, 100, 100 ), new ArrayList<Instruction>() );

    assertEquals( new Bounds( 0, 0, 150, 100 ), bounds );
  }

  @Test
  public void testComputesBoundsWithoutColumnInstrutions() {
    Layouter layouter = new Layouter( createEnvironment( new Bounds( 0, 0, 600, 200 ) ), createConfig() );
    ArrayList<Instruction> instructions = new ArrayList<Instruction>();
    instructions.add( new ColumnsInstruction( 2 ) );

    Bounds bounds = layouter.computeBounds( new Bounds( 0, 0, 100, 100 ), instructions );

    assertEquals( new Bounds( 0, 0, 300, 100 ), bounds );
  }

  @Test
  public void testComputesBoundsWithoutHeightInstrutions() {
    Layouter layouter = new Layouter( createEnvironment( new Bounds( 0, 0, 600, 200 ) ), createConfig() );
    ArrayList<Instruction> instructions = new ArrayList<Instruction>();
    instructions.add( new HeightInstruction( PassePartout.px( 300 ) ) );

    Bounds bounds = layouter.computeBounds( new Bounds( 0, 0, 100, 100 ), instructions );

    assertEquals( new Bounds( 0, 0, 150, 300 ), bounds );
  }

  @Test
  public void testComputesBoundsWithMarginInstrutions() {
    Layouter layouter = new Layouter( createEnvironment( new Bounds( 0, 0, 600, 200 ) ), createConfig() );
    ArrayList<Instruction> instructions = new ArrayList<Instruction>();
    instructions.add( new MarginInstruction( px( 5 ), px( 10 ), px( 20 ), px( 30 ) ) );

    Bounds bounds = layouter.computeBounds( new Bounds( 0, 0, 100, 100 ), instructions );

    assertEquals( new Bounds( 30, 5, 110, 75 ), bounds );
  }

  @Test
  public void testComputesBoundsWithExcludeInstrutions() {
    Layouter layouter = new Layouter( createEnvironment( new Bounds( 0, 0, 600, 200 ) ), createConfig() );
    ArrayList<Instruction> instructions = new ArrayList<Instruction>();
    instructions.add( new ExcludeInstruction() );

    Bounds bounds = layouter.computeBounds( new Bounds( 0, 0, 100, 100 ), instructions );

    assertEquals( new Bounds( 0, 0, 0, 0 ), bounds );
  }
}

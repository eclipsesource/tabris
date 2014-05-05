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
package com.eclipsesource.tabris.passepartout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.passepartout.internal.UIEnvironmentImpl;
import com.eclipsesource.tabris.passepartout.internal.condition.MaxWidthCondition;
import com.eclipsesource.tabris.passepartout.internal.condition.MinWidthCondition;
import com.eclipsesource.tabris.passepartout.internal.instruction.BackgroundImageInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.BackgroundInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ColumnsInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ExcludeInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.FontInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ForegroundInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.HeightInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ImageInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.MarginInstruction;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;
import com.eclipsesource.tabris.passepartout.internal.unit.Pixel;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class PassePartoutTest {

  @org.junit.Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private Display display;

  @Before
  public void setUp() {
    display = new Display();
  }

  @Test
  public void testCreatesFluidGridWithoutMode() {
    FluidGridLayout grid = PassePartout.createFluidGrid();

    assertNotNull( grid );
  }

  @Test
  public void testCreatesFluidGridWithMode() {
    FluidGridLayout grid = PassePartout.createFluidGrid( new FluidGridConfiguration( LayoutMode.AUTO, 100, 200 ) );

    assertNotNull( grid );
  }

  @Test
  public void testCreateFluidGridData() {
    Rule rule1 = mock( Rule.class );
    Rule rule2 = mock( Rule.class );

    FluidGridData data = PassePartout.createFluidGridData( rule1, rule2 );

    assertNotNull( data );
    assertEquals( rule1, data.getRules().get( 1 ) );
    assertEquals( rule2, data.getRules().get( 2 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testCreateFluidGridDataFailsWithNullRule() {
    Rule rule1 = mock( Rule.class );

    PassePartout.createFluidGridData( rule1, null );
  }

  @Test
  public void testCreateResourceCreatesResource() {
    Resource resource = PassePartout.createResource( mock( Rule.class ) );

    assertNotNull( resource );
  }

  @Test
  public void testMinWidthReturnsMinWidthCondition() {
    Condition condition = PassePartout.minWidth( PassePartout.px( 100 ) );

    assertTrue( condition instanceof MinWidthCondition );
  }

  @Test
  public void testMinWidthPassesMinWidthToCondition() {
    Condition condition = PassePartout.minWidth( PassePartout.px( 100 ) );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( new Bounds( 10, 10, 110, 0 ),
                                                                      mock( Bounds.class ),
                                                                      16 ) );

    assertTrue( complies );
  }

  @Test
  public void testMinWidthCreatesNewCondition() {
    Condition condition = PassePartout.minWidth( PassePartout.px( 100 ) );
    Condition condition2 = PassePartout.minWidth( PassePartout.px( 100 ) );

    assertNotSame( condition, condition2 );
  }

  @Test
  public void testMaxWidthReturnsMaxWidthCondition() {
    Condition condition = PassePartout.maxWidth( PassePartout.px( 100 ) );

    assertTrue( condition instanceof MaxWidthCondition );
  }

  @Test
  public void testMaxWidthPassesMaxWidthToCondition() {
    Condition condition = PassePartout.maxWidth( PassePartout.px( 100 ) );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( new Bounds( 10, 10, 90, 0 ),
                                                                      mock( Bounds.class ),
                                                                      16 ) );

    assertTrue( complies );
  }

  @Test
  public void testMaxWidthCreatesNewCondition() {
    Condition condition = PassePartout.maxWidth( PassePartout.px( 100 ) );
    Condition condition2 = PassePartout.maxWidth( PassePartout.px( 100 ) );

    assertNotSame( condition, condition2 );
  }

  @Test
  public void testWhenCreatesQuery() {
    Query query = PassePartout.when( mock( Condition.class ) );

    assertNotNull( query );
  }

  @Test
  public void testWhenCreatesNewQuery() {
    Query query = PassePartout.when( mock( Condition.class ) );
    Query query2 = PassePartout.when( mock( Condition.class ) );

    assertNotSame( query, query2 );
  }

  @Test
  public void testWhenCreatesQueryAndPassesCondition() {
    Condition condition = mock( Condition.class );

    Query query = PassePartout.when( condition );

    Rule rule = query.then( mock( Instruction.class ) );
    assertEquals( condition, rule.getConditions().get( 0 ) );
  }

  @Test
  public void testColumnsReturnsColumnsInstruction() {
    Instruction instruction = PassePartout.columns( 5 );

    assertTrue( instruction instanceof ColumnsInstruction );
  }

  @Test
  public void testHeightReturnsHeightEmInstruction() {
    Instruction instruction = PassePartout.height( PassePartout.em( 1.5 ) );

    assertTrue( instruction instanceof HeightInstruction );
  }

  @Test
  public void testHeightReturnsHeightPercentageInstruction() {
    Instruction instruction = PassePartout.height( PassePartout.percent( 20 ) );

    assertTrue( instruction instanceof HeightInstruction );
  }

  @Test
  public void testAbsoluteHeightReturnsHeightPixelInstruction() {
    Instruction instruction = PassePartout.height( PassePartout.px( 23 ) );

    assertTrue( instruction instanceof HeightInstruction );
  }

  @Test
  public void testColumnsCreatesNewInstruction() {
    Instruction instruction = PassePartout.columns( 5 );
    Instruction instruction2 = PassePartout.columns( 5 );

    assertNotSame( instruction, instruction2 );
  }

  @Test
  public void testEmCreatesEm() {
    Unit em = PassePartout.em( 1.5 );

    BigDecimal value = em.getValue();

    assertEquals( BigDecimal.valueOf( 1.5 ), value );
    assertTrue( em instanceof Em );
  }

  @Test
  public void testPercentCreatesPercent() {
    Unit percent = PassePartout.percent( 24 );

    BigDecimal value = percent.getValue();

    assertEquals( BigDecimal.valueOf( 24.0 ), value );
    assertTrue( percent instanceof Percentage );
  }

  @Test
    public void testPxCreatesPixel() {
      Unit pixel = PassePartout.px( 23 );

      BigDecimal value = pixel.getValue();

      assertEquals( 23, value.intValue() );
      assertTrue( pixel instanceof Pixel );
    }

  @Test
  public void testMarginsCreatesMarginsInstruction() {
    Unit margin = PassePartout.px( 5 );

    Instruction instruction = PassePartout.margins( margin, margin, margin, margin );

    assertTrue( instruction instanceof MarginInstruction );
  }

  @Test
  public void testExcludeCreatesExcludeInstruction() {
    Instruction instruction = PassePartout.exclude();

    assertTrue( instruction instanceof ExcludeInstruction );
  }

  @Test
  public void testFontCreatesFontInstruction() {
    Instruction instruction = PassePartout.font( display.getSystemFont() );

    assertTrue( instruction instanceof FontInstruction );
  }

  @Test
  public void testForegroundCreatesForegroundInstruction() {
    Instruction instruction = PassePartout.foreground( display.getSystemColor( SWT.COLOR_BLACK ) );

    assertTrue( instruction instanceof ForegroundInstruction );
  }

  @Test
  public void testBackgroundCreatesBackgroundInstruction() {
    Instruction instruction = PassePartout.background( display.getSystemColor( SWT.COLOR_BLACK ) );

    assertTrue( instruction instanceof BackgroundInstruction );
  }

  @Test
  public void testImageCreatesImageInstruction() {
    Instruction instruction = PassePartout.image( environment.getTestImage() );

    assertTrue( instruction instanceof ImageInstruction );
  }

  @Test
  public void testBackgroundImageCreatesBackgroundImageInstruction() {
    Instruction instruction = PassePartout.backgroundImage( environment.getTestImage() );

    assertTrue( instruction instanceof BackgroundImageInstruction );
  }
}

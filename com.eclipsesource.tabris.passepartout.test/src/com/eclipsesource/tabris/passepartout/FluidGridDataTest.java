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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.internal.condition.AlwaysTrueContidtion;
import com.eclipsesource.tabris.passepartout.internal.instruction.ColumnsInstruction;


public class FluidGridDataTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullRule() {
    FluidGridData data = new FluidGridData();

    data.addRule( null );
  }

  @Test
  public void testHasAddedRule() {
    FluidGridData data = new FluidGridData();
    Rule rule = mock( Rule.class );

    data.addRule( rule );

    List<Rule> rules = data.getRules();
    assertEquals( rule, rules.get( 1 ) );
  }

  @Test
  public void testAddedRuleReturnsData() {
    FluidGridData data = new FluidGridData();
    Rule rule = mock( Rule.class );

    FluidGridData actualData = data.addRule( rule );

    assertSame( data, actualData );
  }

  @Test
  public void testGetRulesReturnSafeCopy() {
    FluidGridData data = new FluidGridData();

    List<Rule> rules = data.getRules();
    List<Rule> rules2 = data.getRules();

    assertNotSame( rules, rules2 );
  }

  @Test
  public void testAddsDefaultRule() {
    FluidGridData data = new FluidGridData();

    Rule rule = data.getRules().get( 0 );
    assertTrue( rule.getConditions().get( 0 ) instanceof AlwaysTrueContidtion );
    assertTrue( rule.getInstructions().get( 0 ) instanceof ColumnsInstruction );
  }

  @Test
  public void testDefaultRuleObtainsOneColumn() {
    FluidGridData data = new FluidGridData();

    Rule rule = data.getRules().get( 0 );
    ColumnsInstruction instruction = ( ColumnsInstruction )rule.getInstructions().get( 0 );
    assertEquals( 1, instruction.getColumns() );
  }
}

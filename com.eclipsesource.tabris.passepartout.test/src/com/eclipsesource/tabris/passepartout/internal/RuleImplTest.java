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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Condition;
import com.eclipsesource.tabris.passepartout.Instruction;


public class RuleImplTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullConditions() {
    new RuleImpl( null, new ArrayList<Instruction>() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullInstructions() {
    new RuleImpl( new ArrayList<Condition>(), null );
  }

  @Test
  public void testHasConditions() {
    ArrayList<Condition> conditions = new ArrayList<Condition>();
    Condition condition = mock( Condition.class );
    conditions.add( condition );
    RuleImpl rule = new RuleImpl( conditions, new ArrayList<Instruction>() );

    List<Condition> actualConditions = rule.getConditions();

    assertEquals( condition, actualConditions.get( 0 ) );
    assertEquals( 1, actualConditions.size() );
  }

  @Test
  public void testGetConditionsReturnsSafeCopy() {
    ArrayList<Condition> conditions = new ArrayList<Condition>();
    conditions.add( mock( Condition.class ) );
    RuleImpl rule = new RuleImpl( conditions, new ArrayList<Instruction>() );

    List<Condition> actualConditions = rule.getConditions();
    List<Condition> actualConditions2 = rule.getConditions();

    assertNotSame( actualConditions, actualConditions2 );
  }

  @Test
  public void testHasInstructions() {
    ArrayList<Instruction> instructions = new ArrayList<Instruction>();
    Instruction instruction = mock( Instruction.class );
    instructions.add( instruction );
    RuleImpl rule = new RuleImpl( new ArrayList<Condition>(), instructions );

    List<Instruction> actualInstructions = rule.getInstructions();

    assertEquals( instruction, actualInstructions.get( 0 ) );
    assertEquals( 1, actualInstructions.size() );
  }

  @Test
  public void testGetInstructionsReturnsSafeCopy() {
    RuleImpl rule = new RuleImpl( new ArrayList<Condition>(), new ArrayList<Instruction>() );

    List<Instruction> actualInstructions = rule.getInstructions();
    List<Instruction> actualInstructions2 = rule.getInstructions();

    assertNotSame( actualInstructions, actualInstructions2 );
  }
}

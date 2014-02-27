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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Condition;
import com.eclipsesource.tabris.passepartout.Instruction;
import com.eclipsesource.tabris.passepartout.Query;
import com.eclipsesource.tabris.passepartout.Rule;


public class QueryImplTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCondition() {
    new QueryImpl( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAndFailsWithNullCondition() {
    QueryImpl query = new QueryImpl( mock( Condition.class ) );

    query.and( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testThenFailsWithNullInstruction() {
    QueryImpl query = new QueryImpl( mock( Condition.class ) );

    query.then( null );
  }

  @Test
  public void testAndReturnsSameQuery() {
    QueryImpl query = new QueryImpl( mock( Condition.class ) );

    Query actualQuery = query.and( mock( Condition.class ) );

    assertSame( query, actualQuery );
  }

  @Test
  public void testHasInitialCondition() {
    Condition condition = mock( Condition.class );
    QueryImpl query = new QueryImpl( condition );

    List<Condition> conditions = query.then( mock( Instruction.class ) ).getConditions();

    assertEquals( conditions.get( 0 ), condition );
    assertEquals( 1, conditions.size() );
  }

  @Test
  public void testAndAddsCondition() {
    Condition condition = mock( Condition.class );
    Condition condition2 = mock( Condition.class );
    QueryImpl query = new QueryImpl( condition );

    query.and( condition2 );

    List<Condition> conditions = query.then( mock( Instruction.class ) ).getConditions();
    assertEquals( conditions.get( 0 ), condition );
    assertEquals( conditions.get( 1 ), condition2 );
    assertEquals( 2, conditions.size() );
  }

  @Test
  public void testGetConditionsIsSafeCopy() {
    QueryImpl query = new QueryImpl( mock( Condition.class ) );

    List<Condition> conditions = query.then( mock( Instruction.class ) ).getConditions();
    List<Condition> conditions2 = query.then( mock( Instruction.class ) ).getConditions();

    assertNotSame( conditions, conditions2 );
  }

  @Test
  public void testThenAddsInstruction() {
    QueryImpl query = new QueryImpl( mock( Condition.class ) );
    Instruction instruction = mock( Instruction.class );

    Rule rule = query.then( instruction );

    List<Instruction> instructions = rule.getInstructions();
    assertEquals( instructions.get( 0 ), instruction );
  }

  @Test
  public void testThenAddsAllInstructions() {
    QueryImpl query = new QueryImpl( mock( Condition.class ) );
    Instruction instruction = mock( Instruction.class );
    Instruction instruction2 = mock( Instruction.class );
    Instruction instruction3 = mock( Instruction.class );

    Rule rule = query.then( instruction, instruction2, instruction3 );

    List<Instruction> instructions = rule.getInstructions();
    assertEquals( instructions.get( 0 ), instruction );
    assertEquals( instructions.get( 1 ), instruction2 );
    assertEquals( instructions.get( 2 ), instruction3 );
  }

  @Test
  public void testHasConditions() {
    Condition condition1 = mock( Condition.class );
    Condition condition2 = mock( Condition.class );
    QueryImpl query = ( QueryImpl )new QueryImpl( condition1 ).and( condition2 );

    List<Condition> conditions = query.getConditions();

    assertEquals( 2, conditions.size() );
    assertTrue( conditions.contains( condition1 ) );
    assertTrue( conditions.contains( condition2 ) );
  }

  @Test
  public void testConditionsAreSafeCopy() {
    Condition condition1 = mock( Condition.class );
    Condition condition2 = mock( Condition.class );
    QueryImpl query = ( QueryImpl )new QueryImpl( condition1 ).and( condition2 );

    List<Condition> conditions = query.getConditions();
    conditions.add( mock( Condition.class ) );
    List<Condition> actualConditions = query.getConditions();

    assertEquals( 2, actualConditions.size() );
    assertTrue( actualConditions.contains( condition1 ) );
    assertTrue( actualConditions.contains( condition2 ) );
  }
}

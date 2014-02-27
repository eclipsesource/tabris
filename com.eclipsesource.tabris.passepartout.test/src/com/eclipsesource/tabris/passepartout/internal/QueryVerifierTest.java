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

import static com.eclipsesource.tabris.passepartout.test.PassPartoutTestUtil.createEnvironment;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Condition;
import com.eclipsesource.tabris.passepartout.Query;
import com.eclipsesource.tabris.passepartout.Rule;
import com.eclipsesource.tabris.passepartout.UIEnvironment;
import com.eclipsesource.tabris.passepartout.internal.condition.AlwaysTrueContidtion;


public class QueryVerifierTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBounds() {
    new QueryVerifier( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullQuery() {
    QueryVerifier verifier = new QueryVerifier( createEnvironment() );

    verifier.complies( ( Query )null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullRule() {
    QueryVerifier verifier = new QueryVerifier( createEnvironment() );

    verifier.complies( ( Rule )null );
  }

  @Test
  public void testCompliesWithQuery() {
    QueryVerifier verifier = new QueryVerifier( createEnvironment() );
    QueryImpl query = new QueryImpl( new AlwaysTrueContidtion() );

    boolean complies = verifier.complies( query );

    assertTrue( complies );
  }

  @Test
  public void testCompliesNotWithOneFalseConditions() {
    QueryVerifier verifier = new QueryVerifier( createEnvironment() );
    Condition condition = mock( Condition.class );
    doReturn( Boolean.FALSE ).when( condition ).compliesWith( any( UIEnvironment.class ) );
    QueryImpl query = new QueryImpl( new AlwaysTrueContidtion() );

    boolean complies = verifier.complies( query.and( condition ) );

    assertFalse( complies );
  }

  @Test
  public void testCompliesWithRule() {
    Rule rule = mock( Rule.class );
    QueryVerifier verifier = new QueryVerifier( createEnvironment() );
    List<Condition> conditions = new ArrayList<Condition>();
    conditions.add( new AlwaysTrueContidtion() );
    when( rule.getConditions() ).thenReturn( conditions );

    boolean complies = verifier.complies( rule );

    assertTrue( complies );
  }

  @Test
  public void testCompliesNotWithRuleWithOneFalseCondition() {
    Rule rule = mock( Rule.class );
    QueryVerifier verifier = new QueryVerifier( createEnvironment() );
    Condition condition = mock( Condition.class );
    doReturn( Boolean.FALSE ).when( condition ).compliesWith( any( UIEnvironment.class ) );
    List<Condition> conditions = new ArrayList<Condition>();
    conditions.add( new AlwaysTrueContidtion() );
    conditions.add( condition );
    when( rule.getConditions() ).thenReturn( conditions );

    boolean complies = verifier.complies( rule );

    assertFalse( complies );
  }
}

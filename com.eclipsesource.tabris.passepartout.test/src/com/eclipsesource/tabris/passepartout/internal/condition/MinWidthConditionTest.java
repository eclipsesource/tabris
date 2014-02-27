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
package com.eclipsesource.tabris.passepartout.internal.condition;

import static com.eclipsesource.tabris.passepartout.test.PassPartoutTestUtil.createEnvironment;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;

import com.eclipsesource.tabris.passepartout.Bounds;
import com.eclipsesource.tabris.passepartout.Unit;
import com.eclipsesource.tabris.passepartout.internal.UIEnvironmentImpl;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;
import com.eclipsesource.tabris.passepartout.internal.unit.Pixel;
import com.eclipsesource.tabris.passepartout.test.PassPartoutTestUtil;


public class MinWidthConditionTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWidthnullMinWidth() {
    new MinWidthCondition( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWidthNegativeMinWidth() {
    new MinWidthCondition( new Pixel( -1 ) );
  }

  @Test
  public void testCompliesWithWidthBiggerThanMinWidth() {
    MinWidthCondition condition = new MinWidthCondition( new Pixel( 100 ) );
    Bounds bounds = new Bounds( 10, 10, 110, 0 );

    boolean complies = condition.compliesWith( createEnvironment( bounds ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesWithWidthSameThanMinWidth() {
    MinWidthCondition condition = new MinWidthCondition( new Pixel( 100 ) );
    Bounds bounds = new Bounds( 10, 10, 100, 0 );

    boolean complies = condition.compliesWith( createEnvironment( bounds ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesNotWithWidthSmallerThanMinWidth() {
    MinWidthCondition condition = new MinWidthCondition( new Pixel( 100 ) );
    Bounds bounds = new Bounds( 10, 10, 90, 0 );

    boolean complies = condition.compliesWith( createEnvironment( bounds ) );

    assertFalse( complies );
  }

  @Test
  public void testCompliesWithWidthBiggerThanMinWidthInEm() {
    MinWidthCondition condition = new MinWidthCondition( new Em( BigDecimal.valueOf( 3 ) ) );
    Bounds bounds = new Bounds( 10, 10, 100, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, mock( Bounds.class ), 16 ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesWithWidthSameThanMinWidthInEm() {
    MinWidthCondition condition = new MinWidthCondition( new Em( BigDecimal.valueOf( 10 ) ) );
    Bounds bounds = new Bounds( 10, 10, 160, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, mock( Bounds.class ), 16 ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesNotWithWidthSmallerThanMinWidthInEm() {
    MinWidthCondition condition = new MinWidthCondition( new Em( BigDecimal.valueOf( 10 ) ) );
    Bounds bounds = new Bounds( 10, 10, 100, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, mock( Bounds.class ), 16 ) );

    assertFalse( complies );
  }

  @Test
  public void testCompliesWithWidthBiggerThanMinWidthInPercentage() {
    MinWidthCondition condition = new MinWidthCondition( new Percentage( BigDecimal.valueOf( 10 ) ) );
    Bounds bounds = new Bounds( 10, 10, 11, 0 );
    Bounds referenceBounds = new Bounds( 10, 10, 100, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, referenceBounds, 16 ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesWithWidthSameThanMinWidthInPercentage() {
    MinWidthCondition condition = new MinWidthCondition( new Percentage( BigDecimal.valueOf( 10 ) ) );
    Bounds bounds = new Bounds( 10, 10, 10, 0 );
    Bounds referenceBounds = new Bounds( 10, 10, 100, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, referenceBounds, 16 ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesNotWithWidthSmallerThanMinWidthInPercentage() {
    MinWidthCondition condition = new MinWidthCondition( new Percentage( BigDecimal.valueOf( 10 ) ) );
    Bounds bounds = new Bounds( 10, 10, 9, 0 );
    Bounds referenceBounds = new Bounds( 10, 10, 100, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, referenceBounds, 16 ) );

    assertFalse( complies );
  }

  @Test
  public void testReturnsFalsForUnsupportedUnit() {
    Unit unit = mock( Unit.class );
    when( unit.getValue() ).thenReturn( BigDecimal.ONE );
    MinWidthCondition condition = new MinWidthCondition( unit );

    boolean complies = condition.compliesWith( PassPartoutTestUtil.createEnvironment() );

    assertFalse( complies );
  }
}

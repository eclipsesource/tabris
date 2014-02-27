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


public class MaxWidthConditionTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWidthNullMaxWidth() {
    new MaxWidthCondition( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWidthZeroMaxWidth() {
    new MaxWidthCondition( new Pixel( 0 ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWidthNegativeMaxWidth() {
    new MaxWidthCondition( new Pixel( -1 ) );
  }

  @Test
  public void testCompliesNotWithWidthBiggerThanMaxWidth() {
    MaxWidthCondition condition = new MaxWidthCondition( new Pixel( 100 ) );
    Bounds bounds = new Bounds( 10, 10, 110, 0 );

    boolean complies = condition.compliesWith( createEnvironment( bounds ) );

    assertFalse( complies );
  }

  @Test
  public void testCompliesWithWidthSameThanMaxWidth() {
    MaxWidthCondition condition = new MaxWidthCondition( new Pixel( 100 ) );
    Bounds bounds = new Bounds( 10, 10, 100, 0 );

    boolean complies = condition.compliesWith( createEnvironment( bounds ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesWithWidthSmallerThanMaxWidth() {
    MaxWidthCondition condition = new MaxWidthCondition( new Pixel( 100 ) );
    Bounds bounds = new Bounds( 10, 10, 90, 0 );

    boolean complies = condition.compliesWith( createEnvironment( bounds ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesWithWidthSmallerThanMaxWidthEm() {
    MaxWidthCondition condition = new MaxWidthCondition( new Em( BigDecimal.TEN ) );
    Bounds bounds = new Bounds( 10, 10, 90, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, new Bounds( 0, 0, 0, 0 ), 16 ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesNotWithWidthBiggerThanMaxWidthEm() {
    MaxWidthCondition condition = new MaxWidthCondition( new Em( BigDecimal.valueOf( 100 ) ) );
    Bounds bounds = new Bounds( 10, 10, 90, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, new Bounds( 0, 0, 0, 0 ), 16 ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesWithWidthSmallerThanMaxWidthPercentage() {
    MaxWidthCondition condition = new MaxWidthCondition( new Percentage( BigDecimal.valueOf( 50 ) ) );
    Bounds bounds = new Bounds( 10, 10, 100, 0 );
    Bounds referenceBounds = new Bounds( 10, 10, 200, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, referenceBounds, 16 ) );

    assertTrue( complies );
  }

  @Test
  public void testCompliesNotWithWidthBiggerThanMaxWidthPercentage() {
    MaxWidthCondition condition = new MaxWidthCondition( new Percentage( BigDecimal.valueOf( 40 ) ) );
    Bounds bounds = new Bounds( 10, 10, 100, 0 );
    Bounds referenceBounds = new Bounds( 10, 10, 200, 0 );

    boolean complies = condition.compliesWith( new UIEnvironmentImpl( bounds, referenceBounds, 16 ) );

    assertFalse( complies );
  }

  @Test
  public void testReturnsFalsForUnsupportedUnit() {
    Unit unit = mock( Unit.class );
    when( unit.getValue() ).thenReturn( BigDecimal.ONE );
    MaxWidthCondition condition = new MaxWidthCondition( unit );

    boolean complies = condition.compliesWith( PassPartoutTestUtil.createEnvironment() );

    assertFalse( complies );
  }
}

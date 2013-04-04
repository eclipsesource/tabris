/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Serializable;

import org.junit.Test;


public class ActionOperatorImplTest {

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( ActionOperatorImpl.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullController() {
    new ActionOperatorImpl( null );
  }

  @Test
  public void testEnablesActionsByDefault() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    boolean enabled = actionOperator.isActionEnabled( "foo" );

    assertTrue( enabled );
  }

  @Test
  public void testDisbalesActions() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.setActionEnabled( "foo", false );

    boolean enabled = actionOperator.isActionEnabled( "foo" );
    assertFalse( enabled );
    verify( controller ).setActionEnabled( "foo", false );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testDisbalesActionFailsForNullId() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.setActionEnabled( null, false );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testDisbalesActionFailsForEmptyId() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.setActionEnabled( "", false );
  }

  @Test
  public void testActionIsVisibleByDefault() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    boolean visible = actionOperator.isActionVisible( "foo" );

    assertTrue( visible );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActionIsVisibleFailsWithNullId() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.isActionVisible( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActionIsVisibleFailsWithEmptyId() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.isActionVisible( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActionIsEnabledFailsWithNullId() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.isActionEnabled( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActionIsEnabledFailsWithEmptyId() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.isActionEnabled( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetEnabledFailsWithNullId() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.setActionEnabled( null, true );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetEnabledFailsWithEmptyId() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.setActionEnabled( "", true );
  }

  @Test
  public void testHidesAction() {
    Controller controller = mock( Controller.class );
    ActionOperatorImpl actionOperator = new ActionOperatorImpl( controller );

    actionOperator.setActionVisible( "foo", false );

    boolean visible = actionOperator.isActionVisible( "foo" );
    assertFalse( visible );
    verify( controller ).setActionVisible( "foo", false );
  }

}

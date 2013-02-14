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

import org.junit.Test;


public class ActionManagerImplTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullController() {
    new ActionManagerImpl( null );
  }

  @Test
  public void testEnablesActionsByDefault() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    boolean enabled = actionManager.isActionEnabled( "foo" );

    assertTrue( enabled );
  }

  @Test
  public void testDisbalesActions() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.setActionEnabled( "foo", false );

    boolean enabled = actionManager.isActionEnabled( "foo" );
    assertFalse( enabled );
    verify( controller ).setActionEnabled( "foo", false );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testDisbalesActionFailsForNullId() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.setActionEnabled( null, false );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testDisbalesActionFailsForEmptyId() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.setActionEnabled( "", false );
  }

  @Test
  public void testActionIsVisibleByDefault() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    boolean visible = actionManager.isActionVisible( "foo" );

    assertTrue( visible );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActionIsVisibleFailsWithNullId() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.isActionVisible( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActionIsVisibleFailsWithEmptyId() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.isActionVisible( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActionIsEnabledFailsWithNullId() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.isActionEnabled( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActionIsEnabledFailsWithEmptyId() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.isActionEnabled( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetEnabledFailsWithNullId() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.setActionEnabled( null, true );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetEnabledFailsWithEmptyId() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.setActionEnabled( "", true );
  }

  @Test
  public void testHidesAction() {
    Controller controller = mock( Controller.class );
    ActionManagerImpl actionManager = new ActionManagerImpl( controller );

    actionManager.setActionVisible( "foo", false );

    boolean visible = actionManager.isActionVisible( "foo" );
    assertFalse( visible );
    verify( controller ).setActionVisible( "foo", false );
  }

}

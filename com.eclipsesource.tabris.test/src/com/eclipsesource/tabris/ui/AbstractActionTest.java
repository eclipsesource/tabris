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
package com.eclipsesource.tabris.ui;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;



public class AbstractActionTest {

  @Test
  public void testRemembersUI() {
    UI ui = mock( UI.class );
    TestAbstractAction action = new TestAbstractAction();

    action.execute( ui );

    assertSame( ui, action.getUI() );
  }

  @Test
  public void testDelegatesExecuteCall() {
    UI ui = mock( UI.class );
    TestAbstractAction action = spy( new TestAbstractAction() );

    action.execute( ui );

    verify( action ).execute();
  }

  @Test
  public void testSetsTitle() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    Page page = mock( Page.class );
    when( operator.getCurrentPage() ).thenReturn( page );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractAction action = spy( new TestAbstractAction() );
    action.execute( ui );

    action.setPageTitle( "foo" );

    verify( operator ).setCurrentPageTitle( "foo" );
  }

  @Test
  public void testCanCloseCurrentPagePage() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractAction action = spy( new TestAbstractAction() );
    action.execute( ui );

    action.closeCurrentPage();

    verify( operator ).closeCurrentPage();
  }

  @Test
  public void testCanOpenNewPage() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractAction action = spy( new TestAbstractAction() );
    action.execute( ui );

    action.openPage( "foo" );

    verify( operator ).openPage( "foo" );
  }

  @Test
  public void testCanOpenNewPageWithData() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractAction action = spy( new TestAbstractAction() );
    action.execute( ui );

    action.openPage( "foo", data );

    verify( operator ).openPage( "foo", data );
  }

  @Test
  public void testCanHideAction() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    ActionOperator actionOperator = mock( ActionOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    when( ui.getActionOperator() ).thenReturn( actionOperator );
    TestAbstractAction action = spy( new TestAbstractAction() );
    action.execute( ui );

    action.setActionVisible( "foo", false );

    verify( actionOperator ).setActionVisible( "foo", false );
  }

  @Test
  public void testCanDisableAction() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    ActionOperator actionOperator = mock( ActionOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    when( ui.getActionOperator() ).thenReturn( actionOperator );
    TestAbstractAction action = spy( new TestAbstractAction() );
    action.execute( ui );

    action.setActionEnabled( "foo", false );

    verify( actionOperator ).setActionEnabled( "foo", false );
  }

  private static class TestAbstractAction extends AbstractAction {

    @Override
    public void execute() {
      // nothing to do here
    }

  }
}

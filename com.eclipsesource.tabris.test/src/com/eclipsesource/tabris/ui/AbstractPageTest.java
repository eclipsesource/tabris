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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.tabris.test.RWTRunner;


@RunWith( RWTRunner.class )
public class AbstractPageTest {

  private Shell shell;

  @Before
  public void setUp() {
    shell = new Shell( new Display() );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( AbstractPage.class ) );
  }

  @Test
  public void testRemembersUI() {
    UI ui = mock( UI.class );
    PageOperator operator = mock( PageOperator.class );
    PageData data = new PageData();
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractPage page = new TestAbstractPage();

    page.createContent( shell, ui );

    assertSame( ui, page.getUI() );
  }

  @Test
  public void testUsesUIConfigurationOfUI() {
    UI ui = mock( UI.class );
    UIConfiguration config = mock( UIConfiguration.class );
    when( ui.getConfiguration() ).thenReturn( config );
    PageOperator operator = mock( PageOperator.class );
    PageData data = new PageData();
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractPage page = new TestAbstractPage();
    page.createContent( shell, ui );

    UIConfiguration actualConfiguration = page.getUIConfiguration();

    assertSame( config, actualConfiguration );
  }

  @Test
  public void testCanGetPageData() {
    UI ui = mock( UI.class );
    PageOperator operator = mock( PageOperator.class );
    PageData data = new PageData();
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractPage page = new TestAbstractPage();
    page.createContent( shell, ui );

    PageData actualData = page.getData();

    assertSame( data, actualData );
  }

  @Test
  public void testCallsCreateContentsWithPageData() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractPage page = spy( new TestAbstractPage() );

    page.createContent( shell, ui );

    verify( page ).createContent( shell, data );
  }

  @Test
  public void testSetsTitle() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractPage page = spy( new TestAbstractPage() );
    page.createContent( shell, ui );

    page.setTitle( "foo" );

    verify( operator ).setCurrentPageTitle( "foo" );
  }

  @Test
  public void testClosesPage() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractPage page = spy( new TestAbstractPage() );
    page.createContent( shell, ui );

    page.close();

    verify( operator ).closeCurrentPage();
  }

  @Test
  public void testCanOpenNewPage() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractPage page = spy( new TestAbstractPage() );
    page.createContent( shell, ui );

    page.openPage( "foo" );

    verify( operator ).openPage( "foo" );
  }

  @Test
  public void testCanOpenNewPageWithData() {
    UI ui = mock( UI.class );
    PageData data = new PageData();
    PageOperator operator = mock( PageOperator.class );
    when( operator.getCurrentPageData() ).thenReturn( data );
    when( ui.getPageOperator() ).thenReturn( operator );
    TestAbstractPage page = spy( new TestAbstractPage() );
    page.createContent( shell, ui );

    page.openPage( "foo", data );

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
    TestAbstractPage page = spy( new TestAbstractPage() );
    page.createContent( shell, ui );

    page.setActionVisible( "foo", false );

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
    TestAbstractPage page = spy( new TestAbstractPage() );
    page.createContent( shell, ui );

    page.setActionEnabled( "foo", false );

    verify( actionOperator ).setActionEnabled( "foo", false );
  }

  private static class TestAbstractPage extends AbstractPage {

    @Override
    public void createContent( Composite parent, PageData data ) {
      // nothing to do here
    }

  }
}

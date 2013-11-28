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
package com.eclipsesource.tabris.internal.ui.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.ui.PageOperator;
import com.eclipsesource.tabris.ui.UI;


public class WebUITest {

  private Display display;
  private Shell shell;
  private WebUI webUI;

  @Before
  public void setUp() {
    Fixture.setUp();
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    display = new Display();
    shell = new Shell( display );
    webUI = new WebUI( shell );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testCreateActionsBarParent() {
    Composite actionsParent = webUI.getActionsParent();

    assertSame( shell.getChildren()[ 0 ], actionsParent.getParent() );
  }

  @Test
  public void testCreatePageParent() {
    Composite pageParent = webUI.getPageParent();

    assertSame( shell.getChildren()[ 1 ], pageParent );
  }

  @Test
  public void testCreateBackButton() {
    Button backButton = webUI.getBackButton();

    assertNotNull( backButton );
    assertFalse( backButton.isListening( SWT.Selection ) );
    assertFalse( backButton.getEnabled() );
    assertNull( backButton.getImage() );
  }

  @Test
  public void testCreatePageSwitcher() {
    ToolBar pageSwitcher = webUI.getPageSwitcher();

    assertNotNull( pageSwitcher );
    assertEquals( 1, pageSwitcher.getItemCount() );
    assertTrue( pageSwitcher.getItem( 0 ).isListening( SWT.Selection ) );
  }

  @Test
  public void testCreatePageSwitcher_pageSwitcherMenu() {
    Menu pageSwitcherMenu = webUI.getPageSwitcherMenu();

    assertNotNull( pageSwitcherMenu );
  }

  public void testCreateActionsBar() throws Exception {
    Composite actionsBar = webUI.getActionsParent();

    assertNotNull( actionsBar );
  }

  @Test
  public void testPageCreated_topLevelPage_addsMenuItem() {
    WebPage page = mock( WebPage.class );
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    doReturn( "Title" ).when( descriptor ).getTitle();
    when( page.getDescriptor() ).thenReturn( descriptor );

    webUI.pageCreated( page );

    assertEquals( 1, webUI.getPageSwitcherMenu().getItemCount() );
    MenuItem item = webUI.getPageSwitcherMenu().getItem( 0 );
    assertEquals( "Title", item.getText() );
    assertSame( descriptor, item.getData() );
    assertTrue( item.isListening( SWT.Selection ) );
  }

  @Test
  public void testPageCreated_regularPage_ignored() {
    WebPage page = mock( WebPage.class );
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( Boolean.FALSE ).when( descriptor ).isTopLevel();
    doReturn( "Title" ).when( descriptor ).getTitle();
    when( page.getDescriptor() ).thenReturn( descriptor );

    webUI.pageCreated( page );

    assertEquals( 0, webUI.getPageSwitcherMenu().getItemCount() );
  }

  @Test
  public void testPageDestroyed_removesMenuItem() {
    WebPage page = mock( WebPage.class );
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    doReturn( "Title" ).when( descriptor ).getTitle();
    when( page.getDescriptor() ).thenReturn( descriptor );
    webUI.pageCreated( page );

    webUI.pageDestroyed( page );

    assertEquals( 0, webUI.getPageSwitcherMenu().getItemCount() );
  }

  @Test
  public void testPageDestroyed_regularPage_ignored() {
    WebPage page = mock( WebPage.class );
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( Boolean.FALSE ).when( descriptor ).isTopLevel();
    doReturn( "Title" ).when( descriptor ).getTitle();
    when( page.getDescriptor() ).thenReturn( descriptor );
    webUI.pageCreated( page );

    webUI.pageDestroyed( page );

    assertEquals( 0, webUI.getPageSwitcherMenu().getItemCount() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testActivate_failsWithNullRenderer() {
    webUI.activate( null );
  }

  @Test
  public void testActivate_topLevelPage_updatesNavigation() {
    WebPage page = createWebPage( true );

    webUI.activate( page );

    assertTrue( webUI.getPageSwitcher().getVisible() );
    assertFalse( webUI.getBackButton().isListening( SWT.Selection ) );
    assertFalse( webUI.getBackButton().getEnabled() );
  }

  @Test
  public void testActivate_regularPage_updatesNavigation() {
    WebPage page = createWebPage( false );

    webUI.activate( page );

    assertTrue( webUI.getPageSwitcher().getVisible() );
    assertTrue( webUI.getBackButton().isListening( SWT.Selection ) );
    assertTrue( webUI.getBackButton().getEnabled() );
  }

  @Test
  public void testActivate_doesNotCallOpenPage() {
    UI ui = mock( UI.class );
    PageOperator pageOperator = mock( PageOperator.class );
    when( ui.getPageOperator() ).thenReturn( pageOperator );
    webUI.setUi( ui );
    WebPage page = createWebPage( true );
    webUI.pageCreated( page );

    webUI.activate( page );

    verify( pageOperator, never() ).openPage( "foo" );
  }

  @Test
  public void testBackButtonSelection_callsCloseCurrentPage() {
    UI ui = mock( UI.class );
    PageOperator pageOperator = mock( PageOperator.class );
    when( ui.getPageOperator() ).thenReturn( pageOperator );
    webUI.setUi( ui );
    webUI.pageCreated( createWebPage( true ) );
    WebPage page = createWebPage( false );
    webUI.pageCreated( page );
    webUI.activate( page );

    webUI.getBackButton().notifyListeners( SWT.Selection, new Event() );

    verify( pageOperator ).closeCurrentPage();
  }

  @Test
  public void testMenuItemSelection_callsOpenPage() {
    UI ui = mock( UI.class );
    PageOperator pageOperator = mock( PageOperator.class );
    when( ui.getPageOperator() ).thenReturn( pageOperator );
    webUI.setUi( ui );
    WebPage page = createWebPage( true );
    webUI.pageCreated( page );

    webUI.getPageSwitcherMenu().getItem( 0 ).notifyListeners( SWT.Selection, new Event() );

    verify( pageOperator ).openPage( "foo" );
  }

  @Test
  public void testPageSwitcherSelection_showsMenu() {
    webUI.getPageSwitcher().getItem( 0 ).notifyListeners( SWT.Selection, new Event() );

    assertTrue( webUI.getPageSwitcherMenu().getVisible() );
  }

  @Test
  public void testLayoutOrder() {
    List<Composite> log = new ArrayList<Composite>();
    Composite actionsParent = webUI.getActionsParent();
    actionsParent.setLayout( new TestLayout( log ) );
    Composite uiParent = actionsParent.getParent();
    uiParent.setLayout( new TestLayout( log ) );

    webUI.layout();

    assertSame( actionsParent, log.get( 0 ) );
    assertSame( uiParent, log.get( 1 ) );
  }

  private WebPage createWebPage( boolean topLevel ) {
    WebPage page = mock( WebPage.class );
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( "foo" ).when( descriptor ).getId();
    doReturn( Boolean.valueOf( topLevel ) ).when( descriptor ).isTopLevel();
    when( page.getDescriptor() ).thenReturn( descriptor );
    return page;
  }

  private final class TestLayout extends Layout {

    private final List<Composite> log;

    public TestLayout( List<Composite> log ) {
      this.log = log;
    }

    @Override
    protected void layout( Composite composite, boolean flushCache ) {
      log.add( composite );
    }

    @Override
    protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
      return null;
    }

  }

}

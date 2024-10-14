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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.internal.serverpush.ServerPushManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.ImageUtil;
import com.eclipsesource.tabris.internal.ui.PropertyChangeHandler;
import com.eclipsesource.tabris.internal.ui.PropertyChangeNotifier;
import com.eclipsesource.tabris.internal.ui.RemoteActionTest;
import com.eclipsesource.tabris.internal.ui.TestSearchAction;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.UIConfiguration;
import com.eclipsesource.tabris.ui.action.ProposalHandler;
import com.eclipsesource.tabris.ui.action.SearchAction;
import com.eclipsesource.tabris.ui.action.SearchActionListener;


@SuppressWarnings("restriction")
public class WebSearchActionTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  private UI ui;
  private WebUI uiRenderer;
  private ActionDescriptor actionDescriptor;
  private WebSearchAction webSearchAction;
  private SearchAction searchAction;
  private Display display;
  private Button control;
  private Text text;
  private Menu proposalsMenu;

  @Before
  public void setUp() {
    display = new Display();
    Shell shell = new Shell( display );
    shell.open();
    ui = mock( UI.class );
    mockUI( mock( SearchActionListener.class ) );
    uiRenderer = mock( WebUI.class );
    searchAction = spy( new TestSearchAction() );
    actionDescriptor = mockDescriptor( searchAction );
    webSearchAction = new WebSearchAction( ui, uiRenderer, actionDescriptor );
    webSearchAction.createUi( shell );
    control = webSearchAction.getControl();
    text = webSearchAction.getText();
    proposalsMenu = webSearchAction.getProposalsMenu();
  }

  @Test
  public void testSetsItselfAsChangeHandler() {
    webSearchAction = new WebSearchAction( ui, uiRenderer, actionDescriptor );

    PropertyChangeHandler handler = searchAction.getAdapter( PropertyChangeNotifier.class ).getPropertyChangeHandler();
    assertSame( handler, webSearchAction );
  }

  @Test
  public void testGetUI() {
    assertSame( ui, webSearchAction.getUI() );
  }

  @Test
  public void testGetDescriptor() {
    assertSame( actionDescriptor, webSearchAction.getDescriptor() );
  }

  @Test
  public void testInitControl() {
    assertFalse( control.isEnabled() );
    assertFalse( control.isVisible() );
    assertNotNull( control.getImage() );
    assertEquals( "bar", control.getToolTipText() );
    assertTrue( control.isListening( SWT.Selection ) );
  }

  @Test
  public void testInitText() {
    assertFalse( text.getVisible() );
    assertTrue( ( ( RowData )text.getLayoutData() ).exclude );
    assertTrue( text.isListening( SWT.DefaultSelection ) );
    assertTrue( text.isListening( SWT.FocusIn ) );
    assertTrue( text.isListening( SWT.FocusOut ) );
    assertFalse( text.isListening( SWT.Modify ) );
  }

  @Test
  public void testInitProposalsMenu() {
    assertFalse( proposalsMenu.isVisible() );
    assertEquals( 0, proposalsMenu.getItemCount() );
  }

  @Test
  public void testDestroy() {
    webSearchAction.destroy();

    assertTrue( control.isDisposed() );
    assertTrue( text.isDisposed() );
    assertTrue( proposalsMenu.isDisposed() );
  }

  @Test
  public void testSetsVisible() {
    webSearchAction.setVisible( true );

    assertTrue( control.getVisible() );
  }

  @Test
  public void testSetEnabled() {
    webSearchAction.setEnabled( true );

    assertTrue( control.isEnabled() );
  }

  @Test
  public void testOpen_executesActions() {
    webSearchAction.open();

    verify( searchAction ).execute( ui );
  }

  @Test
  public void testOpen_activatesText() {
    webSearchAction.open();

    assertTrue( text.getVisible() );
    assertTrue( text.isListening( SWT.Modify ) );
    assertFalse( ( ( RowData )text.getLayoutData() ).exclude );
    assertSame( text, display.getFocusControl() );
  }

  @Test
  public void testOpen_deactivatesControl() {
    webSearchAction.open();

    assertFalse( control.getVisible() );
    assertTrue( ( ( RowData )control.getLayoutData() ).exclude );
  }

  @Test
  public void testOpen_doesLayout() {
    webSearchAction.open();

    verify( uiRenderer ).layout();
  }

  @Test
  public void testSetQuery() {
    webSearchAction.setQuery( "foo" );

    assertEquals( "foo", text.getText() );
  }

  @Test
  public void testSetMessage() {
    webSearchAction.setMessage( "foo" );

    assertEquals( "foo", text.getMessage() );
  }

  @Test
  public void testControlSelectionEvent_activatesText() {
    control.notifyListeners( SWT.Selection, new Event() );

    assertTrue( text.getVisible() );
    assertTrue( text.isListening( SWT.Modify ) );
    assertFalse( ( ( RowData )text.getLayoutData() ).exclude );
    assertSame( text, display.getFocusControl() );
  }

  @Test
  public void testControlSelectionEvent_callsActionExecute() {
    control.notifyListeners( SWT.Selection, new Event() );

    verify( searchAction ).execute( ui );
  }

  @Test
  public void testDoSearch_deactivatesText() {
    webSearchAction.open();

    text.notifyListeners( SWT.DefaultSelection, new Event() );

    assertFalse( text.getVisible() );
    assertFalse( text.isListening( SWT.Modify ) );
    assertTrue( ( ( RowData )text.getLayoutData() ).exclude );
  }

  @Test
  public void testDoSearch_callsActionSearch() {
    webSearchAction.open();
    text.setText( "foo" );

    text.notifyListeners( SWT.DefaultSelection, new Event() );

    verify( searchAction ).search( "foo" );
  }

  @Test
  public void testDoSearchNotifiesListener() {
    SearchActionListener listener = mock( SearchActionListener.class );
    mockUI( listener );
    webSearchAction.open();
    text.setText( "foo" );

    text.notifyListeners( SWT.DefaultSelection, new Event() );

    verify( listener ).searched( ui, searchAction, "foo" );
  }

  @Test
  public void testDoSearch_clearsText() {
    webSearchAction.open();
    text.setText( "foo" );

    text.notifyListeners( SWT.DefaultSelection, new Event() );

    assertEquals( "", text.getText() );
  }

  @Test
  public void testDoSearch_clearsMenuItems() {
    webSearchAction.open();
    new MenuItem( proposalsMenu, SWT.PUSH );

    text.notifyListeners( SWT.DefaultSelection, new Event() );

    assertEquals( 0, proposalsMenu.getItemCount() );
  }

  @Test
  public void testFocusIn_activatesServerPush() {
    text.notifyListeners( SWT.FocusIn, new Event() );

    assertTrue( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testFocusOut_deactivatesServerPush() {
    text.notifyListeners( SWT.FocusOut, new Event() );

    assertFalse( ServerPushManager.getInstance().isServerPushActive() );
  }

  @Test
  public void testFocusOut_deactivatesText() {
    text.notifyListeners( SWT.FocusOut, new Event() );

    assertFalse( text.getVisible() );
    assertFalse( text.isListening( SWT.Modify ) );
    assertTrue( ( ( RowData )text.getLayoutData() ).exclude );
  }

  @Test
  public void testModify_callsActionModify() {
    webSearchAction.open();

    text.setText( "foo" );

    verify( searchAction ).modified( eq( "foo" ), any( ProposalHandler.class ) );
  }

  @Test
  public void testModifyNotifiesListenerModify() {
    SearchActionListener listener = mock( SearchActionListener.class );
    mockUI( listener );
    webSearchAction.open();

    text.setText( "foo" );

    verify( listener ).modified( ui, searchAction, "foo" );
  }

  @Test
  public void testShowProposals_updatesMenuItems() {
    List<String> proposals = new ArrayList<String>();
    proposals.add( "foo" );
    proposals.add( "bar" );

    webSearchAction.showProposals( proposals );

    assertEquals( 2, proposalsMenu.getItemCount() );
    assertEquals( "foo", proposalsMenu.getItem( 0 ).getText() );
    assertEquals( "bar", proposalsMenu.getItem( 1 ).getText() );
  }

  @Test
  public void testShowProposals_showsMenu_withProposals() {
    List<String> proposals = new ArrayList<String>();
    proposals.add( "foo" );
    proposals.add( "bar" );

    webSearchAction.showProposals( proposals );

    assertTrue( proposalsMenu.getVisible() );
  }

  @Test
  public void testShowProposals_doesNotShowMenu_withoutProposals() {
    List<String> proposals = new ArrayList<String>();

    webSearchAction.showProposals( proposals );

    assertFalse( proposalsMenu.getVisible() );
  }

  private ActionDescriptor mockDescriptor( Action action ) {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( action );
    InputStream image = RemoteActionTest.class.getResourceAsStream( "testImage.png" );
    when( descriptor.getImage() ).thenReturn( ImageUtil.getBytes( image ) );
    when( descriptor.getTitle() ).thenReturn( "bar" );
    doReturn( Boolean.FALSE ).when( descriptor ).isEnabled();
    doReturn( Boolean.FALSE ).when( descriptor ).isVisible();
    return descriptor;
  }

  private void mockUI( SearchActionListener listener ) {
    UIConfiguration configuration = new UIConfiguration();
    configuration.addActionListener( listener );
    when( ui.getConfiguration() ).thenReturn( configuration );
  }

}

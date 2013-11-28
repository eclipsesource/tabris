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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.CUSTOM_VARIANT_TABRIS_UI;
import static com.eclipsesource.tabris.internal.ui.ImageUtil.getImage;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.eclipsesource.tabris.internal.ui.Controller;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.ui.UI;

public class WebUI implements UIRenderer {

  private static final int BACK_BUTTON_SIZE = 32;
  private static final String BACK_BUTTON_CUSTOM_VARIANT = CUSTOM_VARIANT_TABRIS_UI + "_back";
  private static final String DATA_ACTIVATED = "activated";

  private final Shell shell;
  private UI ui;
  private Composite pageParent;
  private Composite uiParent;
  private Button backButton;
  private Listener backButtonSelectionListener;
  private ToolBar pageSwitcher;
  private Menu pageSwitcherMenu;
  private Composite actionsBar;

  public WebUI( Shell shell ) {
    this.shell = shell;
    init();
    createUIParent();
    createPageParent();
    createBackButton();
    createPageSwitcher();
    createSeparator();
    createActionsBar();
  }

  @Override
  public void setUi( UI ui ) {
    this.ui = ui;
  }

  @Override
  public void setController( Controller controller ) {
  }

  @Override
  public void activate( PageRenderer page ) {
    whenNull( page ).throwIllegalArgument( "Page must not be null" );
    updatePageNavigationBar( page.getDescriptor() );
    ( ( WebPage )page ).pageActivated();
    layout();
  }

  @Override
  public void setForeground( Color color ) {
    // has no effect on web
  }

  @Override
  public void setBackground( Color color ) {
    // has no effect on web
  }

  @Override
  public void setImage( Image image ) {
    // has no effect on web
  }

  @Override
  public Composite getPageParent() {
    return pageParent;
  }

  @Override
  public Composite getActionsParent() {
    return actionsBar;
  }

  void pageCreated( WebPage page ) {
    if( page.getDescriptor().isTopLevel() ) {
      addMenuItem( page.getDescriptor() );
    }
  }

  void pageDestroyed( WebPage page ) {
    if( page.getDescriptor().isTopLevel() ) {
      removeMenuItem( page.getDescriptor() );
    }
  }

  void layout() {
    actionsBar.layout();
    uiParent.layout();
  }

  Button getBackButton() {
    return backButton;
  }

  ToolBar getPageSwitcher() {
    return pageSwitcher;
  }

  Menu getPageSwitcherMenu() {
    return pageSwitcherMenu;
  }

  private void init() {
    shell.setLayout( createShellLayout() );
  }

  private GridLayout createShellLayout() {
    GridLayout layout = new GridLayout( 1, false );
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    layout.verticalSpacing = 0;
    return layout;
  }

  private void createPageParent() {
    pageParent = new Composite( shell, SWT.NONE );
    pageParent.setLayoutData( new GridData( GridData.FILL, GridData.FILL, true, true ) );
  }

  private void createUIParent() {
    uiParent = new Composite( shell, SWT.NONE );
    uiParent.setData( RWT.CUSTOM_VARIANT, CUSTOM_VARIANT_TABRIS_UI );
    uiParent.setLayoutData( new GridData( GridData.FILL, GridData.CENTER, true, false ) );
    uiParent.setBackgroundMode( SWT.INHERIT_DEFAULT );
    GridLayout layout = new GridLayout( 4, false );
    layout.marginHeight = 0;
    layout.marginWidth = 10;
    layout.verticalSpacing = 0;
    uiParent.setLayout( layout );
  }

  private void createBackButton() {
    backButton = new Button( uiParent, SWT.PUSH );
    backButton.setData( RWT.CUSTOM_VARIANT, BACK_BUTTON_CUSTOM_VARIANT );
    backButton.setLayoutData( new GridData( BACK_BUTTON_SIZE, BACK_BUTTON_SIZE ) );
    backButton.setEnabled( false );
    backButtonSelectionListener = new BackButtonSelectionListener();
  }

  private void createPageSwitcher() {
    pageSwitcher = new ToolBar( uiParent, SWT.NONE );
    pageSwitcher.setData( RWT.CUSTOM_VARIANT, CUSTOM_VARIANT_TABRIS_UI );
    pageSwitcher.setLayoutData( new GridData( GridData.FILL, GridData.CENTER, false, false ) );
    ToolItem pageSwitcherDropDown = new ToolItem( pageSwitcher, SWT.DROP_DOWN );
    pageSwitcherDropDown.setData( RWT.CUSTOM_VARIANT, CUSTOM_VARIANT_TABRIS_UI );
    pageSwitcherDropDown.addListener( SWT.Selection, new PageSwitcherSelectionListener() );
    pageSwitcherMenu = new Menu( uiParent.getShell(), SWT.POP_UP );
    pageSwitcherMenu.setData( RWT.CUSTOM_VARIANT, CUSTOM_VARIANT_TABRIS_UI );
  }

  private void createSeparator() {
    Composite separator = new Composite( uiParent, SWT.NONE );
    GridData layoutData = new GridData( GridData.FILL, GridData.CENTER, true, false );
    layoutData.heightHint = 1;
    separator.setLayoutData( layoutData );
  }

  private void createActionsBar() {
    actionsBar = new Composite( uiParent, SWT.NONE );
    actionsBar.setLayoutData( new GridData( GridData.FILL, GridData.CENTER, false, false ) );
    RowLayout layout = new RowLayout();
    layout.wrap = false;
    layout.center = true;
    actionsBar.setLayout( layout );
  }

  private void updatePageNavigationBar( PageDescriptor pageDescriptor ) {
    if( pageDescriptor.isTopLevel() ) {
      backButton.removeListener( SWT.Selection, backButtonSelectionListener );
      backButton.setEnabled( false );
      notifyMenuItemSelected( pageDescriptor );
    } else {
      if( !backButton.isListening( SWT.Selection ) ) {
        backButton.addListener( SWT.Selection, backButtonSelectionListener );
      }
      backButton.setEnabled( true );
    }
  }

  private void notifyMenuItemSelected( PageDescriptor pageDescriptor ) {
    for( MenuItem item : pageSwitcherMenu.getItems() ) {
      Object itemData = item.getData();
      if( itemData == pageDescriptor ) {
        Event event = new Event();
        event.data = DATA_ACTIVATED;
        item.notifyListeners( SWT.Selection, event );
        break;
      }
    }
  }

  private void addMenuItem( PageDescriptor pageDescriptor ) {
    MenuItem item = new MenuItem( pageSwitcherMenu, SWT.PUSH );
    item.setData( RWT.CUSTOM_VARIANT, CUSTOM_VARIANT_TABRIS_UI );
    String title = pageDescriptor.getTitle();
    item.setText( title == null ? "" : title );
    item.setImage( getImage( uiParent.getDisplay(), pageDescriptor.getImage() ) );
    item.setData( pageDescriptor );
    item.addListener( SWT.Selection, new MenuItemSelectionListener() );
  }

  private void removeMenuItem( PageDescriptor pageDescriptor ) {
    for( MenuItem item : pageSwitcherMenu.getItems() ) {
      Object itemData = item.getData();
      if( pageDescriptor == itemData ) {
        item.dispose();
        break;
      }
    }
  }

  private final class BackButtonSelectionListener implements Listener {

    @Override
    public void handleEvent( Event event ) {
      ui.getPageOperator().closeCurrentPage();
    }
  }

  private final class PageSwitcherSelectionListener implements Listener {

    @Override
    public void handleEvent( Event event ) {
      ToolItem item = ( ToolItem )event.widget;
      Rectangle bounds = item.getBounds();
      bounds.y += bounds.height;
      Point point = pageSwitcher.toDisplay( bounds.x + 10, bounds.y );
      pageSwitcherMenu.setLocation( point );
      pageSwitcherMenu.setVisible( true );
    }
  }

  private final class MenuItemSelectionListener implements Listener {

    @Override
    public void handleEvent( Event event ) {
      MenuItem item = ( MenuItem )event.widget;
      PageDescriptor pageDescriptor = ( PageDescriptor )item.getData();
      ToolItem dropDown = pageSwitcher.getItem( 0 );
      dropDown.setText( item.getText() );
      dropDown.setImage( item.getImage() );
      if( !DATA_ACTIVATED.equals( event.data ) ) {
        ui.getPageOperator().openPage( pageDescriptor.getId() );
      }
    }
  }
}

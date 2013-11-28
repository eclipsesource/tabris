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

import java.util.List;

import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.PropertyChangeHandler;
import com.eclipsesource.tabris.internal.ui.PropertyChangeNotifier;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.action.ProposalHandler;
import com.eclipsesource.tabris.ui.action.SearchAction;


public class WebSearchAction extends WebAction implements PropertyChangeHandler {

  private static final int TEXT_WIDTH = 200;

  private Text text;
  private Menu proposalsMenu;
  private final Listener textModifyListener;

  public WebSearchAction( UI ui, WebUI uiRenderer, ActionDescriptor descriptor ) {
    super( ui, uiRenderer, descriptor );
    textModifyListener = new TextModifyListener( this );
    Action action = descriptor.getAction();
    ( ( Adaptable )action ).getAdapter( PropertyChangeNotifier.class ).setPropertyChangeHandler( this );
  }

  @Override
  public void createUi( Composite uiParent ) {
    super.createUi( uiParent );
    createText( uiParent );
    createProposalsMenu();
  }

  @Override
  public void destroy() {
    super.destroy();
    if( text != null ) {
      text.dispose();
      text = null;
    }
    if( proposalsMenu != null ) {
      proposalsMenu.dispose();
      proposalsMenu = null;
    }
  }

  @Override
  public void propertyChanged( String key, Object value ) {
    if( key.equals( "open" ) ) {
      open();
    } else if( key.equals( "query" ) ) {
      setQuery( ( String )value );
    } else if( key.equals( "message" ) ) {
      setMessage( ( String )value );
    }
  }

  public void open() {
    whenNull( text ).throwIllegalState( "UI is not created" );
    getDescriptor().getAction().execute( getUI() );
    actionExecuted();
  }

  public void setQuery( String query ) {
    whenNull( text ).throwIllegalState( "UI is not created" );
    text.setText( query );
  }

  public void setMessage( String message ) {
    whenNull( text ).throwIllegalState( "UI is not created" );
    text.setMessage( message );
  }

  @Override
  protected void actionExecuted() {
    activateText( true );
  }

  private void createText( Composite uiParent ) {
    if( text == null ) {
      text = new Text( uiParent, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL );
      text.setData( RWT.CUSTOM_VARIANT, CUSTOM_VARIANT_TABRIS_UI );
      text.setLayoutData( createRowData( TEXT_WIDTH, SWT.DEFAULT, false ) );
      text.setVisible( false );
      text.addListener( SWT.DefaultSelection, new TextDefaultSelectionListener() );
      text.addFocusListener( new TextFocusListener() );
    }
  }

  private void createProposalsMenu() {
    if( proposalsMenu == null ) {
      proposalsMenu = new Menu( getControl().getShell(), SWT.POP_UP );
      proposalsMenu.setData( RWT.CUSTOM_VARIANT, CUSTOM_VARIANT_TABRIS_UI );
    }
  }

  Text getText() {
    return text;
  }

  Menu getProposalsMenu() {
    return proposalsMenu;
  }

  void showProposals( List<String> proposals ) {
    if( !proposalsMenu.isDisposed() ) {
      updateMenuItems( proposals );
      if( !proposals.isEmpty() ) {
        showProposalsMenu();
      }
    }
  }

  private void showProposalsMenu() {
    Control control = getControl();
    Rectangle bounds = control.getBounds();
    bounds.y += bounds.height + 5;
    Point point = control.toDisplay( bounds.x, bounds.y );
    proposalsMenu.setLocation( point );
    proposalsMenu.setVisible( true );
  }

  private void doSearch( String query ) {
    ActionDescriptor descriptor = getDescriptor();
    SearchAction action = ( SearchAction )descriptor.getAction();
    action.search( query );
    activateText( false );
    text.setText( "" );
    clearMenuItems();
  }

  private void updateMenuItems( List<String> proposals ) {
    clearMenuItems();
    for( String proposal : proposals ) {
      if( proposal != null ) {
        createMenuItem( proposal );
      }
    }
  }

  private void clearMenuItems() {
    for( MenuItem item : proposalsMenu.getItems() ) {
      item.dispose();
    }
  }

  private void createMenuItem( String proposal ) {
    MenuItem item = new MenuItem( proposalsMenu, SWT.PUSH );
    item.setData( RWT.CUSTOM_VARIANT, CUSTOM_VARIANT_TABRIS_UI );
    item.setText( proposal );
    item.addListener( SWT.Selection, new MenuItemSelectionListener() );
  }

  private void activateText( boolean active ) {
    Button button = getControl();
    button.setLayoutData( createRowData( SWT.DEFAULT, SWT.DEFAULT, !active ) );
    button.setVisible( !active );
    text.setLayoutData( createRowData( TEXT_WIDTH, SWT.DEFAULT, active ) );
    text.setVisible( active );
    if( active ) {
      if( !text.isListening( SWT.Modify ) ) {
        text.addListener( SWT.Modify, textModifyListener );
      }
      text.setFocus();
    } else {
      text.removeListener( SWT.Modify, textModifyListener );
    }
    layoutWebUI();
  }

  private final class TextDefaultSelectionListener implements Listener {

    @Override
    public void handleEvent( Event event ) {
      if( event.detail != SWT.ICON_CANCEL ) {
        Text text = ( Text )event.widget;
        doSearch( text.getText() );
      }
    }

  }

  private final class TextModifyListener implements Listener {

    private final ProposalHandler proposalHandler;

    public TextModifyListener( WebSearchAction actionRenderer ) {
      proposalHandler = new WebProposalHandlerImpl( actionRenderer ) ;
    }

    @Override
    public void handleEvent( Event event ) {
      Text text = ( Text )event.widget;
      SearchAction action = ( SearchAction )getDescriptor().getAction();
      action.modified( text.getText(), proposalHandler );
    }

  }

  private final class TextFocusListener implements FocusListener {

    private final ServerPushSession pushSession;

    public TextFocusListener() {
      pushSession = new ServerPushSession();
    }

    @Override
    public void focusGained( FocusEvent event ) {
      pushSession.start();
    }

    @Override
    public void focusLost( FocusEvent event ) {
      pushSession.stop();
      activateText( false );
    }

  }

  private final class MenuItemSelectionListener implements Listener {

    @Override
    public void handleEvent( Event event ) {
      MenuItem item = ( MenuItem )event.widget;
      doSearch( item.getText() );
    }

  }

}

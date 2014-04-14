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

import static com.eclipsesource.tabris.internal.Constants.EVENT_MODIFY;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SEARCH;
import static com.eclipsesource.tabris.internal.Constants.METHOD_ACTIVATE;
import static com.eclipsesource.tabris.internal.Constants.METHOD_DEACTIVATE;
import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_QUERY;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.rap.rwt.service.ServerPushSession;

import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.ActionListener;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.action.SearchAction;
import com.eclipsesource.tabris.ui.action.SearchActionListener;


public class RemoteSearchAction extends RemoteAction implements PropertyChangeHandler {

  private ServerPushSession pushSession;

  public RemoteSearchAction( UI ui, RemoteUI uiRenderer, ActionDescriptor descriptor ) {
    super( ui, uiRenderer, descriptor );
    Action action = descriptor.getAction();
    ( ( Adaptable )action ).getAdapter( PropertyChangeNotifier.class ).setPropertyChangeHandler( this );
  }

  @Override
  protected String getType() {
    return "tabris.SearchAction";
  }

  @Override
  public void propertyChanged( String key, Object value ) {
    if( key.equals( METHOD_OPEN ) ) {
      getDescriptor().getAction().execute( getUI() );
      getRemoteObject().call( METHOD_OPEN, null );
    } else if( key.equals( PROPERTY_QUERY ) ) {
      getRemoteObject().set( PROPERTY_QUERY, ( String )value );
    } else if( key.equals( PROPERTY_MESSAGE ) ) {
      getRemoteObject().set( PROPERTY_MESSAGE, ( String )value );
    }
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    super.handleNotify( event, properties );
    SearchAction action = ( SearchAction )getDescriptor().getAction();
    if( event.equals( EVENT_SEARCH ) ) {
      handleSearch( properties, action );
    } else if( event.equals( EVENT_MODIFY ) ) {
      handleModify( properties, action );
    }
  }

  private void handleSearch( JsonObject properties, SearchAction action ) {
    String query = properties.get( PROPERTY_QUERY ).asString();
    action.search( query );
    notifyListenersAboutSearch( action, query );
  }

  private void notifyListenersAboutSearch( Action action, String query ) {
    UIDescriptor uiDescriptor = getUI().getConfiguration().getAdapter( UIDescriptor.class );
    for( ActionListener listener : uiDescriptor.getActionListeners() ) {
      if( listener instanceof SearchActionListener ) {
        ( ( SearchActionListener )listener ).searched( getUI(), action, query );
      }
    }
  }

  private void handleModify( JsonObject properties, SearchAction action ) {
    String query = properties.get( PROPERTY_QUERY ).asString();
    action.modified( query, new ProposalHandlerImpl( getRemoteObject() ) );
    notifyListenersAboutModify( action, query );
  }

  private void notifyListenersAboutModify( Action action, String query ) {
    UIDescriptor uiDescriptor = getUI().getConfiguration().getAdapter( UIDescriptor.class );
    for( ActionListener listener : uiDescriptor.getActionListeners() ) {
      if( listener instanceof SearchActionListener ) {
        ( ( SearchActionListener )listener ).modified( getUI(), action, query );
      }
    }
  }

  @Override
  public void handleCall( String method, JsonObject parameters ) {
    if( method.equals( METHOD_ACTIVATE ) ) {
      startPush();
    } else if( method.equals( METHOD_DEACTIVATE ) ) {
      stopPush();
    }
  }

  private void startPush() {
    getUI().getDisplay().syncExec( new Runnable() {

      @Override
      public void run() {
        pushSession = new ServerPushSession();
        pushSession.start();
      }
    } );
  }

  private void stopPush() {
    getUI().getDisplay().syncExec( new Runnable() {

      @Override
      public void run() {
        pushSession.stop();
      }
    } );
  }

}

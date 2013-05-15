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
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_QUERY;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.service.ServerPushSession;

import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.action.SearchAction;


public class RemoteSearchAction extends RemoteAction {

  private ServerPushSession pushSession;

  public RemoteSearchAction( UI ui, ActionDescriptor descriptor, String parentId ) {
    super( ui, descriptor, parentId );
  }

  @Override
  protected String getType() {
    return "tabris.SearchAction";
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    super.handleNotify( event, properties );
    SearchAction action = ( SearchAction )getDescriptor().getAction();
    if( event.equals( EVENT_SEARCH ) ) {
      action.search( properties.get( PROPERTY_QUERY ).asString() );
    } else if( event.equals( EVENT_MODIFY ) ) {
      action.modified( properties.get( PROPERTY_QUERY ).asString(), new ProposalHandlerImpl( getRemoteObject() ) );
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

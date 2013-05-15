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

import static com.eclipsesource.tabris.internal.Constants.EVENT_SHOW_PAGE;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SHOW_PREVIOUS_PAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ACTIVE_PAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_BACKGROUND;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_FOREGROUND;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PAGE_ID;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SHELL;
import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNullAndNotEmpty;
import static org.eclipse.rap.rwt.RWT.getUISession;
import static org.eclipse.rap.rwt.lifecycle.WidgetUtil.getId;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.ui.UI;


@SuppressWarnings("restriction")
public class RemoteUI extends AbstractOperationHandler {


  private final RemoteObjectImpl remoteObject;
  private UI ui;
  private Controller controller;

  public RemoteUI( Shell shell ) {
    remoteObject = ( RemoteObjectImpl )getUISession().getConnection().createRemoteObject( "tabris.UI" );
    remoteObject.setHandler( this );
    remoteObject.set( PROPERTY_SHELL, getId( shell ) );
  }

  public void setUi( UI ui ) {
    this.ui = ui;
  }

  public void setController( Controller controller ) {
    this.controller = controller;
  }

  public String getRemoteUIId() {
    return remoteObject.getId();
  }

  public void activate( String pageId ) {
    checkArgumentNotNullAndNotEmpty( pageId, "PageId" );
    remoteObject.set( PROPERTY_ACTIVE_PAGE, pageId );
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( event.equals( EVENT_SHOW_PAGE ) ) {
      String remotePageId = properties.get( PROPERTY_PAGE_ID ).asString();
      String pageId = controller.getPageId( remotePageId );
      ui.getPageOperator().openPage( pageId );
    } else if( event.equals( EVENT_SHOW_PREVIOUS_PAGE ) ) {
      ui.getPageOperator().closeCurrentPage();
    }
  }

  public void setForeground( Color color ) {
    setColor( PROPERTY_FOREGROUND, color );
  }

  public void setBackground( Color color ) {
    setColor( PROPERTY_BACKGROUND, color );
  }

  private void setColor( String name, Color color ) {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add( color.getRed() );
    jsonArray.add( color.getGreen() );
    jsonArray.add( color.getBlue() );
    remoteObject.set( name, jsonArray );
  }

}

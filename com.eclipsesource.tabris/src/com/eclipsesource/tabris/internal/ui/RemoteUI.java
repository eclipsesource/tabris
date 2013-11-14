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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SHOW_PAGE;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SHOW_PREVIOUS_PAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ACTIVE_PAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_BACKGROUND;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_FOREGROUND;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PAGE_ID;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SHELL;
import static org.eclipse.rap.rwt.RWT.getUISession;
import static org.eclipse.rap.rwt.lifecycle.WidgetUtil.getId;

import java.util.List;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.ui.UI;


public class RemoteUI extends AbstractOperationHandler implements UIRenderer {

  private final RemoteObject remoteObject;
  private final Shell shell;
  private UI ui;
  private Controller controller;

  public RemoteUI( Shell shell ) {
    this.shell = shell;
    this.remoteObject = getUISession().getConnection().createRemoteObject( "tabris.UI" );
    this.remoteObject.setHandler( this );
    this.remoteObject.set( PROPERTY_SHELL, getId( shell ) );
  }

  @Override
  public void setUi( UI ui ) {
    this.ui = ui;
  }

  @Override
  public void setController( Controller controller ) {
    this.controller = controller;
  }

  @Override
  public Composite getPageParent() {
    return shell;
  }

  @Override
  public Composite getActionsParent() {
    return shell;
  }

  @Override
  public void activate( PageRenderer page ) {
    whenNull( page ).throwIllegalArgument( "Page must not be null" );
    remoteObject.set( PROPERTY_ACTIVE_PAGE, ( ( RemotePage )page ).getId() );
  }

  @Override
  public void handleNotify( String event, JsonObject properties ) {
    if( event.equals( EVENT_SHOW_PAGE ) ) {
      String pageId = getPageId( properties.get( PROPERTY_PAGE_ID ).asString() );
      ui.getPageOperator().openPage( pageId );
    } else if( event.equals( EVENT_SHOW_PREVIOUS_PAGE ) ) {
      ui.getPageOperator().closeCurrentPage();
    }
  }

  @Override
  public void setForeground( Color color ) {
    setColor( PROPERTY_FOREGROUND, color );
  }

  @Override
  public void setBackground( Color color ) {
    setColor( PROPERTY_BACKGROUND, color );
  }

  String getRemoteUIId() {
    return remoteObject.getId();
  }

  String getPageId( String pageRendererId ) {
    List<PageRenderer> pageRenderers = controller.getAllPages();
    for( PageRenderer pageRenderer : pageRenderers ) {
      RemotePage remotePage = ( RemotePage )pageRenderer;
      if( remotePage.getId().equals( pageRendererId ) ) {
        return remotePage.getDescriptor().getId();
      }
    }
    throw new IllegalStateException( "RemotePage with id " + pageRendererId + " does not exist." );
  }

  private void setColor( String name, Color color ) {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add( color.getRed() );
    jsonArray.add( color.getGreen() );
    jsonArray.add( color.getBlue() );
    remoteObject.set( name, jsonArray );
  }

}

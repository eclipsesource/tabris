/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.GRID_ITEM_HEIGHT_SETTER;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ITEM_HEIGHT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TARGET;
import static com.eclipsesource.tabris.internal.Constants.SET_ITEM_HEIGHT;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ClientService;
import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

@SuppressWarnings("restriction")
public class TableItemHeightService extends AbstractOperationHandler implements ClientService {

  private final RemoteObject remoteObject;

  public TableItemHeightService() {
    remoteObject = ( ( ConnectionImpl )RWT.getUISession().getConnection() ).createServiceObject( GRID_ITEM_HEIGHT_SETTER );
    remoteObject.setHandler( this );
  }

  @Override
  public void handleCall( String method, JsonObject parameters ) {
    if( ( SET_ITEM_HEIGHT ).equals( method ) ) {
      setCustomItemHeight( parameters );
    }
  }

  private void setCustomItemHeight( JsonObject parameters ) {
    String widgetID = parameters.get( PROPERTY_TARGET ).asString();
    int itemHeight = parameters.get( PROPERTY_ITEM_HEIGHT ).asInt();
    Widget widget = findWidget( widgetID );
    setRowHeight( itemHeight, widget );
  }

  private void setRowHeight( int itemHeight, Widget widget ) {
    if( wigetExists( widget ) && ( widget instanceof Table || widget instanceof Tree ) ) {
      widget.setData( RWT.CUSTOM_ITEM_HEIGHT, new Integer( itemHeight ) );
    }
  }

  private boolean wigetExists( Widget widget ) {
    return widget != null && !widget.isDisposed();
  }

  Widget findWidget( String widgetID ) {
    Display display = Display.getCurrent();
    Shell[] shells = display.getShells();
    for( Shell shell : shells ) {
      Widget widget = findWidget( widgetID, shell );
      if( widget != null ) {
        return widget;
      }
    }
    return null;
  }

  private Widget findWidget( String widgetID, Shell shell ) {
    return WidgetUtil.find( shell, widgetID );
  }
}

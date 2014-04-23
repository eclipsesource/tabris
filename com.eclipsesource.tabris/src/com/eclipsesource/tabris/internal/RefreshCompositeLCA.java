/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.EVENT_REFRESH;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CLIENT_AREA;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARENT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_RESET;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_STYLE;
import static com.eclipsesource.tabris.internal.Constants.TYPE_REFRESH_COMPOSITE;
import static org.eclipse.rap.rwt.internal.protocol.JsonUtil.createJsonArray;
import static org.eclipse.rap.rwt.internal.protocol.RemoteObjectFactory.createRemoteObject;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.getStyles;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.preserveBackgroundGradient;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.preserveCustomVariant;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.preserveListener;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.preserveProperty;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.preserveRoundedBorder;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.renderBackgroundGradient;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.renderClientListeners;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.renderCustomVariant;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.renderListener;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.renderProperty;
import static org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil.renderRoundedBorder;
import static org.eclipse.rap.rwt.lifecycle.WidgetUtil.getId;

import java.io.IOException;
import java.io.Serializable;

import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rap.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rap.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.eclipsesource.tabris.widgets.RefreshComposite;


@SuppressWarnings("restriction")
public class RefreshCompositeLCA extends AbstractWidgetLCA implements Serializable {

  private static final String[] ALLOWED_STYLES = new String[] { "NO_RADIO_GROUP", "BORDER" };

  @Override
  public void preserveValues( Widget widget ) {
    ControlLCAUtil.preserveValues( ( Control )widget );
    preserveCustomVariant( widget );
    preserveBackgroundGradient( widget );
    preserveRoundedBorder( widget );
    RefreshComposite composite = ( RefreshComposite )widget;
    preserveProperty( composite, PROPERTY_CLIENT_AREA, composite.getClientArea() );
    preserveProperty( composite, PROPERTY_MESSAGE, composite.getMessage() );
    preserveProperty( composite, PROPERTY_RESET, composite.getAdapter( ResetAdapter.class ).wantReset() );
    preserveListener( composite, EVENT_REFRESH, !composite.getRefreshListeners().isEmpty() );
  }

  @Override
  public void renderInitialization( Widget widget ) throws IOException {
    RefreshComposite composite = ( RefreshComposite )widget;
    RemoteObject remoteObject = createRemoteObject( composite, TYPE_REFRESH_COMPOSITE );
    remoteObject.setHandler( new RefreshCompositeOperationHandler( composite ) );
    remoteObject.set( PROPERTY_PARENT, getId( composite.getParent() ) );
    remoteObject.set( PROPERTY_STYLE, createJsonArray( getStyles( composite, ALLOWED_STYLES ) ) );
    initializeListenToRefresh( composite, remoteObject );
    initializeMessage( remoteObject, composite.getMessage() );
  }

  private void initializeListenToRefresh( RefreshComposite composite, RemoteObject remoteObject ) {
    boolean hasRefreshListeners = !composite.getRefreshListeners().isEmpty();
    if( hasRefreshListeners ) {
      remoteObject.listen( EVENT_REFRESH, hasRefreshListeners );
    }
  }

  private void initializeMessage( RemoteObject remoteObject, String message ) {
    if( message != null ) {
      remoteObject.set( PROPERTY_MESSAGE, JsonValue.valueOf( message ) );
    }
  }

  @Override
  public void renderChanges( Widget widget ) throws IOException {
    RefreshComposite composite = ( RefreshComposite )widget;
    ControlLCAUtil.renderChanges( composite );
    renderBackgroundGradient( composite );
    renderRoundedBorder( composite );
    renderCustomVariant( composite );
    renderClientArea( composite );
    renderClientListeners( composite );
    renderListenToRefresh( composite );
    renderMessage( composite );
    renderReset( composite );
  }

  void renderClientArea( RefreshComposite composite ) {
    renderProperty( composite, PROPERTY_CLIENT_AREA, composite.getClientArea(), null );
  }

  void renderMessage( RefreshComposite composite ) {
    renderProperty( composite, PROPERTY_MESSAGE, composite.getMessage(), null );
  }

  void renderReset( RefreshComposite composite ) {
    ResetAdapter adapter = composite.getAdapter( ResetAdapter.class );
    boolean wantReset = adapter.wantReset();
    if( WidgetLCAUtil.hasChanged( composite, PROPERTY_RESET, Boolean.valueOf( wantReset ) ) ) {
      renderProperty( composite, PROPERTY_RESET, Boolean.valueOf( wantReset ), null );
      adapter.setReset( false );
    }
  }

  void renderListenToRefresh( RefreshComposite composite ) {
    boolean hasRefreshListeners = !composite.getRefreshListeners().isEmpty();
    renderListener( composite, EVENT_REFRESH, hasRefreshListeners, false );
  }

  public static class ResetAdapter {

    private boolean reset;

    public void setReset( boolean reset ) {
      this.reset = reset;
    }

    public boolean wantReset() {
      return reset;
    }
  }
}

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
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.internal.lifecycle.ProcessActionRunner;
import org.eclipse.swt.internal.widgets.canvaskit.CanvasOperationHandler;
import org.eclipse.swt.widgets.Canvas;

import com.eclipsesource.tabris.widgets.ClientCanvas;
import com.eclipsesource.tabris.widgets.ClientDrawListener;


@SuppressWarnings("restriction")
public class ClientCanvasOperator extends CanvasOperationHandler {

  public static final String DRAWING_EVENT = "Drawing";
  public static final String DRAWINGS_PROPERTY = "drawings";

  public ClientCanvasOperator( ClientCanvas canvas ) {
    super( canvas );
    whenNull( canvas ).throwIllegalArgument( "ClientCanvas must not be null" );
  }

  @Override
  public void handleNotify( final Canvas control, String eventName, JsonObject properties ) {
    if( eventName.equals( DRAWING_EVENT ) ) {
      handleDrawings( control, properties );
    } else {
      super.handleNotify( control, eventName, properties );
    }
  }

  private void handleDrawings( final Canvas control, final JsonObject properties ) {
    ProcessActionRunner.add( new Runnable() {

      @Override
      public void run() {
        DrawingsCache cache = control.getAdapter( DrawingsCache.class );
        JsonValue drawings = properties.get( DRAWINGS_PROPERTY );
        if( drawings != null ) {
          cache.cache( drawings.asString() );
          cache.clearRemoved();
          fireDrawEvent( ( ClientCanvas )control );
        }
      }
    } );
  }

  @SuppressWarnings("unchecked")
  private void fireDrawEvent(ClientCanvas control) {
    if( !control.isDisposed() ) {
      List<ClientDrawListener> listeners = new ArrayList<ClientDrawListener>( control.getAdapter( List.class ) );
      for( ClientDrawListener listener : listeners ) {
        listener.receivedDrawing();
      }
    }
  }
}
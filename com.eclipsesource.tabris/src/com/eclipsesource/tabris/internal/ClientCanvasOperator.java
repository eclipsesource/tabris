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

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.lifecycle.ProcessActionRunner;
import org.eclipse.swt.internal.widgets.canvaskit.CanvasOperationHandler;
import org.eclipse.swt.widgets.Canvas;

import com.eclipsesource.tabris.widgets.ClientCanvas;


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
      ProcessActionRunner.add( new Runnable() {

        @Override
        public void run() {
          control.redraw();
        }
      } );
    } else {
      super.handleNotify( control, eventName, properties );
    }
  }

}

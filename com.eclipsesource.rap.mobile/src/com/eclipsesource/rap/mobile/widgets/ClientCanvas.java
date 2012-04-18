/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.rap.mobile.widgets;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.application.RWTFactory;
import org.eclipse.rwt.internal.lifecycle.LifeCycleUtil;
import org.eclipse.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rwt.lifecycle.PhaseEvent;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.rwt.lifecycle.PhaseListener;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.rwt.service.SessionStoreEvent;
import org.eclipse.rwt.service.SessionStoreListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.rap.mobile.internal.GCOperationDispatcher;


/**
 * Provisional API, may change in future versions. Use it at your own risk.
 */
@SuppressWarnings("restriction")
public class ClientCanvas extends Canvas implements PhaseListener, SessionStoreListener {
  
  static final String CLIENT_CANVAS = "CLIENT_CANVAS";
  
  private List<String> cachedDrawings;
  private PaintListener paintListener;

  public ClientCanvas( Composite parent, int style ) {
    super( parent, style );
    cachedDrawings = new ArrayList<String>();
    RWT.getLifeCycle().addPhaseListener( this );
    RWT.getSessionStore().addSessionStoreListener( this );
    addDispatchPaintListener();
    setData( WidgetUtil.CUSTOM_VARIANT, CLIENT_CANVAS );
  }

  private void addDispatchPaintListener() {
    paintListener = new PaintListener() {
      public void paintControl( PaintEvent event ) {
        GC gc = event.gc;
        processClientDrawings( gc );
      }
    };
    addPaintListener( paintListener );
  }
  
  public void clear() {
    cachedDrawings.clear();
    redraw();
  }

  @Override
  public void addPaintListener( PaintListener listener ) {
    removePaintListener( paintListener );
    super.addPaintListener( listener );
    super.addPaintListener( paintListener );
  }
  
  private void processClientDrawings( GC gc ) {
    String drawings = getDrawings();
    if( drawings != null ) {
      cacheDrawings( drawings );
    }
    dispatchDrawings( gc );
  }

  private void cacheDrawings( String drawings ) {
    if( !cachedDrawings.contains( drawings ) ) {
      cachedDrawings.add( drawings );
    }
  }

  private void dispatchDrawings( GC gc ) {
    if( !cachedDrawings.isEmpty() ) {
      for( String drawing : cachedDrawings) {
        GCOperationDispatcher dispatcher = new GCOperationDispatcher( gc, drawing );
        dispatcher.dispatch();
      }
    }
  }

  public void beforePhase( PhaseEvent event ) {
    Display sessionDisplay = LifeCycleUtil.getSessionDisplay();
    if( getDisplay() == sessionDisplay ) {
      if( getDrawings() != null ) {
        redraw();
      }
    }
  }

  private String getDrawings() {
    HttpServletRequest request = RWT.getRequest();
    IClientObjectAdapter adapter = getAdapter( IClientObjectAdapter.class );
    String drawings = request.getParameter( adapter.getId() + ".drawings" );
    return drawings;
  }

  public void afterPhase( PhaseEvent event ) {
    // do nothing
  }

  public PhaseId getPhaseId() {
    return PhaseId.PROCESS_ACTION;
  }

  public void beforeDestroy( SessionStoreEvent event ) {
    // TODO: Check RAP bug #375356
    RWTFactory.getLifeCycleFactory().getLifeCycle().removePhaseListener( this );
    RWT.getSessionStore().removeSessionStoreListener( this );
  }

}

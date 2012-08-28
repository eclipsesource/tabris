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
package com.eclipsesource.tabris.widgets;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.application.RWTFactory;
import org.eclipse.rap.rwt.internal.lifecycle.LifeCycleUtil;
import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rap.rwt.lifecycle.PhaseEvent;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.PhaseListener;
import org.eclipse.rap.rwt.service.SessionStoreEvent;
import org.eclipse.rap.rwt.service.SessionStoreListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.internal.DrawingsCache;
import com.eclipsesource.tabris.internal.GCOperationDispatcher;


/**
 * Provisional API, may change in future versions. Use it at your own risk.
 * @since 0.6
 */
@SuppressWarnings("restriction")
public class ClientCanvas extends Canvas implements PhaseListener, SessionStoreListener {
  
  static final String CLIENT_CANVAS = "CLIENT_CANVAS";
  
  private List<ClientDrawListener> drawListeners;
  private DrawingsCache cache;
  private PaintListener paintListener;

  public ClientCanvas( Composite parent, int style ) {
    super( parent, style );
    drawListeners = new ArrayList<ClientDrawListener>();
    cache = new DrawingsCache();
    RWT.getLifeCycle().addPhaseListener( this );
    RWT.getSessionStore().addSessionStoreListener( this );
    addDispatchPaintListener();
    setData( RWT.CUSTOM_VARIANT, CLIENT_CANVAS );
  }

  private void addDispatchPaintListener() {
    paintListener = new PaintListener() {
      public void paintControl( PaintEvent event ) {
        GC gc = event.gc;
        processClientDrawings( gc );
        gc.drawPoint( -1, -1 ); //TODO: This is a workaround to force updates, see RAP bug 377070
      }
    };
    addPaintListener( paintListener );
  }
  
  public void addClientDrawListener( ClientDrawListener listener ) {
    drawListeners.add( listener );
  }
  
  public void removeClientDrawListener( ClientDrawListener listener ) {
    drawListeners.remove( listener );
  }
  
  public void clear() {
    if( !isDisposed() ) {
      cache.clear();
      redraw();
      fireDrawEvent();
    }
  }
  
  public void undo() {
    if( !isDisposed() ) {
      if( cache.hasUndo() ) {
        cache.undo();
        redraw();
        fireDrawEvent();
      }
    }
  }
  
  public boolean hasUndo() {
    return cache.hasUndo();
  }
  
  public void redo() {
    if( !isDisposed() ) {
      if( cache.hasRedo() ) {
        cache.redo();
        redraw();
        fireDrawEvent();
      }
    }
  }
  
  public boolean hasRedo() {
    return cache.hasRedo();
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
      cache.clearRemoved();
      fireDrawEvent();
    }
    dispatchDrawings( gc );
  }

  private void fireDrawEvent() {
    if( !isDisposed() ) {
      for( ClientDrawListener listener : drawListeners ) {
        listener.receivedDrawing();
      }
    }
  }

  private void cacheDrawings( String drawings ) {
    cache.cache( drawings );
  }

  private void dispatchDrawings( GC gc ) {
    for( String drawing : cache.getCachedDrawings() ) {
      if( drawing != null ) {
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
  
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getAdapter( Class<T> adapter ) {
    T result = super.getAdapter( adapter );
    if( adapter == DrawingsCache.class ) {
      result = ( T )cache;
    }
    return result;
  }

}

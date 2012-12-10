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

import static org.eclipse.rap.rwt.internal.protocol.ProtocolUtil.readEventPropertyValueAsString;
import static org.eclipse.rap.rwt.internal.protocol.ProtocolUtil.wasEventSent;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.application.RWTFactory;
import org.eclipse.rap.rwt.internal.lifecycle.LifeCycleUtil;
import org.eclipse.rap.rwt.lifecycle.PhaseEvent;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.PhaseListener;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.service.UISessionEvent;
import org.eclipse.rap.rwt.service.UISessionListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.eclipsesource.tabris.internal.DrawingsCache;
import com.eclipsesource.tabris.internal.GCOperationDispatcher;


/**
 * <i>Provisional API, may change in future versions. Use it at your own risk.</i>
 * <p>
 * A <code>ClientCanvas</code> can be used like a SWT <code>Canvas</code> with the difference that a client can draw on 
 * it's area, too. Client side drawing will be sent back to the server and registered <code>ClientDrawListeners</code>
 * will be notified.</code>
 * </p>
 * 
 * @see ClientDrawListener
 * @since 0.6
 */
@SuppressWarnings("restriction")
public class ClientCanvas extends Canvas implements PhaseListener, UISessionListener {
  
  static final String DRAWING_EVENT = "Drawing";
  static final String DRAWINGS_PROPERTY = "drawings";
  static final String CLIENT_CANVAS = "CLIENT_CANVAS";
  
  private final List<ClientDrawListener> drawListeners;
  private final DrawingsCache cache;
  private PaintListener paintListener;

  public ClientCanvas( Composite parent, int style ) {
    super( parent, style );
    drawListeners = new ArrayList<ClientDrawListener>();
    cache = new DrawingsCache();
    RWT.getLifeCycle().addPhaseListener( this );
    RWT.getUISession().addUISessionListener( this );
    addDispatchPaintListener();
    setData( RWT.CUSTOM_VARIANT, CLIENT_CANVAS );
  }

  private void addDispatchPaintListener() {
    paintListener = new PaintListener() {
      @Override
      public void paintControl( PaintEvent event ) {
        GC gc = event.gc;
        processClientDrawings( gc );
        gc.drawPoint( -1, -1 ); //TODO: This is a workaround to force updates, see RAP bug 377070
      }
    };
    super.addPaintListener( paintListener );
  }
  
  /**
   * <p>
   * Adds a <code>ClientDrawListener</code> that gets called when a client draws.
   * </p>
   * 
   * @see ClientDrawListener
   */
  public void addClientDrawListener( ClientDrawListener listener ) {
    drawListeners.add( listener );
  }
  
  /**
   * <p>
   * Removes a <code>ClientDrawListener</code>.
   * </p>
   * 
   * @see ClientDrawListener
   */
  public void removeClientDrawListener( ClientDrawListener listener ) {
    drawListeners.remove( listener );
  }
  
  /**
   * <p>
   * Clears all client side drawings.
   * </p>
   */
  public void clear() {
    if( !isDisposed() ) {
      cache.clear();
      redraw();
      fireDrawEvent();
    }
  }
  
  /**
   * <p>
   * Undo the most recent client side drawing.
   * </p>
   */
  public void undo() {
    if( !isDisposed() ) {
      if( cache.hasUndo() ) {
        cache.undo();
        redraw();
        fireDrawEvent();
      }
    }
  }
  
  /**
   * <p>
   * returns if a undo can be performed.
   * </p>
   */
  public boolean hasUndo() {
    return cache.hasUndo();
  }
  
  /**
   * <p>
   * Redo the most recent undo.
   * </p>
   */
  public void redo() {
    if( !isDisposed() ) {
      if( cache.hasRedo() ) {
        cache.redo();
        redraw();
        fireDrawEvent();
      }
    }
  }
  
  /**
   * <p>
   * Returns if a redo can be performed.
   * </p>
   */
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
    String drawings = readEventPropertyValueAsString( WidgetUtil.getId( this ), DRAWING_EVENT, DRAWINGS_PROPERTY );
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

  @Override
  public void beforePhase( PhaseEvent event ) {
    Display sessionDisplay = LifeCycleUtil.getSessionDisplay();
    if( getDisplay() == sessionDisplay ) {
      if( wasEventSent( WidgetUtil.getId( this ), DRAWING_EVENT ) ) {
        redraw();
      }
    }
  }

  @Override
  public void afterPhase( PhaseEvent event ) {
    // do nothing
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.PROCESS_ACTION;
  }

  @Override
  public void beforeDestroy( UISessionEvent event ) {
    RWTFactory.getLifeCycleFactory().getLifeCycle().removePhaseListener( this );
    RWT.getUISession().removeUISessionListener( this );
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

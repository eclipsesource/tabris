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
package com.eclipsesource.tabris.internal;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.application.RWTFactory;
import org.eclipse.rap.rwt.internal.lifecycle.LifeCycleUtil;
import org.eclipse.rap.rwt.internal.protocol.ClientObjectFactory;
import org.eclipse.rap.rwt.internal.protocol.IClientObject;
import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rap.rwt.lifecycle.PhaseEvent;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.PhaseListener;
import org.eclipse.rap.rwt.service.IServiceStore;
import org.eclipse.rap.rwt.service.SessionStoreEvent;
import org.eclipse.rap.rwt.service.SessionStoreListener;
import org.eclipse.swt.widgets.Display;


@SuppressWarnings("restriction")
public abstract class AbstractObjectSynchronizer<T extends Adaptable> implements PhaseListener, SessionStoreListener {
  
  private final Display display;
  private final String id;
  private final T object;
  private boolean initialized;

  public AbstractObjectSynchronizer( T object ) {
    this.object = object;
    this.initialized = false;
    RWT.getLifeCycle().addPhaseListener( this );
    RWT.getSessionStore().addSessionStoreListener( this );
    display = Display.getCurrent();
    id = object.getAdapter( IClientObjectAdapter.class ).getId();
  }

  @Override
  public void beforePhase( PhaseEvent event ) {
    // do nothing
  }

  @Override
  public void afterPhase( PhaseEvent event ) {
    Display sessionDisplay = LifeCycleUtil.getSessionDisplay();
    if( getDisplay() == sessionDisplay ) {
      processLifeCycleMethods( event );
    }
  }

  private void processLifeCycleMethods( PhaseEvent event ) {
    PhaseId currentPhase = event.getPhaseId();
    if( currentPhase == PhaseId.PREPARE_UI_ROOT && !initialized ) {
      initialize();
    } else if( currentPhase == PhaseId.READ_DATA ) {
      readData( object );
      preserveValues( object );
    } else if( currentPhase == PhaseId.PROCESS_ACTION ) {
      processAction( object );
    } else if( currentPhase == PhaseId.RENDER ) {
      initialize();
      renderChanges( object );
    }
  }

  private void initialize() {
    if( !initialized ) {
      renderInitializationInternal();
      initialized = true;
    }
  }

  private void renderInitializationInternal() {
    renderInitialization( getClientObject(), object );
  }

  protected abstract void renderInitialization( IClientObject clientObject, T object );

  protected abstract void readData( T object );

  // TODO: move to Util
  public String readPropertyValue( String name ) {
    // TODO: WidgetUtil.readPropertyValue with id should be public
    HttpServletRequest request = RWT.getRequest();
    StringBuilder key = new StringBuilder();
    key.append( id );
    key.append( "." );
    key.append( name );
    return request.getParameter( key.toString() );
  }

  protected abstract void preserveValues( T object );

  // TODO move to Util
  protected void preserveProperty( String name, Object value ) {
    IServiceStore serviceStore = RWT.getServiceStore();
    serviceStore.setAttribute( id + "." + name, value );
  }
  
  // TODO move to Util
  protected void preserveProperty( String name, int value ) {
    preserveProperty( name, Integer.valueOf( value ) );
  }
  
  // TODO move to Util
  protected void preserveProperty( String name, boolean value ) {
    preserveProperty( name, Boolean.valueOf( value ) );
  }

  protected abstract void processAction( T object );

  protected abstract void renderChanges( T object );
  
  // TODO move to Util
  public boolean hasPropertyChanged( String name, Object newValue ) {
    IServiceStore serviceStore = RWT.getServiceStore();
    Object attribute = serviceStore.getAttribute( id + "." + name );
    boolean result = false;
    if( attribute != null ) {
      if( !attribute.equals( newValue ) ) {
        result = true;
      }
    }
    return result;
  }
  
  // TODO move to Util
  public void renderProperty( String name, Object newValue, Object defaultValue ) {
    IServiceStore serviceStore = RWT.getServiceStore();
    Object attribute = serviceStore.getAttribute( id + "." + name );
    if( attribute == null ) {
      getClientObject().set( name, newValue );
    } else if( !attribute.equals( newValue ) ) {
      if( defaultValue != null && !defaultValue.equals( attribute ) ) {
        getClientObject().set( name, newValue );
      }
    }
  }
  
  // TODO move to Util
  public void renderProperty( String name, int newValue, int defaultValue ) {
    renderProperty( name, Integer.valueOf( newValue ), Integer.valueOf( defaultValue ) );
  }
  
  // TODO move to Util
  public void renderProperty( String name, boolean newValue, boolean defaultValue ) {
    renderProperty( name, Boolean.valueOf( newValue ), Boolean.valueOf( defaultValue ) );
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.ANY;
  }

  @Override
  public void beforeDestroy( SessionStoreEvent event ) {
    RWTFactory.getLifeCycleFactory().getLifeCycle().removePhaseListener( this );
    RWT.getSessionStore().removeSessionStoreListener( this );
  }

  protected void destroy() {
    getClientObject().destroy();
  }
  
  protected String getId() {
    return id;
  }
  
  public IClientObject getClientObject() {
    return ClientObjectFactory.getClientObject( object );
  }

  public Display getDisplay() {
    return display;
  }
}

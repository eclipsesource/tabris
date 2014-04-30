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
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.internal.lifecycle.WidgetLifeCycleAdapter;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.internal.RefreshCompositeLCA;
import com.eclipsesource.tabris.internal.RefreshCompositeLCA.RefreshAdapter;


/**
 * @since 1.4
 */
@SuppressWarnings("restriction")
public class RefreshComposite extends Composite {

  private final List<RefreshListener> listeners;
  private final RefreshAdapter resetAdapter;
  private String message;

  public RefreshComposite( Composite parent, int style ) {
    super( parent, style );
    this.listeners = new ArrayList<RefreshListener>();
    this.resetAdapter = new RefreshAdapter();
  }

  public void setMessage( String message ) {
    whenNull( message ).throwIllegalArgument( "Message must not be null" );
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void addRefreshListener( RefreshListener listener ) {
    whenNull( listener ).throwIllegalArgument( "RefreshListener must not be null" );
    listeners.add( listener );
  }

  public void removeRefreshListener( RefreshListener listener ) {
    whenNull( listener ).throwIllegalArgument( "RefreshListener must not be null" );
    listeners.remove( listener );
  }

  public List<RefreshListener> getRefreshListeners() {
    return new ArrayList<RefreshListener>( listeners );
  }

  public void done() {
    resetAdapter.setDone( true );
  }

  @Override
  @SuppressWarnings({ "unchecked", "deprecation" })
  public <T> T getAdapter( Class<T> adapter ) {
    T result;
    if( adapter == WidgetLifeCycleAdapter.class || adapter == org.eclipse.rap.rwt.lifecycle.WidgetLifeCycleAdapter.class ) {
      result = ( T )new RefreshCompositeLCA();
    } else if( adapter == RefreshAdapter.class ) {
      result = ( T )resetAdapter;
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }

}

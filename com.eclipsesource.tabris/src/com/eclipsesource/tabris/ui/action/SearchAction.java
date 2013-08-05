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
package com.eclipsesource.tabris.ui.action;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.METHOD_OPEN;

import org.eclipse.rap.rwt.Adaptable;

import com.eclipsesource.tabris.internal.ui.RemoteObjectHolder;
import com.eclipsesource.tabris.ui.AbstractAction;

/**
 * @since 1.2
 */
public abstract class SearchAction extends AbstractAction implements Adaptable {

  private final RemoteObjectHolder remoteObjectHolder;

  public SearchAction() {
    remoteObjectHolder = new RemoteObjectHolder();
  }

  @Override
  public void execute() {
    // might be implemented by subclasses
  }

  public abstract void search( String query );

  public abstract void modified( String query, ProposalHandler proposalHandler );

  public final void open() {
    whenNull( remoteObjectHolder.getRemoteObject() ).throwIllegalState( "RemoteObject not set" );
    remoteObjectHolder.getRemoteObject().call( METHOD_OPEN, null );
  }

  public void setQuery( String query ) {
    whenNull( query ).throwIllegalArgument( "Query must not be null" );
    whenNull( remoteObjectHolder.getRemoteObject() ).throwIllegalState( "RemoteObject not set" );
    remoteObjectHolder.getRemoteObject().set( "query", query );
  }

  public void setMessage( String message ) {
    whenNull( message ).throwIllegalArgument( "Message must not be null" );
    whenNull( remoteObjectHolder.getRemoteObject() ).throwIllegalState( "RemoteObject not set" );
    remoteObjectHolder.getRemoteObject().set( "message", message );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    if( adapter == RemoteObjectHolder.class ) {
      return ( T )remoteObjectHolder;
    }
    return null;
  }

}
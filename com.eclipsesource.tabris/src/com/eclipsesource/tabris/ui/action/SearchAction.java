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

import org.eclipse.rap.rwt.Adaptable;

import com.eclipsesource.tabris.internal.ui.rendering.SearchActionRendererHolder;
import com.eclipsesource.tabris.ui.AbstractAction;

/**
 * @since 1.2
 */
public abstract class SearchAction extends AbstractAction implements Adaptable {

  private final SearchActionRendererHolder rendererHolder;

  public SearchAction() {
    rendererHolder = new SearchActionRendererHolder();
  }

  @Override
  public void execute() {
    // might be implemented by subclasses
  }

  public abstract void search( String query );

  public abstract void modified( String query, ProposalHandler proposalHandler );

  public final void open() {
    whenNull( rendererHolder.getSearchActionRenderer() ).throwIllegalState( "SearchActionRenderer not set" );
    execute( rendererHolder.getSearchActionRenderer().getUI() );
    rendererHolder.getSearchActionRenderer().open();
  }

  public void setQuery( String query ) {
    whenNull( query ).throwIllegalArgument( "Query must not be null" );
    whenNull( rendererHolder.getSearchActionRenderer() ).throwIllegalState( "SearchActionRenderer not set" );
    rendererHolder.getSearchActionRenderer().setQuery( query );
  }

  public void setMessage( String message ) {
    whenNull( message ).throwIllegalArgument( "Message must not be null" );
    whenNull( rendererHolder.getSearchActionRenderer() ).throwIllegalState( "SearchActionRenderer not set" );
    rendererHolder.getSearchActionRenderer().setMessage( message );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    if( adapter == SearchActionRendererHolder.class ) {
      return ( T )rendererHolder;
    }
    return null;
  }

}
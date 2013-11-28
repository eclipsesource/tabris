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
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_QUERY;

import com.eclipsesource.tabris.internal.ui.PropertyChangeNotifier;
import com.eclipsesource.tabris.ui.AbstractAction;

/**
 * @since 1.2
 */
public abstract class SearchAction extends AbstractAction {

  @Override
  public void execute() {
    // might be implemented by subclasses
  }

  public abstract void search( String query );

  public abstract void modified( String query, ProposalHandler proposalHandler );

  public final void open() {
    getAdapter( PropertyChangeNotifier.class ).firePropertyChange( METHOD_OPEN, null );
  }

  public final void setQuery( String query ) {
    whenNull( query ).throwIllegalArgument( "Query must not be null" );
    getAdapter( PropertyChangeNotifier.class ).firePropertyChange( PROPERTY_QUERY, query );
  }

  public final void setMessage( String message ) {
    whenNull( message ).throwIllegalArgument( "Message must not be null" );
    getAdapter( PropertyChangeNotifier.class ).firePropertyChange( PROPERTY_MESSAGE, message );
  }

}
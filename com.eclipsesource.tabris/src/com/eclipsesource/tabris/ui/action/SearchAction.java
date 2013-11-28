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
 * <p>
 * The {@link SearchAction} is a specialized action that can be used to execute a search task. Besides the actual
 * search, such a task contains the creation of search proposals during typing. A {@link SearchAction} provides methods
 * that will be called when a user types a search query and executes a search.
 * </p>
 * <p>
 * Please Note: A {@link SearchAction} will be rendered the platform typical way. You don't have to worry about placing
 * a search field and so on.
 * </p>
 *
 * @since 1.2
 */
public abstract class SearchAction extends AbstractAction {

  @Override
  public void execute() {
    // might be implemented by subclasses
  }

  /**
   * <p>
   * The {@link SearchAction#modified(String, ProposalHandler)} method will be called when a user types into a search
   * field aka. modifies the search query. The {@link ProposalHandler} can be used to propose search terms that will
   * be rendered on the accessing client.
   * </p>
   *
   * @param query the search query so far.
   * @param proposalHandler the handler to propose search terms.
   *
   * @see ProposalHandler
   */
  public abstract void modified( String query, ProposalHandler proposalHandler );

  /**
   * <p>
   * The {@link SearchAction#search(String)} method will be executed when the use executes a search e.g. by hitting
   * enter in a search field.
   * </p>
   *
   * @param query the query to search for
   */
  public abstract void search( String query );

  /**
   * <p>
   * Opens the search field on the client.
   * </p>
   */
  public final void open() {
    getAdapter( PropertyChangeNotifier.class ).firePropertyChange( METHOD_OPEN, null );
  }

  /**
   * <p>
   * Sets the query in the client's search field
   * </p>
   *
   * @param query the query to display in the client's search field. Must not be <code>null</code>.
   */
  public final void setQuery( String query ) {
    whenNull( query ).throwIllegalArgument( "Query must not be null" );
    getAdapter( PropertyChangeNotifier.class ).firePropertyChange( PROPERTY_QUERY, query );
  }

  /**
   * <p>
   * Sets the message to display in the client's search field as long as there is not query.
   * </p>
   *
   * @param message the message to set into the client's search field. Must not be <code>null</code>.
   */
  public final void setMessage( String message ) {
    whenNull( message ).throwIllegalArgument( "Message must not be null" );
    getAdapter( PropertyChangeNotifier.class ).firePropertyChange( PROPERTY_MESSAGE, message );
  }

}
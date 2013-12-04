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

import java.io.Serializable;


/**
 * <p>
 * A {@link Proposal} can be displayed on a client's proposal list during a search.
 * </p>
 *
 * @see ProposalHandler
 * @see SearchAction
 *
 * @since 1.2
 */
public class Proposal implements Serializable {

  private final String title;

  /**
   * <p>
   * Creates a new {@link Proposal} object with a given title.
   * </p>
   *
   * @param title the title to use. Must not be <code>null</code>.
   */
  public Proposal( String title ) {
    whenNull( title ).throwIllegalArgument( "title must not be null" );
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}

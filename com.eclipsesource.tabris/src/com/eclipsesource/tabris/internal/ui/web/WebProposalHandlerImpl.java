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
package com.eclipsesource.tabris.internal.ui.web;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.eclipsesource.tabris.ui.action.Proposal;
import com.eclipsesource.tabris.ui.action.ProposalHandler;


public class WebProposalHandlerImpl implements ProposalHandler {

  private final WebSearchAction actionRenderer;

  public WebProposalHandlerImpl( WebSearchAction actionRenderer ) {
    this.actionRenderer = actionRenderer;
  }

  @Override
  public void setProposals( List<Proposal> proposals ) {
    whenNull( proposals ).throwIllegalArgument( "Search Proposals must not be null" );
    List<String> titles = createTitleList( proposals );
    actionRenderer.showProposals( Collections.unmodifiableList( titles ) );
  }

  private List<String> createTitleList( List<Proposal> proposals ) {
    List<String> result = new ArrayList<String>();
    for( Proposal proposal : proposals ) {
      result.add( proposal.getTitle() );
    }
    return result;
  }

}

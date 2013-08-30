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

import java.util.Collections;
import java.util.List;

import com.eclipsesource.tabris.ui.action.ProposalHandler;


public class WebProposalHandlerImpl implements ProposalHandler {

  private WebSearchAction actionRenderer;

  public WebProposalHandlerImpl( WebSearchAction actionRenderer ) {
    this.actionRenderer = actionRenderer;
  }

  @Override
  public void setProposals( List<String> proposals ) {
    whenNull( proposals ).throwIllegalArgument( "Search Proposals must not be null" );
    actionRenderer.showProposals( Collections.unmodifiableList( proposals ) );
  }

}

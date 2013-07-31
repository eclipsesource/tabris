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
package com.eclipsesource.tabris.internal.ui;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PROPOSALS;
import static com.eclipsesource.tabris.internal.JsonUtil.createJsonArray;

import java.util.List;

import org.eclipse.rap.rwt.remote.RemoteObject;

import com.eclipsesource.tabris.ui.action.ProposalHandler;


public class ProposalHandlerImpl implements ProposalHandler {

  private final RemoteObject remoteObject;

  public ProposalHandlerImpl( RemoteObject remoteObject ) {
    this.remoteObject = remoteObject;
  }

  @Override
  public void setProposals( List<String> proposals ) {
    whenNull( proposals ).throwIllegalArgument( "Search Proposals must not be null" );
    String[] proposalsToSend;
    if( proposals != null ) {
      proposalsToSend = new String[ proposals.size() ];
      proposals.toArray( proposalsToSend );
    } else {
      proposalsToSend = new String[ 0 ];
    }
    remoteObject.set( PROPERTY_PROPOSALS, createJsonArray( proposalsToSend ) );
  }

}

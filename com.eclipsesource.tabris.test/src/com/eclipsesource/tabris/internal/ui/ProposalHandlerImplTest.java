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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.junit.Test;


public class ProposalHandlerImplTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullProposals() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    ProposalHandlerImpl proposalHandler = new ProposalHandlerImpl( remoteObject );

    proposalHandler.setProposals( null );
  }

  @Test
  public void testSetsProposalsAsProperty() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    ProposalHandlerImpl proposalHandler = new ProposalHandlerImpl( remoteObject );
    ArrayList<String> proposals = new ArrayList<String>();
    proposals.add( "foo" );
    proposals.add( "bar" );

    proposalHandler.setProposals( proposals );

    JsonArray jsonArray = new JsonArray();
    jsonArray.add( "foo" );
    jsonArray.add( "bar" );
    verify( remoteObject ).set( "proposals", jsonArray );
  }

  @Test
  public void testSetsEmptyProposalsAsProperty() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    ProposalHandlerImpl proposalHandler = new ProposalHandlerImpl( remoteObject );

    proposalHandler.setProposals( new ArrayList<String>() );

    verify( remoteObject ).set( "proposals", new JsonArray() );
  }
}

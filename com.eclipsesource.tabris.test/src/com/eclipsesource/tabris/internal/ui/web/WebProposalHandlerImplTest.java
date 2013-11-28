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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.ui.action.Proposal;


public class WebProposalHandlerImplTest {

  private WebSearchAction actionRenderer;
  private WebProposalHandlerImpl proposalHandler;

  @Before
  public void setUp() {
    actionRenderer = mock( WebSearchAction.class );
    proposalHandler = new WebProposalHandlerImpl( actionRenderer );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetProposals_null() {
    proposalHandler.setProposals( null );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testSetProposals_empty() {
    List<Proposal> proposals = new ArrayList<Proposal>();

    proposalHandler.setProposals( proposals );

    ArgumentCaptor<List> captor = ArgumentCaptor.forClass( List.class );
    verify( actionRenderer ).showProposals( captor.capture() );
    assertTrue( captor.getValue().isEmpty() );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testSetProposals() {
    List<Proposal> proposals = new ArrayList<Proposal>();
    proposals.add( new Proposal( "foo" ) );
    proposals.add( new Proposal( "bar" ) );

    proposalHandler.setProposals( proposals );

    ArgumentCaptor<List> captor = ArgumentCaptor.forClass( List.class );
    verify( actionRenderer ).showProposals( captor.capture() );
    List value = captor.getValue();
    assertEquals( "foo", value.get( 0 ) );
    assertEquals( "bar", value.get( 1 ) );
  }

}

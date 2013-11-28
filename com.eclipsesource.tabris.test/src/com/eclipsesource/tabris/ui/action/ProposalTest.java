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

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ProposalTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTitle() {
    new Proposal( null );
  }

  @Test
  public void testHasTitle() {
    Proposal proposal = new Proposal( "foo" );

    String title = proposal.getTitle();

    assertEquals( "foo", title );
  }
}

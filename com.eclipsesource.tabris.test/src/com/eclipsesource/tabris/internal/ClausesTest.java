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
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ClausesTest {

  @Test( expected = IllegalStateException.class )
  public void testThrowsISEForTrueCondition() {
    Clauses.when( true ).throwIllegalState( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testThrowsIAEForTrueCondition() {
    Clauses.when( true ).throwIllegalArgument( "" );
  }

  @Test( expected = IllegalStateException.class )
  public void testThrowsISEForInvertedTrueCondition() {
    Clauses.whenNot( false ).throwIllegalState( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testThrowsIAEForInvertedTrueCondition() {
    Clauses.whenNot( false ).throwIllegalArgument( "" );
  }

  @Test( expected = IllegalStateException.class )
  public void testThrowsISEForNullCondition() {
    Clauses.whenNull( null ).throwIllegalState( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testThrowsIAEForNullCondition() {
    Clauses.whenNull( null ).throwIllegalArgument( "" );
  }

  @Test
  public void testThrowsISEWithDelegatedMessage() {
    try {
      Clauses.when( true ).throwIllegalState( "foo" );
    } catch( IllegalStateException ise ) {
      assertEquals( "foo", ise.getMessage() );
    }
  }

  @Test( expected = IllegalArgumentException.class )
  public void testThrowsIAEWithDelegatedMessage() {
    try {
      Clauses.when( true ).throwIllegalArgument( "foo" );
    } catch( IllegalStateException ise ) {
      assertEquals( "foo", ise.getMessage() );
    }
  }
}

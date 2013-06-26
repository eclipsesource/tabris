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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNot;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ClausesTest {

  @Test( expected = IllegalStateException.class )
  public void testThrowsISEForTrueCondition() {
    when( true ).throwIllegalState( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testThrowsIAEForTrueCondition() {
    when( true ).throwIllegalArgument( "" );
  }

  @Test( expected = IllegalStateException.class )
  public void testThrowsISEForInvertedTrueCondition() {
    whenNot( false ).throwIllegalState( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testThrowsIAEForInvertedTrueCondition() {
    whenNot( false ).throwIllegalArgument( "" );
  }

  @Test( expected = IllegalStateException.class )
  public void testThrowsISEForNullCondition() {
    whenNull( null ).throwIllegalState( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testThrowsIAEForNullCondition() {
    whenNull( null ).throwIllegalArgument( "" );
  }

  @Test
  public void testThrowsISEWithDelegatedMessage() {
    try {
      when( true ).throwIllegalState( "foo" );
    } catch( IllegalStateException ise ) {
      assertEquals( "foo", ise.getMessage() );
    }
  }

  @Test( expected = IllegalArgumentException.class )
  public void testThrowsIAEWithDelegatedMessage() {
    try {
      when( true ).throwIllegalArgument( "foo" );
    } catch( IllegalStateException ise ) {
      assertEquals( "foo", ise.getMessage() );
    }
  }
}

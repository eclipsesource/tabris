/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import org.junit.Test;


public class PreconditionsTest {

  @Test( expected = IllegalArgumentException.class )
  public void testNotNullFailsWithNullArgument() {
    Preconditions.checkArgumentNotNull( null, "" );
  }

  @Test( expected = IllegalStateException.class )
  public void testNotNullFailsWithNullState() {
    Preconditions.checkState( null, "" );
  }

  @Test
  public void testNotNullDoesNotFailsWithNonNullArgument() {
    Preconditions.checkArgumentNotNull( "", "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testNotEmptyFailsWithNullArgument() {
    Preconditions.checkArgumentNotNullAndNotEmpty( null, "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testNotEmptyFailsWithEmpty() {
    Preconditions.checkArgumentNotNullAndNotEmpty( "", "" );
  }

  @Test( expected = IllegalStateException.class )
  public void testCheckStateThrowsException() {
    Preconditions.checkState( null, "message" );
  }
}

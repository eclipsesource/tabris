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


public class Clauses {

  public static class Clause {

    private final boolean condition;

    public Clause( boolean condition ) {
      this.condition = condition;
    }

    public void throwIllegalState( String message ) {
      if( condition ) {
        throw new IllegalStateException( message );
      }
    }

    public void throwIllegalArgument( String message ) {
      if( condition ) {
        throw new IllegalArgumentException( message );
      }
    }

  }

  private static Clause TRUE_CLAUSE = new Clause( true );
  private static Clause FALSE_CLAUSE = new Clause( false );

  public static Clause when( boolean condition ) {
    return condition ? TRUE_CLAUSE : FALSE_CLAUSE;
  }

  public static Clause whenNot( boolean condition ) {
    return when( !condition );
  }

  public static Clause whenNull( Object object ) {
    return when( object == null );
  }

  private Clauses() {
    // prevent instantiation
  }
}

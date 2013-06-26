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

  public static class ExceptionConclusion {

    private final boolean condition;

    public ExceptionConclusion( boolean condition ) {
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

  public static ExceptionConclusion when( boolean condition ) {
    return new ExceptionConclusion( condition );
  }

  public static ExceptionConclusion whenNot( boolean condition ) {
    return when( !condition );
  }

  public static ExceptionConclusion whenNull( Object object ) {
    return when( object == null );
  }

  private Clauses() {
    // prevent instantiation
  }
}

/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;


public class Preconditions {

  public static void checkArgument( boolean expression, String message ) throws IllegalArgumentException {
    if( !expression ) {
      throw new IllegalArgumentException( message );
    }
  }

  public static void checkArgumentNotNull( Object object, String name ) throws IllegalArgumentException {
    if( object == null ) {
      throw new IllegalArgumentException( name + " must not be null" );
    }
  }

  public static void checkArgumentNotNullAndNotEmpty( Object object, String name ) throws IllegalArgumentException {
    if( object == null ) {
      throw new IllegalArgumentException( name + " must not be null" );
    }
    if( object instanceof String && ( ( String )object ).isEmpty() ) {
      throw new IllegalArgumentException( name + " must not be empty" );
    }
  }

  public static void checkState( Object object, String message ) throws IllegalStateException {
    if( object == null ) {
      throw new IllegalStateException( message );
    }
  }

  private Preconditions() {
    // prevent instantiation
  }
}

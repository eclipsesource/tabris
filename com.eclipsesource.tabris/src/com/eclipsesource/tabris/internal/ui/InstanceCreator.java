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


public class InstanceCreator {

  public static <T> T createInstance( Class<T> type ) {
    return createInstanceSafely( type );
  }

  private static <T> T createInstanceSafely( Class<T> type ) {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      setContextClassloader( type.getClassLoader() );
      return type.newInstance();
    } catch( Exception exception ) {
      throw new IllegalStateException( "Could no create instance of " + type.getName(), exception );
    } finally {
      setContextClassloader( original );
    }
  }

  private static void setContextClassloader( ClassLoader original ) {
    Thread.currentThread().setContextClassLoader( original );
  }

  private InstanceCreator() {
    // prevent instantiation
  }

}

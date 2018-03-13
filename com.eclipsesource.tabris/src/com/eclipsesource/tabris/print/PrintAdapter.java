/*******************************************************************************
 * Copyright (c) 2014, 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.print;



/**
 * <p>
 * This adapter provides an empty default implementation of {@link PrintListener}.
 * </p>
 *
 * @see PrintListener
 *
 * @since 1.4
 */
public class PrintAdapter implements PrintListener {

  @Override
  public void printSucceeded( String jobName ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void printCanceled( String jobName ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void printFailed( PrintError error ) {
    // intended to be implemented by subclasses.
  }

}

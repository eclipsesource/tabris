/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.ui;


/**
 * <p>
 * This adapter provides an empty default implementation of {@link TransitionListener}.
 * </p>
 *
 * @see TransitionListener
 * @since 1.3
 */
public class TransistionAdapter implements TransitionListener {

  @Override
  public void before( UI ui, Page from, Page to ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void after( UI ui, Page from, Page to ) {
    // intended to be implemented by subclasses.
  }
}

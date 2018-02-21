/*******************************************************************************
 * Copyright (c) 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

/**
 * <p>
 * This adapter provides an empty default implementation of {@link ScanListener}.
 * </p>
 *
 * @see ScanListener
 *
 * @since 3.4
 */
public class ScanAdapter implements ScanListener {

  @Override
  public void scanSucceeded( String format, String data ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void scanFailed( String error ) {
    // intended to be implemented by subclasses.
  }

}

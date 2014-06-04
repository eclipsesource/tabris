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
package com.eclipsesource.tabris.widgets;


/**
 * <p>
 * This adapter provides an empty default implementation of {@link ClientDialogListener}.
 * </p>
 *
 * @see ClientDialogListener
 *
 * @since 1.4
 */
public class ClientDialogAdapter implements ClientDialogListener {

  @Override
  public void open() {
    // intended to be implemented by subclasses.
  }

  @Override
  public void close() {
    // intended to be implemented by subclasses.
  }

}

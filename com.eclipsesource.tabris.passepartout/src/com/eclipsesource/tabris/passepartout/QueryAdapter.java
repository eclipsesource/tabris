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
package com.eclipsesource.tabris.passepartout;


/**
 * <p>
 * This adapter provides an empty default implementation of {@link QueryListener}.
 * </p>
 *
 * @see QueryListener
 *
 * @since 0.9
 */
public class QueryAdapter implements QueryListener {

  @Override
  public void activated( Query query ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void deactivated( Query query ) {
    // intended to be implemented by subclasses.
  }

}

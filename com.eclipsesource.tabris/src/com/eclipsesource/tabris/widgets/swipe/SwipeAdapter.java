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
package com.eclipsesource.tabris.widgets.swipe;


/**
 * <p>
 * An empty implementation for {@link SwipeListener} to be able to just override separate methods.
 * </p>
 *
 * @see SwipeListener
 *
 * @since 0.10
 */
public class SwipeAdapter implements SwipeListener {

  @Override
  public void itemLoaded( SwipeItem item, int index ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void itemActivated( SwipeItem item, int index, SwipeContext context ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void itemDeactivated( SwipeItem item, int index, SwipeContext context ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void disposed( SwipeContext context ) {
    // intended to be implemented by subclasses.
  }
}

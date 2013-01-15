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
 * A <code>SwipeListener</code> acts as a server side callback mechanism of the client side {@link Swipe}
 * representation. This means it's methods will be called when the user swipes from item to item. A
 * <code>SwipeListener</code> can only be registered directly on the {@link Swipe} object.
 * </p>
 *
 * @see Swipe
 * @see SwipeItem
 *
 * @since 0.10
 */
public interface SwipeListener {

  /**
   * <p>
   * Will be called when an item was loaded.
   * </p>
   */
  void itemLoaded( SwipeItem item, int index );

  /**
   * <p>
   * Will be called when an item was activated.
   * </p>
   */
  void itemActivated( SwipeItem item, int index, SwipeContext context );

  /**
   * <p>
   * Will be called when an item was deactivated.
   * </p>
   */
  void itemDeactivated( SwipeItem item, int index, SwipeContext context );

  /**
   * <p>
   * Will be called when the {@link Swipe} was disposed.
   * </p>
   */
  void disposed( SwipeContext context );

}

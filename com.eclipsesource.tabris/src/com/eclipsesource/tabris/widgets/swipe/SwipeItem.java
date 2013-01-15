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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * <p>
 * A <code>SwipeItem</code> is the data unit of a {@link Swipe} object. It defines it's content. The item will be
 * notified when a item should be loaded and when it's activated or deactivated.
 * </p>
 *
 * @see Swipe
 * @see SwipeContext
 *
 * @since 0.10
 */
public interface SwipeItem {

  /**
   * <p>
   * Will be called by a {@link Swipe} object. Should tell the <code>Swipe</code> if or if not the item can be pre
   * loaded. Per loading means the load mehtod will be called but the item is not active. E.g. when showing item 0,
   * item 1 will be pre loaded.
   * </p>
   */
  boolean isPreloadable();

  /**
   * <p>
   * Should create the actual {@link Control} of an item. If an item is pre loadable it will be called before the item
   * will be activated. If not the it will be called shortly before the activate method will be called.
   * </p>
   *
   * @param parent the {@link Composite} that should be used as a parent. Has always the same bounds as
   *               the {@link Swipe}.
   */
  Control load( Composite parent );

  /**
   * <p>
   * Will be called when a the item will be activated. This means when a user swipes to this item.
   * </p>
   *
   * @param context the shared context during all swiping events.
   */
  void activate( SwipeContext context );

  /**
   * <p>
   * Will be called when a the item will be deactivated. This means when a user swipes to another item.
   * </p>
   *
   * @param context the shared context during all swiping events.
   */
  void deactivate( SwipeContext context );

}

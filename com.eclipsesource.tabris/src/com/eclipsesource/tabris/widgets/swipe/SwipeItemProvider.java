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

import java.io.Serializable;


/**
 * <p>
 * A <code>SwipeItemProvider</code> acts a the data source for a {@link Swipe} object. It's responsible for creating
 * the {@link SwipeItem}s for the <code>Swipe</code> object. It's comparable with a JFace Viewer.
 * </p>
 *
 * @see Swipe
 * @see SwipeItem
 *
 * @since 0.10
 */
public interface SwipeItemProvider extends Serializable {

  /**
   * <p>
   * Should return the item for the given index.
   * </p>
   */
  SwipeItem getItem( int index );

  /**
   * <p>
   * Should return the amount of items of the {@link Swipe} object. Can change during runtime to grow/shrink
   * dynamically.
   * </p>
   */
  int getItemCount();

}

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
package com.eclipsesource.tabris.ui;


/**
 * <p>
 * For a Tabris UI one {@link UI} object exists. This object will be passed in you {@link UIConfiguration}
 * implementation to add pages and actions. Think about it as the glue between you pages and actions.
 * </p>
 *
 * @see PageConfiguration
 * @see Page
 * @see ActionConfiguration
 * @see Action
 * @see TransitionListener
 *
 * @since 0.11
 */
public interface UI {

  /**
   * <p>
   * Adds a page represented by a {@link PageConfiguration} object.
   * </p>
   */
  UI addPage( PageConfiguration configuration );

  /**
   * <p>
   * Adds an action represented by an {@link ActionConfiguration} object.
   * </p>
   */
  UI addAction( ActionConfiguration configuration );

  /**
   * <p>
   * Adds a {@link TransitionListener} that notifies you when a user browses from one page to another.
   * </p>
   */
  UI addTransitionListener( TransitionListener listener );

  /**
   * <p>
   * Removes a {@link TransitionListener}.
   * </p>
   */
  UI removeTransitionListener( TransitionListener listener );

}

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
 * A {@link TransitionListener} can be used to get notified when the user navigates from one {@link Page} to another.
 * {@link TransitionListener} object are usually registered within the {@link UIConfiguration#configure(UI, UIContext)}
 * method.
 * </p>
 *
 * @see Page
 * @see UIConfiguration
 *
 * @since 0.11
 */
public interface TransitionListener {

  /**
   * <p>
   * Will be called before the new {@link Page} will be activated.
   * </p>
   */
  void before( UIContext context, Page from, Page to );

  /**
   * <p>
   * Will be called after the new {@link Page} will was activated.
   * </p>
   */
  void after( UIContext context, Page from, Page to );

}

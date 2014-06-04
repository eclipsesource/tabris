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

import java.io.Serializable;

import com.eclipsesource.tabris.widgets.enhancement.RefreshHandler;


/**
 * <p>
 * A {@link RefreshListener} can be added to a {@link RefreshComposite} or a {@link RefreshHandler}. Implementations
 * need to do the refresh work within the {@link RefreshListener#refresh()} method.
 * </p>
 * <p>
 * <b>Please note:</b> The refresh method will be called within the UIThread. This means you should not execute long
 * running operations directly. Use a {@link Thread} for it and call the responsible <code>done</code> method after the
 * refreshing was finished.
 * </p>
 *
 * @see RefreshComposite
 * @see RefreshHandler
 *
 * @since 1.4
 */
public interface RefreshListener extends Serializable {

  /**
   * <p>
   * Will be called when a user triggers a refresh. This method will be called within the UIThread.
   * </p>
   */
  void refresh();
}

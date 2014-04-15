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
package com.eclipsesource.tabris.ui.action;

import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.ActionListener;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.UIConfiguration;


/**
 * <p>
 * A {@link SearchActionListener} is a specialized {@link ActionListener} that can be used to get notified when the
 * user has modified the search query or executed a search. {@link SearchActionListener} can be added as usual
 * {@link ActionListener} to a {@link UIConfiguration}.
 * </p>
 *
 * @see UIConfiguration
 * @see SearchAction
 *
 * @since 1.4
 */
public interface SearchActionListener extends ActionListener {

  /**
   * <p>
   * Will be called after a search was executed.
   * </p>
   *
   * @param ui the {@link UI} the action lives in.
   * @param action the action that was executed.
   * @param query the query used for the search.
   */
  void searched( UI ui, Action action, String query );

  /**
   * <p>
   * Will be called after the search query was modified.
   * </p>
   *
   * @param ui the {@link UI} the action lives in.
   * @param action the action that was executed.
   * @param query the query that will be used for search.
   */
  void modified( UI ui, Action action, String query );
}

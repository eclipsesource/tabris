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
 * An {@link AbstractAction} is a convenience class to make the implementation of actions easier. It provides service
 * methods to control the application flow or manipulate the action state.
 * </p>
 *
 * @see Action
 *
 * @since 1.0
 */
public abstract class AbstractAction implements Action {

  private UI ui;

  @Override
  public final void execute( UI ui ) {
    this.ui = ui;
    execute();
  }

  /**
   * <p>
   * Will be called by the Tabris UI when a user presses the visual representation of this action.
   * </p>
   *
   * @since 1.0
   */
  public abstract void execute();

  /**
   * <p>
   * Returns the UI instance this action lives in.
   * </p>
   */
  public UI getUI() {
    return ui;
  }

  /**
   * <p>
   * Returns the current visible page.
   * </p>
   */
  public Page getCurrentPage() {
    return ui.getPageOperator().getCurrentPage();
  }

  /**
   * <p>
   * Returns the {@link PageData} of the current visible page.
   * </p>
   */
  public PageData getCurrentPageData() {
    return ui.getPageOperator().getCurrentPageData();
  }

  /**
   * <p>
   * Sets the title of the current visible page.
   * </p>
   */
  public void setPageTitle( String title ) {
    ui.getPageOperator().setCurrentPageTitle( title );
  }

  /**
   * <p>
   * Opens a new page with the defined id.
   * <p>
   *
   * @param pageId the id of the page to open. Must not be <code>null</code> or empty.
   *
   * @throws IllegalStateException when no page exist for the given id.
   */
  public void openPage( String pageId ) throws IllegalStateException {
    ui.getPageOperator().openPage( pageId );
  }

  /**
   * <p>
   * Opens a new page with the defined id and passes {@link PageData} to the new page.
   * <p>
   *
   * @param pageId the id of the page to open. Must not be <code>null</code> or empty.
   * @param data the data for the new page. Can be accessed using {@link AbstractPage#getData()} on the new page.
   *
   * @throws IllegalStateException when no page exist for the given id.
   */
  public void openPage( String pageId, PageData data ) throws IllegalStateException {
    ui.getPageOperator().openPage( pageId, data );
  }

  /**
   * <p>
   * Closes the current visible page.
   * </p>
   *
   * @throws IllegalStateException when it's called on a top level page.
   */
  public void closeCurrentPage() throws IllegalStateException {
    ui.getPageOperator().closeCurrentPage();
  }

  /**
   * <p>
   * Manipulates the visibility of an {@link Action}.
   * </p>
   *
   * @param actionId the id of the action to manipulate. Must not be <code>null</code> or empty.
   *
   * @throws IllegalStateException when no {@link Action} for the given id exist.
   */
  public void setActionVisible( String actionId, boolean visible ) throws IllegalStateException {
    ui.getActionOperator().setActionVisible( actionId, visible );
  }

  /**
   * <p>
   * Manipulates the enable state of an {@link Action}.
   * </p>
   *
   * @param actionId the id of the action to manipulate. Must not be <code>null</code> or empty.
   *
   * @throws IllegalStateException when no {@link Action} for the given id exist.
   */
  public void setActionEnabled( String actionId, boolean enabled ) throws IllegalStateException {
    ui.getActionOperator().setActionEnabled( "foo", enabled );
  }

  /**
   * <p>
   * Returns the {@link UIConfiguration} used to configure the current UI. The configuration returned has
   * session scope and can be used to manipulate the UI during runtime e.g. adding {@link PageConfiguration}s
   * and {@link ActionConfiguration}s.
   * </p>
   *
   * @since 1.2
   */
  public UIConfiguration getUIConfiguration() {
    return ui.getConfiguration();
  }
}

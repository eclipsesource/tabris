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

import org.eclipse.swt.widgets.Composite;


/**
 * <p>
 * An {@link AbstractPage} is a convenience class to make the implementation of pages easier. It provides service
 * methods to control the application flow or manipulate actions.
 * </p>
 *
 * @see Page
 *
 * @since 1.0
 */
public abstract class AbstractPage implements Page {

  private UI ui;

  @Override
  public final void createContent( Composite parent, UI ui ) {
    this.ui = ui;
    createContent( parent, ui.getPageOperator().getCurrentPageData() );
  }

  /**
   * <p>
   * Will be called by the Tabris UI during the instantiation of a {@link Page}. Within this method the UI code should
   * be created.
   * </p>
   *
   * @param parent the parent to use for creating SWT widgets.
   * @param data the {@link PageData} object for the newly created page.
   */
  public abstract void createContent( Composite parent, PageData data );

  @Override
  public void activate() {
    // to be implemented by sub classes.
  }

  @Override
  public void deactivate() {
    // to be implemented by sub classes.
  }

  /**
   * <p>
   * Returns the UI instance this page lives in.
   * </p>
   */
  public UI getUI() {
    return ui;
  }

  /**
   * <p>
   * Returns the {@link PageData} for the current page.
   * </p>
   */
  public PageData getData() {
    return ui.getPageOperator().getCurrentPageData();
  }

  /**
   * <p>
   * Sets the title of the current page.
   * </p>
   */
  public void setTitle( String title ) {
    ui.getPageOperator().setCurrentPageTitle( title );
  }

  /**
   * <p>
   * Closes the current page.
   * </p>
   *
   * @throws IllegalStateException when it's called on a top level page.
   */
  public void close() throws IllegalStateException {
    ui.getPageOperator().closeCurrentPage();
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
    ui.getActionOperator().setActionEnabled( actionId, enabled );
  }
}

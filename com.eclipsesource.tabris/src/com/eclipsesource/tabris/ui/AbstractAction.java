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
 * @since 1.0
 */
public abstract class AbstractAction implements Action {

  private UI ui;

  @Override
  public final void execute( UI ui ) {
    this.ui = ui;
    execute();
  }

  public abstract void execute();

  public UI getUI() {
    return ui;
  }

  public Page getCurrentPage() {
    return ui.getPageOperator().getCurrentPage();
  }

  public PageData getCurrentPageData() {
    return ui.getPageOperator().getCurrentPageData();
  }

  public void setPageTitle( String title ) {
    ui.getPageOperator().setCurrentPageTitle( title );
  }

  public void openPage( String pageId ) {
    ui.getPageOperator().openPage( pageId );
  }

  public void openPage( String pageId, PageData data ) {
    ui.getPageOperator().openPage( pageId, data );
  }

  public void closeCurrentPage() {
    ui.getPageOperator().closeCurrentPage();
  }

  public void setActionVisible( String actionId, boolean visible ) {
    ui.getActionOperator().setActionVisible( actionId, visible );
  }

  public void setActionEnabled( String actionId, boolean enabled ) {
    ui.getActionOperator().setActionEnabled( "foo", enabled );
  }
}

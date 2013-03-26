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
 * @since 1.0
 */
public abstract class AbstractPage implements Page {

  private UI ui;

  @Override
  public void createContent( Composite parent, UI ui ) {
    this.ui = ui;
    createContent( parent, ui.getPageOperator().getCurrentPageData() );
  }

  public abstract void createContent( Composite parent, PageData data );

  @Override
  public void activate() {
    // to be implemented by sub classes.
  }

  @Override
  public void deactivate() {
    // to be implemented by sub classes.
  }

  public UI getUI() {
    return ui;
  }

  public PageData getData() {
    return ui.getPageOperator().getCurrentPageData();
  }

  public void setTitle( String title ) {
    ui.getPageOperator().setCurrentPageTitle( this, title );
  }

  public void close() {
    ui.getPageOperator().closeCurrentPage();
  }

  public void openPage( String pageId ) {
    ui.getPageOperator().openPage( pageId );
  }

  public void openPage( String pageId, PageData data ) {
    ui.getPageOperator().openPage( pageId, data );
  }

  public void setActionVisible( String actionId, boolean visible ) {
    ui.getActionOperator().setActionVisible( actionId, visible );
  }

  public void setActionEnabled( String actionId, boolean enabled ) {
    ui.getActionOperator().setActionEnabled( "foo", enabled );
  }
}

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
package com.eclipsesource.tabris.internal.ui;

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;
import static com.eclipsesource.tabris.internal.Preconditions.checkState;

import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageOperator;
import com.eclipsesource.tabris.ui.PageStore;


public class PageOperatorImpl implements PageOperator {

  private final Controller controller;
  private final UIImpl ui;

  public PageOperatorImpl( Controller controller, UIImpl ui ) {
    checkArgumentNotNull( controller, Controller.class.getSimpleName() );
    checkArgumentNotNull( ui, UIImpl.class.getSimpleName() );
    this.controller = controller;
    this.ui = ui;
  }

  @Override
  public void openPage( String pageId ) throws IllegalStateException {
    openPage( pageId, new PageStore() );
  }

  @Override
  public void openPage( String pageId, PageStore store ) throws IllegalStateException {
    checkArgumentNotNull( store, PageStore.class.getSimpleName() );
    UIDescriptor uiDescriptor = ui.getConfiguration().getAdapter( UIDescriptor.class );
    PageDescriptor descriptor = uiDescriptor.getPageDescriptor( pageId );
    checkState( descriptor, "Page with id " + pageId + " does not exist." );
    controller.show( ui, descriptor, store );
  }

  @Override
  public void closeCurrentPage() throws IllegalStateException{
    boolean wasClosed = controller.closeCurrentPage( ui );
    if( !wasClosed ) {
      throw new IllegalStateException( "Can not close top level page." );
    }
  }

  @Override
  public Page getCurrentPage() {
    return controller.getCurrentPage();
  }

  @Override
  public PageStore getCurrentPageStore() {
    return controller.getCurrentStore();
  }

  @Override
  public void setCurrentPageTitle( Page page, String title ) {
    checkArgumentNotNull( page, Page.class.getSimpleName() );
    checkArgumentNotNull( title, "PageTitle" );
    controller.setTitle( page, title );
  }
}

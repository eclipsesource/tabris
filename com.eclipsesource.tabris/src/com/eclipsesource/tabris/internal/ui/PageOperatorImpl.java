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

import java.io.Serializable;

import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.PageOperator;


public class PageOperatorImpl implements PageOperator, Serializable {

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
    openPage( pageId, new PageData() );
  }

  @Override
  public void openPage( String pageId, PageData data ) throws IllegalStateException {
    checkArgumentNotNull( data, PageData.class.getSimpleName() );
    UIDescriptor uiDescriptor = ui.getConfiguration().getAdapter( UIDescriptor.class );
    PageDescriptor descriptor = uiDescriptor.getPageDescriptor( pageId );
    checkState( descriptor, "Page with id " + pageId + " does not exist." );
    controller.show( ui, descriptor, data );
  }

  @Override
  public void closeCurrentPage() throws IllegalStateException{
    boolean wasClosed = controller.closeCurrentPage( ui );
    checkState( wasClosed, "Can not close top level page." );
  }

  @Override
  public Page getCurrentPage() {
    return controller.getCurrentPage();
  }

  @Override
  public PageData getCurrentPageData() {
    return controller.getCurrentData();
  }

  @Override
  public void setCurrentPageTitle( String title ) {
    checkArgumentNotNull( title, "PageTitle" );
    controller.setTitle( getCurrentPage(), title );
  }
}

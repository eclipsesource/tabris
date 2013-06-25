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

import static com.eclipsesource.tabris.internal.Clauses.whenNot;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;

import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.PageOperator;


public class PageOperatorImpl implements PageOperator, Serializable {

  private final Controller controller;
  private final UIImpl ui;

  public PageOperatorImpl( Controller controller, UIImpl ui ) {
    whenNull( controller ).thenIllegalArgument( "Controller must not be null" );
    whenNull( ui ).thenIllegalArgument( "UI must not be null" );
    this.controller = controller;
    this.ui = ui;
  }

  @Override
  public void openPage( String pageId ) throws IllegalStateException {
    openPage( pageId, new PageData() );
  }

  @Override
  public void openPage( String pageId, PageData data ) throws IllegalStateException {
    whenNull( data ).thenIllegalArgument( "PageData must not be null" );
    UIDescriptor uiDescriptor = ui.getConfiguration().getAdapter( UIDescriptor.class );
    PageDescriptor descriptor = uiDescriptor.getPageDescriptor( pageId );
    whenNull( descriptor ).thenIllegalState( "Page with id " + pageId + " does not exist." );
    controller.show( ui, descriptor, data );
  }

  @Override
  public void closeCurrentPage() throws IllegalStateException{
    boolean wasClosed = controller.closeCurrentPage( ui );
    whenNot( wasClosed ).thenIllegalState( "Can not close top level page." );
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
    whenNull( title ).thenIllegalArgument( "Page Title must not be null" );
    controller.setTitle( getCurrentPage(), title );
  }
}

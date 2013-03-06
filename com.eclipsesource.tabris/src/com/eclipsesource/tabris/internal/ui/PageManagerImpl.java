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

import com.eclipsesource.tabris.Store;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageManager;


public class PageManagerImpl implements PageManager {

  private final Controller controller;
  private final UIContextImpl context;

  public PageManagerImpl( Controller controller, UIContextImpl context ) {
    checkArgumentNotNull( controller, Controller.class.getSimpleName() );
    checkArgumentNotNull( context, UIContextImpl.class.getSimpleName() );
    this.controller = controller;
    this.context = context;
  }

  @Override
  public void showPage( String pageId ) throws IllegalStateException {
    showPage( pageId, new Store() );
  }

  @Override
  public void showPage( String pageId, Store store ) throws IllegalStateException {
    checkArgumentNotNull( store, Store.class.getSimpleName() );
    PageDescriptor descriptor = context.getUI().getDescriptorHolder().getPageDescriptor( pageId );
    checkState( descriptor, "Page with id " + pageId + " does not exist." );
    controller.show( context, descriptor, store );
  }

  @Override
  public boolean showPreviousPage() {
    return controller.showPreviousPage( context );
  }

  @Override
  public Page getPage() {
    return controller.getCurrentPage();
  }

  @Override
  public Store getPageStore() {
    return controller.getCurrentStore();
  }

  @Override
  public void setTitle( Page page, String title ) {
    checkArgumentNotNull( page, Page.class.getSimpleName() );
    checkArgumentNotNull( title, "PageTitle" );
    controller.setTitle( page, title );
  }
}

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
import java.util.ArrayList;
import java.util.List;


public class PageFlow implements Serializable {

  private final List<RemotePage> pages;

  public PageFlow( RemotePage root ) {
    checkArgumentNotNull( root, "Root" );
    pages = new ArrayList<RemotePage>();
    pages.add( root );
  }

  public RemotePage getCurrentPage() {
    return pages.get( getIndexOfLastPage() );
  }

  public RemotePage getPreviousPage() {
    int indexOfLastPage = getIndexOfLastPage();
    if( indexOfLastPage > 0 ) {
      return pages.get( indexOfLastPage - 1 );
    }
    return null;
  }

  public RemotePage getRootPage() {
    return pages.get( 0 );
  }

  public void add( RemotePage page ) {
    pages.add( page );
  }

  public RemotePage pop() {
    checkState( pages.size() > 1, "Can not remove root page." );
    return pages.remove( getIndexOfLastPage() );
  }

  private int getIndexOfLastPage() {
    return pages.size() - 1;
  }

  public void destroy() {
    for( RemotePage page : pages ) {
      PageDescriptor descriptor = page.getDescriptor();
      if( !descriptor.isTopLevel() ) {
        page.destroy();
      }
    }
    pages.clear();
  }

  public List<RemotePage> getAllPages() {
    return pages;
  }

}

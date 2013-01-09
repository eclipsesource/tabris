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
package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNull;

import com.eclipsesource.tabris.widgets.swipe.SwipeContext;
import com.eclipsesource.tabris.widgets.swipe.SwipeItemProvider;


public class SwipeManager {

  private final SwipeItemProvider provider;
  private final SwipeContext context;
  private final SwipeItemHolder itemHolder;
  private final SwipeItemIndexer indexer;

  public SwipeManager( SwipeItemProvider provider ) {
    argumentNotNull( provider, "SwipeItemProvier" );
    this.provider = provider;
    this.context = new SwipeContext();
    this.itemHolder = new SwipeItemHolder();
    this.indexer = new SwipeItemIndexer();
  }

  public SwipeItemProvider getProvider() {
    return provider;
  }

  public SwipeContext getContext() {
    return context;
  }

  public SwipeItemHolder getItemHolder() {
    return itemHolder;
  }

  public SwipeItemIndexer getIndexer() {
    return indexer;
  }
}

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

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;


public class PageFlow implements Serializable {

  private final List<PageRenderer> renderers;

  public PageFlow( PageRenderer root ) {
    whenNull( root ).throwIllegalArgument( "Root must not be null" );
    renderers = new ArrayList<PageRenderer>();
    renderers.add( root );
  }

  public PageRenderer getCurrentRenderer() {
    return renderers.get( getIndexOfLastRenderer() );
  }

  public PageRenderer getPreviousRenderer() {
    int indexOfLastPage = getIndexOfLastRenderer();
    if( indexOfLastPage > 0 ) {
      return renderers.get( indexOfLastPage - 1 );
    }
    return null;
  }

  public PageRenderer getRootRenderer() {
    return renderers.get( 0 );
  }

  public void add( PageRenderer renderer ) {
    renderers.add( renderer );
  }

  public PageRenderer pop() {
    when( renderers.size() <= 1 ).throwIllegalState( "Can not remove root page." );
    return renderers.remove( getIndexOfLastRenderer() );
  }

  private int getIndexOfLastRenderer() {
    return renderers.size() - 1;
  }

  public void destroy() {
    for( PageRenderer renderer : renderers ) {
      PageDescriptor descriptor = renderer.getDescriptor();
      if( !descriptor.isTopLevel() ) {
        renderer.destroy();
      }
    }
    renderers.clear();
  }

  public List<PageRenderer> getAllRenderers() {
    return renderers;
  }

}

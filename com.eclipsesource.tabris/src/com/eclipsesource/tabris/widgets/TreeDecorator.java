/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

import org.eclipse.swt.widgets.Tree;


/**
 * @since 0.6
 */
public class TreeDecorator extends WidgetDecorator<TreeDecorator> {
  
  private final Tree tree;

  public TreeDecorator( Tree tree ) {
    super( tree );
    this.tree = tree;
  }
  
  public TreeDecorator useTitle( String title ) {
    tree.setToolTipText( title );
    return this;
  }
}

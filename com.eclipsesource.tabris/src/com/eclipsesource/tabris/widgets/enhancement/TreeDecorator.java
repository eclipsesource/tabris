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
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.WidgetsUtil.setVariant;

import org.eclipse.swt.widgets.Tree;


/**
 * @since 0.8
 */
public class TreeDecorator extends WidgetDecorator<TreeDecorator> {
  
  public enum TreePart {
    LEAF, BRANCH, ALL
  }
  
  private final Tree tree;

  public TreeDecorator( Tree tree ) {
    super( tree );
    this.tree = tree;
  }
  
  public TreeDecorator useTitle( String title ) {
    tree.setToolTipText( title );
    return this;
  }

  /**
   * @since 0.9
   */
  public TreeDecorator enableAlternativeSelection( TreePart part ) {
    switch( part ) {
      case LEAF:
        setVariant( tree, "ALT_SELECTION_LEAF" );
      break;
      case BRANCH:
        setVariant( tree, "ALT_SELECTION_BRANCH" );
        break;
      case ALL:
        setVariant( tree, "ALT_SELECTION" );
        break;
    }
    return this;
  }
}

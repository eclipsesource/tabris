/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.WidgetsUtil.TABRIS_VARIANT;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setVariant;

import org.eclipse.swt.internal.widgets.IDisplayAdapter;
import org.eclipse.swt.internal.widgets.WidgetTreeVisitor;
import org.eclipse.swt.internal.widgets.WidgetTreeVisitor.AllWidgetTreeVisitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

/**
 * @since 0.8
 */
@SuppressWarnings("restriction")
public class TreeDecorator extends WidgetDecorator<TreeDecorator> {

  private static final String VARIANT_BACK_FOCUS = "BACK_FOCUS";

  private class BackButtonVariantTreeVisistor extends AllWidgetTreeVisitor {

    @Override
    public boolean doVisit( Widget widget ) {
      if( widget instanceof Tree && widget != tree ) {
        return removeVariantBackFocus( widget );
      }
      return true;
    }

    private boolean removeVariantBackFocus( Widget widget ) {
      Object variant = widget.getData( TABRIS_VARIANT );
      if (variant != null && variant.equals( VARIANT_BACK_FOCUS )) {
        setVariant( widget, null );
        return false;
      }
      return true;
    }
  }

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

  /**
   * @since 0.10
   */
  public void enableBackButtonNavigation() {
    setVariant( tree, VARIANT_BACK_FOCUS );
    setVariantToNullOnOtherTrees();
  }

  private void setVariantToNullOnOtherTrees() {
    IDisplayAdapter displayAdapter = tree.getDisplay().getAdapter( IDisplayAdapter.class );
    Composite[] shells = displayAdapter.getShells();
    for( int i = 0; i < shells.length; i++ ) {
      WidgetTreeVisitor.accept( shells[ i ], new BackButtonVariantTreeVisistor() );
    }
  }

}

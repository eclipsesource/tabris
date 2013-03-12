/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ALT_SELECTION;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.BACK_FOCUS;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
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

  private class BackButtonDataTreeVisistor extends AllWidgetTreeVisitor {

    @Override
    public boolean doVisit( Widget widget ) {
      if( widget instanceof Tree && widget != tree ) {
        return removeBackFocusData( widget );
      }
      return true;
    }

    private boolean removeBackFocusData( Widget widget ) {
      Object data = widget.getData( BACK_FOCUS.getKey() );
      if( data != null && data.equals( Boolean.TRUE ) ) {
        setData( widget, BACK_FOCUS, null );
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

  /**
   * <p>
   * Defines a title for a tree. This is comparable with a header for a tree.
   * </p>
   *
   * @since 0.9
   */
  public TreeDecorator useTitle( String title ) {
    tree.setToolTipText( title );
    return this;
  }

  /**
   * <p>
   * This enables alternative selection on tree items. When the alternative action will be selected the
   * {@link SelectionEvent#stateMask} contains the {@link SWT#ALT} key.
   * </p>
   *
   * @since 0.9
   */
  public TreeDecorator enableAlternativeSelection( TreePart part ) {
    switch( part ) {
      case LEAF:
        setData( tree, ALT_SELECTION, "leaf" );
      break;
      case BRANCH:
        setData( tree, ALT_SELECTION, "branch" );
      break;
      case ALL:
        setData( tree, ALT_SELECTION, "all" );
      break;
    }
    return this;
  }

  /**
   * <p>
   * Enables the defined tree to take advantage of the back button. This means when the back button will be pressed
   * the tree navigates back.
   * </p>
   *
   * @since 0.10
   */
  public void enableBackButtonNavigation() {
    setData( tree, BACK_FOCUS, Boolean.TRUE );
    setDataToNullOnOtherTrees();
  }

  private void setDataToNullOnOtherTrees() {
    IDisplayAdapter displayAdapter = tree.getDisplay().getAdapter( IDisplayAdapter.class );
    Composite[] shells = displayAdapter.getShells();
    for( int i = 0; i < shells.length; i++ ) {
      WidgetTreeVisitor.accept( shells[ i ], new BackButtonDataTreeVisistor() );
    }
  }

}

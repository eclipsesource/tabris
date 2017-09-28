/*******************************************************************************
 * Copyright (c) 2012, 2017 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.BACK_FOCUS;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.REFRESH_HANDLER;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.template.Cell;
import org.eclipse.rap.rwt.template.Template;
import org.eclipse.swt.internal.widgets.IDisplayAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @since 0.8
 */
@SuppressWarnings("restriction")
public class TreeDecorator extends WidgetDecorator<TreeDecorator> {

  public enum TreePart {
    LEAF, BRANCH, ALL
  }

  private final Tree tree;

  TreeDecorator( Tree tree ) {
    super( tree );
    this.tree = tree;
  }

  /**
   * <p>
   * Enables the defined tree to take advantage of the back button. This means when the back button will be pressed
   * the tree navigates back.
   * </p>
   *
   * @since 0.10
   * @deprecated use {@link TreeDecorator#setBackButtonNavigationEnabled(boolean)}
   */
  @Deprecated
  public TreeDecorator enableBackButtonNavigation() {
    return setBackButtonNavigationEnabled( true );
  }

  /**
   * <p>
   * Enables or disables the defined tree to take advantage of the back button. This means when the back button will
   * be pressed the tree navigates back.
   * </p>
   *
   * @since 1.3
   */
  public TreeDecorator setBackButtonNavigationEnabled( boolean enabled ) {
    setData( tree, BACK_FOCUS, Boolean.valueOf( enabled ) );
    if( enabled ) {
      setDataToNullOnOtherTrees();
    }
    return this;
  }

  private void setDataToNullOnOtherTrees() {
    IDisplayAdapter displayAdapter = tree.getDisplay().getAdapter( IDisplayAdapter.class );
    for( Shell shell : displayAdapter.getShells() ) {
      visitAllTrees( shell );
    }
  }

  private void visitAllTrees( Composite root ) {
    Control[] children = root.getChildren();
    for( Control control : children ) {
      if( control instanceof Tree && control != tree ) {
        removeBackFocusData( control );
      } else if( control instanceof Composite ) {
        visitAllTrees( ( Composite )control );
      }
    }
  }

  private void removeBackFocusData( Control control ) {
    Object data = control.getData( BACK_FOCUS.getKey() );
    if( data != null && data.equals( Boolean.TRUE ) ) {
      setData( control, BACK_FOCUS, null );
    }
  }

  /**
   * <p>
   * Controls the number of preloaded items outside (above and below) visible area of a virtual
   * <code>Tree</code>.
   * </p>
   *
   * @param preloadedItems the items to preload. Must be > 0.
   *
   * @since 1.2
   */
  public TreeDecorator setPreloadedItems( int preloadedItems ) {
    when( preloadedItems < 0 ).throwIllegalArgument( "Preloaded items must be > 0 but was " + preloadedItems );
    tree.setData( RWT.PRELOADED_ITEMS, Integer.valueOf( preloadedItems ) );
    return this;
  }

  /**
   * <p>
   * Sets a {@link Template} on this tree.
   * </P>
   *
   * @param template the template to use. Must not be null.
   *
   * @see Template
   * @see Cell
   * @see RWT#ROW_TEMPLATE
   *
   * @since 1.2
   */
  public TreeDecorator setTemplate( Template template ) {
    whenNull( template ).throwIllegalArgument( "Template must not be null" );
    tree.setData( RWT.ROW_TEMPLATE, template );
    return this;
  }

  /**
   * <p>
   * Set the item height of a {@link TreeItem} in pixels.
   * </p>
   *
   * @param itemHeight the item height in pixels. Must be >= 0.
   *
   * @since 1.2
   */
  public TreeDecorator setItemHeight( int itemHeight ) {
    when( itemHeight < 0 ).throwIllegalArgument( "ItemHeight must be >= 0 but was " + itemHeight );
    tree.setData( RWT.CUSTOM_ITEM_HEIGHT, Integer.valueOf( itemHeight ) );
    return this;
  }

  /**
   * <p>
   * Sets a {@link RefreshHandler} to a {@link Tree}. This instructs a client to add native refresh capabilities to
   * the {@link Tree}.
   * </p>
   *
   * @see RefreshHandler
   *
   * @param handler the {@link RefreshHandler} to add. Must not be <code>null</code>.
   *
   * @since 1.4
   */
  public TreeDecorator setRefreshHandler( RefreshHandler handler ) {
    whenNull( handler ).throwIllegalArgument( "RefreshHandler must not be null" );
    setData( tree, REFRESH_HANDLER, handler.getId() );
    handler.hookToWidget( tree );
    return this;
  }

}

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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ALT_SELECTION;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;


/**
 * @since 0.9
 */
public class ListDecorator extends WidgetDecorator<ListDecorator> {

  private final List list;

  public ListDecorator( List list ) {
    super( list );
    this.list = list;
  }

  /**
   * <p>
   * Defines a title for a list. This is comparable with a header for a list.
   * </p>
   *
   * @since 0.9
   */
  public ListDecorator useTitle( String title ) {
    list.setToolTipText( title );
    return this;
  }

  /**
   * <p>
   * This enables alternative selection on list items. When the alternative action will be selected the
   * {@link SelectionEvent#stateMask} contains the {@link SWT#ALT} key.
   * </p>
   *
   * @since 0.9
   */
  public ListDecorator enableAlternativeSelection() {
    setData( list, ALT_SELECTION, "all" );
    return this;
  }

}
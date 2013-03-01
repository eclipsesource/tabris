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

  public ListDecorator useTitle( String title ) {
    list.setToolTipText( title );
    return this;
  }

  public ListDecorator enableAlternativeSelection() {
    setData( list, ALT_SELECTION, "all" );
    return this;
  }

}
/*******************************************************************************
 * Copyright (c) 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ACTION_STYLE;

import org.eclipse.swt.widgets.MenuItem;

import com.eclipsesource.tabris.internal.WidgetsUtil;

/**
 * @since 3.5
 */
public class MenuItemDecorator {

  private final MenuItem item;

  MenuItemDecorator( MenuItem item ) {
    this.item = item;
  }

  /**
   * Enables the 'destructive' action style for this menu item (iOs only).
   * A destructive action is shown in red.
   *
   * @since 3.5
   */
  public MenuItemDecorator useDestructiveStyle() {
    WidgetsUtil.setData( item, ACTION_STYLE, "destructive" );
    return this;
  }

}


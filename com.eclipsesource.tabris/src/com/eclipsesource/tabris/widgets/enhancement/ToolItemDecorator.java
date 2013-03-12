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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.TITLE;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.swt.widgets.ToolItem;


/**
 * @since 0.8
 */
public class ToolItemDecorator {

  private final ToolItem toolItem;

  ToolItemDecorator( ToolItem toolItem ) {
    this.toolItem = toolItem;
  }

  /**
   * <p>
   * Instructs a {@link ToolItem} to represent itself as a title item.
   * </p>
   *
   * @since 0.8
   */
  public ToolItemDecorator useAsTitle() {
    setData( toolItem, TITLE, Boolean.TRUE );
    return this;
  }
}

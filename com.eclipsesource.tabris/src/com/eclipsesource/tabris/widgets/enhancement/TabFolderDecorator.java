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
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.PAGING;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.swt.widgets.TabFolder;


/**
 * @since 1.1
 */
public class TabFolderDecorator extends WidgetDecorator<TabFolderDecorator> {

  private final TabFolder tabFolder;

  TabFolderDecorator( TabFolder tabFolder ) {
    super( tabFolder );
    this.tabFolder = tabFolder;
  }

  /**
   * <p>
   * Enables pagewise scrolling.
   * </p>
   */
  public TabFolderDecorator usePaging() {
    setData( tabFolder, PAGING, Boolean.TRUE );
    return this;
  }
}

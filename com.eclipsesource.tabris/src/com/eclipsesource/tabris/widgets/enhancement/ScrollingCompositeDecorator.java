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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.PAGING;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import com.eclipsesource.tabris.widgets.ScrollingComposite;


/**
 * @since 1.0
 */
public class ScrollingCompositeDecorator extends WidgetDecorator<ScrollingCompositeDecorator> {

  private final ScrollingComposite composite;

  ScrollingCompositeDecorator( ScrollingComposite composite ) {
    super( composite );
    this.composite = composite;
  }

  public ScrollingCompositeDecorator usePaging() {
    setData( composite.getParent(), PAGING, Boolean.TRUE );
    return this;
  }

}

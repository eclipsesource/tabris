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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ZOOM;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.swt.widgets.Label;


/**
 * @since 0.8
 */
public class LabelDecorator extends WidgetDecorator<LabelDecorator>{

  private final Label label;

  LabelDecorator( Label label ) {
    super( label );
    this.label = label;
  }

  /**
   * <p>
   * Enables a zooming behavior for a {@link Label}. Can be used e.g. to display very large images on a smaller
   * {@link Label}.
   * </p>
   *
   * @since 0.8
   */
  public LabelDecorator useZoom() {
    setData( label, ZOOM, Boolean.TRUE );
    return this;
  }
}

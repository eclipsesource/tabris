/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.SPINNING_INDICATOR;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.swt.widgets.ProgressBar;


/**
 * @since 1.4
 */
public class ProgressBarDecorator extends WidgetDecorator<ProgressBarDecorator> {

  private final ProgressBar progressBar;

  public ProgressBarDecorator( ProgressBar progressBar ) {
    super( progressBar );
    this.progressBar = progressBar;
  }

  public ProgressBarDecorator useSpinningIndicator() {
    setData( progressBar, SPINNING_INDICATOR, Boolean.TRUE );
    return this;
  }
}

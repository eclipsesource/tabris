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
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.WidgetsUtil.setVariant;

import org.eclipse.swt.widgets.Label;


/**
 * @since 0.6
 */
public class LabelDecorator extends WidgetDecorator<LabelDecorator>{

  private final Label label;

  LabelDecorator( Label label ) {
    super( label );
    this.label = label;
  }

  public LabelDecorator useZoom() {
    setVariant( label, "ZOOM" );
    return this;
  }
}

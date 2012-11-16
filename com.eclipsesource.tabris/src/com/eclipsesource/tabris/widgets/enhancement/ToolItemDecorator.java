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

import static com.eclipsesource.tabris.internal.WidgetsUtil.setVariant;

import org.eclipse.swt.widgets.ToolItem;


/**
 * @since 0.8
 */
public class ToolItemDecorator {
  
  private final ToolItem toolItem;

  ToolItemDecorator( ToolItem toolItem ) {
    this.toolItem = toolItem;
  }
  
  public ToolItemDecorator useAsTitle() {
    setVariant( toolItem, "TITLE" );
    return this;
  }
}

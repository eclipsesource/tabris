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

import org.eclipse.swt.custom.ScrolledComposite;


/**
 * @since 0.8
 */
public class ScrolledCompositeDecorator extends WidgetDecorator<ScrolledCompositeDecorator> {
  
  private final ScrolledComposite composite;

  ScrolledCompositeDecorator( ScrolledComposite composite ) {
    super( composite );
    this.composite = composite;
  }
  
  public ScrolledCompositeDecorator usePaging() {
    setVariant( composite, "PAGINGENABLED" );
    return this;
  }
  
}

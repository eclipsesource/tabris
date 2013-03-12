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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ANIMATED;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.SHOW_TOUCH;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.swt.widgets.Widget;


/**
 * @since 0.8
 */
public class WidgetDecorator<T extends WidgetDecorator> {

  private final Widget widget;

  WidgetDecorator( Widget widget ) {
    this.widget = widget;
  }

  /**
   * <p>
   * Instructs a widget to be animated when it's bounds change.
   * </p>
   *
   * @since 0.8
   */
  @SuppressWarnings("unchecked")
  public T useAnimation() {
    setData( widget, ANIMATED, Boolean.TRUE );
    return ( T )this;
  }

  /**
   * <p>
   * Instructs a {@link Widget} to show immediate visual feedback on a user interaction.
   * </p>
   *
   * @since 0.10
   */
  @SuppressWarnings("unchecked")
  public T showLocalTouch() {
    setData( widget, SHOW_TOUCH, Boolean.TRUE );
    return ( T )this;
  }

}

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

import java.io.Serializable;

import org.eclipse.swt.widgets.Widget;


/**
 * @since 0.8
 */
public class WidgetDecorator<T extends WidgetDecorator> implements Serializable {

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
   * @deprecated use {@link WidgetDecorator#showLocalTouch(boolean)} instead.
   *
   * @since 0.10
   */
  @Deprecated
  public T showLocalTouch() {
    return showLocalTouch( true );
  }

  /**
   * <p>
   * Instructs a {@link Widget} to show or hide immediate visual feedback on a user interaction.
   * </p>
   *
   * @since 1.2
   */
  @SuppressWarnings("unchecked")
  public T showLocalTouch( boolean show ) {
    setData( widget, SHOW_TOUCH, Boolean.valueOf( show ) );
    return ( T )this;
  }

}

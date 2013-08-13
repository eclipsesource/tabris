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


import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.BADGE_VALUE;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.swt.widgets.TabItem;


/**
 * @since 1.2
 */
public class TabItemDecorator extends WidgetDecorator<TabItemDecorator> {

  private final TabItem item;

  TabItemDecorator( TabItem item ) {
    super( item );
    this.item = item;
  }

  /**
   * <p>
   * Instructs the {@link TabItem} to show a badge with the given value.
   * An empty String will make the badge disappear.
   * </p>
   *
   * @since 1.2
   */
  public TabItemDecorator setBadgeValue( String value ) {
    whenNull( value ).throwIllegalArgument( "Value must not be null" );
    setData( item, BADGE_VALUE, value );
    return this;
  }

}
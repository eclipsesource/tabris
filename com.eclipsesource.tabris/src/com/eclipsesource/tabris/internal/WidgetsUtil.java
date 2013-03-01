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
package com.eclipsesource.tabris.internal;

import org.eclipse.swt.widgets.Widget;

import com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry;


public class WidgetsUtil {

  public static void setData( Widget widget, WhiteListEntry key, Object value ) {
    widget.setData( key.getKey(), value );
  }

  public static void checkComponent( Object component ) {
    if( component == null ) {
      throw new IllegalArgumentException( "Widget/Item must not be null" );
    }
  }

  private WidgetsUtil() {
    // prevent instantiation
  }
}

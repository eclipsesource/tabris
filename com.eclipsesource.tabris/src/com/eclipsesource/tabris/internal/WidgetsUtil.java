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

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Widget;


public class WidgetsUtil {

  public static final String TABRIS_VARIANT = RWT.CUSTOM_VARIANT;

  public static void setVariant( Widget widget, String variant ) {
    widget.setData( TABRIS_VARIANT, variant );
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

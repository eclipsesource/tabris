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
package com.eclipsesource.tabris.internal;

import org.eclipse.rap.rwt.internal.client.WidgetDataWhiteList;


@SuppressWarnings("restriction")
public class DataWhitelist implements WidgetDataWhiteList {

  public enum WhiteListEntry {

    ANIMATED( "animated" ),
    TITLE( "title" ),
    KEYBOARD( "keyboard" ),
    PAGING( "paging" ),
    ALT_SELECTION( "altSelection" ),
    ZOOM( "zoom" ),
    BACK_FOCUS( "backFocus" ),
    CLIENT_CANVAS( "clientCanvas" ),
    SWIPE( "swipe" ),
    SHOW_TOUCH( "showTouch" );

    private final String key;

    private WhiteListEntry( String key ) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }

  }

  @Override
  public String[] getKeys() {
    WhiteListEntry[] whiteList = WhiteListEntry.values();
    String[] keys = new String[ whiteList.length ];
    for( int i = 0; i < whiteList.length; i++ ) {
      keys[ i ] = whiteList[ i ].getKey();
    }
    return keys;
  }
}

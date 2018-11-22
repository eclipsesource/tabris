/*******************************************************************************
 * Copyright (c) 2013, 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import java.io.Serializable;

import org.eclipse.rap.rwt.internal.lifecycle.WidgetUtil;


@SuppressWarnings("restriction")
public class DataWhitelist implements Serializable {

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
    SPINNING_INDICATOR( "spinningIndicator" ),
    SHOW_TOUCH( "showTouch" ),
    BADGE_VALUE( "badgeValue" ),
    OVERLAY_COLOR( "overlayColor" ),
    TEXT_REPLACEMENT( "textReplacement" ),
    AUTO_CAPITALIZE( "autoCapitalize" ),
    AUTO_CORRECT( "autoCorrect" ),
    REFRESH_HANDLER( "refreshHandler" ),
    REFRESH_COMPOSITE("refreshComposite"),
    LOCAL_CLIPBOARD( "localClipboard" ),
    DISABLE_LOOKUP_ACTION( "disableLookupAction" ),
    DISABLE_SHARE_ACTION( "disableShareAction" ),
    ACTION_STYLE( "actionStyle" );

    private final String key;

    private WhiteListEntry( String key ) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }
  }

  public static void register() {
    WhiteListEntry[] values = WhiteListEntry.values();
    for( WhiteListEntry whiteListEntry : values ) {
      WidgetUtil.registerDataKeys( whiteListEntry.getKey() );
    }
  }

  private DataWhitelist() {
    // prevent instantiation
  }

}

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


public class Constants {

  // System Constants
  public static final String ID_ANDROID = "com.eclipsesource.tabris.android";
  public static final String ID_IOS = "com.eclipsesource.tabris.ios";
  public static final String USER_AGENT = "User-Agent";
  public static final String THEME_ID_ANDROID = "com.eclipsesource.rap.mobile.theme.android";
  public static final String THEME_ID_IOS = "com.eclipsesource.rap.mobile.theme.ios";
  public static final String THEME_ID_IOS6 = "com.eclipsesource.rap.mobile.theme.ios6";
  public static final String THEME_PATH_ANDROID = "theme/android.css";
  public static final String THEME_PATH_IOS = "theme/ios.css";
  public static final String THEME_PATH_IOS6 = "theme/ios6.css";
  public static final String THEME_PATH_WEB = "theme/web.css";
  public static final String INDEX_JSON = "index.json";
  public static final String CUSTOM_VARIANT_TABRIS_UI = "tabrisUI";

  // Common Constants
  public static final String METHOD_OPEN = "open";
  public static final String METHOD_REMOVE = "remove";
  public static final String METHOD_ADD = "add";
  public static final String PROPERTY_PARENT = "parent";
  public static final String PROPERTY_DATA = "data";
  public static final String PROPERTY_IMAGE = "image";
  public static final String PROPERTY_FOREGROUND = "foreground";
  public static final String PROPERTY_URL = "url";
  public static final String PROPERTY_LONGITUDE = "longitude";
  public static final String PROPERTY_LATITUDE = "latitude";
  public static final String PROPERTY_CONTROL = "control";
  public static final String PROPERTY_QUERY = "query";
  public static final String PROPERTY_MESSAGE = "message";
  public static final String PROPERTY_ERROR_MESSAGE = "errorMessage";
  public static final String PROPERTY_ERROR_CODE = "errorCode";

  // TabrisUI Constants
  public static final String EVENT_SHOW_PREVIOUS_PAGE = "ShowPreviousPage";
  public static final String EVENT_SHOW_PAGE = "ShowPage";
  public static final String EVENT_SELECTION = "Selection";
  public static final String EVENT_MODIFY = "Modify";
  public static final String EVENT_SEARCH = "Search";
  public static final String METHOD_DEACTIVATE = "deactivate";
  public static final String METHOD_ACTIVATE = "activate";
  public static final String PROPERTY_ACTIVE_PAGE = "activePage";
  public static final String PROPERTY_PAGE_ID = "pageId";
  public static final String PROPERTY_TITLE = "title";
  public static final String PROPERTY_STYLE = "style";
  public static final String PROPERTY_TOP_LEVEL = "topLevel";
  public static final String PROPERTY_VISIBILITY = "visibility";
  public static final String PROPERTY_ENABLED = "enabled";
  public static final String PROPERTY_PROMINENCE = "prominence";
  public static final String PROPERTY_SHELL = "shell";
  public static final String PROPERTY_BACKGROUND = "background";
  public static final String PROPERTY_PROPOSALS = "proposals";
  public static final String PROPERTY_PLACEMENT_PRIORITY = "placementPriority";

  // Geolocation Constants
  public static final String TYPE_GEOLOCATION = "tabris.Geolocation";
  public static final String EVENT_LOCATION_UPDATE_ERROR_EVENT = "LocationUpdateError";
  public static final String EVENT_LOCATION_UPDATE_EVENT = "LocationUpdate";
  public static final String PROPERTY_HIGH_ACCURACY = "highAccuracy";
  public static final String PROPERTY_MAXIMUM_AGE = "maximumAge";
  public static final String PROPERTY_FREQUENCY = "frequency";
  public static final String PROPERTY_NEEDS_POSITION = "needsPosition";
  public static final String PROPERTY_TIMESTAMP = "timestamp";
  public static final String PROPERTY_SPEED = "speed";
  public static final String PROPERTY_HEADING = "heading";
  public static final String PROPERTY_ALTITUDE_ACCURACY = "altitudeAccuracy";
  public static final String PROPERTY_ACCURACY = "accuracy";
  public static final String PROPERTY_ALTITUDE = "altitude";

  // Camera Constants
  public static final String TYPE_CAMERA = "tabris.Camera";
  public static final String EVENT_IMAGE_SELECTION = "ImageSelection";
  public static final String EVENT_IMAGE_SELECTION_ERROR = "ImageSelectionError";
  public static final String EVENT_IMAGE_SELECTION_CANCEL = "ImageSelectionCancel";
  public static final String PROPERTY_RESOLUTION = "resolution";
  public static final String PROPERTY_SAVE_TO_ALBUM = "saveToAlbum";
  public static final String PROPERTY_COMPRESSON_QUALITY = "compressionQuality";

  // Video Constants
  public static final String TYPE_VIDEO = "tabris.widgets.Video";
  public static final String EVENT_PRESENTATION = "Presentation";
  public static final String EVENT_PLAYBACK = "Playback";
  public static final String PROPERTY_PLAYBACK = "playback";
  public static final String PROPERTY_PRESENTATION = "presentation";

  // Swipe Constants
  public static final String TYPE_SWIPE = "tabris.Swipe";
  public static final String EVENT_SWIPE = "Swipe";
  public static final String METHOD_LOCK_LEFT = "lockLeft";
  public static final String METHOD_LOCK_RIGHT = "lockRight";
  public static final String METHOD_UNLOCK_LEFT = "unlockLeft";
  public static final String METHOD_UNLOCK_RIGHT = "unlockRight";
  public static final String PROPERTY_ITEM = "item";
  public static final String PROPERTY_ITEMS = "items";
  public static final String PROPERTY_ITEM_COUNT = "itemCount";
  public static final String PROPERTY_ACTIVE = "active";
  public static final String PROPERTY_INDEX = "index";

  // GC Constants
  public static final String PROPERTY_PATH = "path";
  public static final String PROPERTY_LINE_WIDTH = "lineWidth";

  // ClientStore Constants
  public static final String TYPE_CLIENT_STORE = "tabris.ClientStore";
  public static final String METHOD_SYNCHRONIZE = "synchronize";
  public static final String METHOD_CLEAR = "clear";
  public static final String PROPERTY_KEYS = "keys";
  public static final String PROPERTY_VALUE = "value";
  public static final String PROPERTY_KEY = "key";

  // ClientDevice Constants
  public static final String TYPE_CLIENT_DEVICE = "tabris.Device";
  public static final String PROPERTY_TIMEZONE_OFFSET = "timezoneOffset";
  public static final String PROPERTY_CONNECTION_TYPE = "connectionType";
  public static final String PROPERTY_CAPABILITIES = "capabilities";
  public static final String PROPERTY_ORIENTATION = "orientation";

  // App Constants
  public static final String TYPE_APP = "tabris.App";
  public static final String EVENT_BACK_NAVIGATION = "BackNavigation";
  public static final String METHOD_START_INACTIVITY_TIMER = "startInactivityTimer";
  public static final String METHOD_STOP_INACTIVITY_TIMER = "stopInactivityTimer";
  public static final String PROPERTY_INACTIVITY_TIME = "inactivityTime";
  public static final String PROPERTY_SCREEN_PROTECTION = "screenProtection";
  public static final String PROPERTY_BADGE_NUMBER = "badgeNumber";

  // AppLauncher Constants
  public static final String TYPE_APP_LAUNCHER = "tabris.AppLauncher";
  public static final String METHOD_OPEN_URL = "openUrl";
  public static final String PROPERTY_APP = "app";

  // LaunchOptions Constants
  public static final String PROPERTY_TEXT = "text";
  public static final String PROPERTY_NUMBER = "number";
  public static final String PROPERTY_ZOOM = "zoom";
  public static final String PROPERTY_CC = "cc";
  public static final String PROPERTY_BODY = "body";
  public static final String PROPERTY_TO = "to";
  public static final String PROPERTY_SUBJECT = "subject";
  public static final String PROPERTY_HTML = "html";

  // XCallbackURL Constants
  public static final String TYPE_XCALLBACK = "tabris.XCallback";
  public static final String EVENT_SUCCESS = "Success";
  public static final String EVENT_ERROR = "Error";
  public static final String EVENT_CANCEL = "Cancel";
  public static final String METHOD_CALL = "call";
  public static final String PROPERTY_PARAMETERS = "parameters";
  public static final String PROPERTY_TARGET_SCHEME = "targetScheme";
  public static final String PROPERTY_TARGET_ACTION = "targetAction";
  public static final String PROPERTY_XSOURCE = "xSource";
  public static final String PROPERTY_ACTION_PARAMETERS = "actionParameters";

  private Constants() {
    // prevent instantiation
  }

}

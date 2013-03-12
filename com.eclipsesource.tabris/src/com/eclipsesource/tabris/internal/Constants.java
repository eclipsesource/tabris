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
  public static final String THEME_ID_IOS = "com.eclipsesource.rap.mobile.theme.ios";
  public static final String THEME_ID_ANDROID = "com.eclipsesource.rap.mobile.theme.android";
  public static final String THEME_PATH_IOS = "theme/ios.css";
  public static final String THEME_PATH_ANDROID = "theme/theme-android-holo.css";

  // Common Constants
  public static final String METHOD_OPEN = "open";
  public static final String PROPERTY_PARENT = "parent";
  public static final String PROPERTY_DATA = "data";
  public static final String PROPERTY_IMAGE = "image";
  public static final String PROPERTY_FOREGROUND = "foreground";
  public static final String PROPERTY_URL = "url";
  public static final String PROPERTY_LONGITUDE = "longitude";
  public static final String PROPERTY_LATITUDE = "latitude";

  // TabrisUI Constants
  public static final String EVENT_SHOW_PREVIOUS_PAGE = "ShowPreviousPage";
  public static final String EVENT_SHOW_PAGE = "ShowPage";
  public static final String EVENT_SELECTION = "Selection";
  public static final String PROPERTY_ACTIVE_PAGE = "activePage";
  public static final String PROPERTY_PAGE_ID = "pageId";
  public static final String PROPERTY_CONTROL = "control";
  public static final String PROPERTY_TITLE = "title";
  public static final String PROPERTY_STYLE = "style";
  public static final String PROPERTY_TOP_LEVEL = "topLevel";
  public static final String PROPERTY_VISIBILITY = "visibility";
  public static final String PROPERTY_ENABLED = "enabled";
  public static final String PROPERTY_PROMINENCE = "prominence";
  public static final String PROPERTY_SHELL = "shell";
  public static final String PROPERTY_BACKGROUND = "background";

  // Geolocation Constants
  public static final String TYPE_GEOLOCATION = "tabris.Geolocation";
  public static final String EVENT_LOCATION_UPDATE_ERROR_EVENT = "LocationUpdateError";
  public static final String EVENT_LOCATION_UPDATE_EVENT = "LocationUpdate";
  public static final String PROPERTY_ENABLE_HIGH_ACCURACY = "enableHighAccuracy";
  public static final String PROPERTY_MAXIMUM_AGE = "maximumAge";
  public static final String PROPERTY_FREQUENCY = "frequency";
  public static final String PROPERTY_NEEDS_POSITION = "needsPosition";
  public static final String PROPERTY_TIMESTAMP = "timestamp";
  public static final String PROPERTY_SPEED = "speed";
  public static final String PROPERTY_HEADING = "heading";
  public static final String PROPERTY_ALTITUDE_ACCURACY = "altitudeAccuracy";
  public static final String PROPERTY_ACCURACY = "accuracy";
  public static final String PROPERTY_ALTITUDE = "altitude";
  public static final String PROPERTY_ERROR_MESSAGE = "errorMessage";
  public static final String PROPERTY_ERROR_CODE = "errorCode";

  // Camera Constants
  public static final String TYPE_CAMERA = "tabris.Camera";
  public static final String EVENT_IMAGE_SELECTION = "ImageSelection";
  public static final String EVENT_IMAGE_SELECTION_ERROR = "ImageSelectionError";
  public static final String PROPERTY_RESOLUTION = "resolution";
  public static final String PROPERTY_SAVETOALBUM = "saveToAlbum";

  // Video Constants
  public static final String TYPE_VIDEO = "tabris.widgets.Video";
  public static final String EVENT_PRESENTATION = "PresentationChanged";
  public static final String EVENT_PLAYBACK = "PlaybackChanged";
  public static final String PROPERTY_VIDEO_URL = "videoURL";
  public static final String PROPERTY_VIDEO_LISTENER = "video";
  public static final String PROPERTY_MODE = "mode";

  // Swipe Constants
  public static final String TYPE_SWIPE = "tabris.Swipe";
  public static final String EVENT_SWIPED_TO_ITEM = "SwipedToItem";
  public static final String METHOD_REMOVE_ITEMS = "removeItems";
  public static final String METHOD_ITEM_LOADED = "itemLoaded";
  public static final String METHOD_LOCK_LEFT = "lockLeft";
  public static final String METHOD_LOCK_RIGHT = "lockRight";
  public static final String METHOD_UNLOCK_LEFT = "unlockLeft";
  public static final String METHOD_UNLOCK_RIGHT = "unlockRight";
  public static final String PROPERTY_ITEM = "item";
  public static final String PROPERTY_ITEMS = "items";
  public static final String PROPERTY_ACTIVE_ITEM = "activeItem";
  public static final String PROPERTY_CONTENT = "content";
  public static final String PROPERTY_INDEX = "index";

  // GC Constants
  public static final String PROPERTY_POLYLINE = "polyline";
  public static final String PROPERTY_LINE_WIDTH = "lineWidth";

  // ClientStore Constants
  public static final String TYPE_CLIENT_STORE = "tabris.ClientStore";
  public static final String METHOD_SYNCHRONIZE_STORE = "synchronizeStore";
  public static final String METHOD_CLEAR = "clear";
  public static final String METHOD_REMOVE = "remove";
  public static final String METHOD_ADD = "add";
  public static final String PROPERTY_KEYS = "keys";
  public static final String PROPERTY_VALUE = "value";
  public static final String PROPERTY_KEY = "key";

  // ClientDevice Constants
  public static final String TYPE_CLIENT_DEVICE = "tabris.Device";
  public static final String PROPERTY_TIMEZONE_OFFSET = "timezoneOffset";
  public static final String PROPERTY_CONNECTION_TYPE = "connectionType";
  public static final String PROPERTY_CAPABILITIES = "capabilities";
  public static final String PROPERTY_ORIENTATION = "orientation";

  // AppLauncher Constants
  public static final String TYPE_APP_LAUNCHER = "tabris.AppLauncher";
  public static final String METHOD_OPEN_URL = "openUrl";
  public static final String PROPERTY_APP = "app";

  // App Constants
  public static final String TYPE_APP = "tabris.App";
  public static final String EVENT_BACK_NAVIGATION = "BackNavigation";

  // LaunchOptions Constants
  public static final String PROPERTY_TEXT = "text";
  public static final String PROPERTY_NUMBER = "number";
  public static final String PROPERTY_ZOOM = "zoom";
  public static final String PROPERTY_QUERY = "query";
  public static final String PROPERTY_CC = "cc";
  public static final String PROPERTY_BODY = "body";
  public static final String PROPERTY_TO = "to";
  public static final String PROPERTY_SUBJECT = "subject";
  public static final String PROPERTY_USE_HTML = "useHtml";

  private Constants() {
    // prevent instantiation
  }

}

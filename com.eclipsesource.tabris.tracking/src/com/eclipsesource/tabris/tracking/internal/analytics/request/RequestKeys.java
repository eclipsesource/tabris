/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.analytics.request;

import java.io.Serializable;


public class RequestKeys implements Serializable {

  public static final String APP_NAME = "APP_NAME";
  public static final String APP_VERSION = "APP_VERSION";
  public static final String CLIENT_ID = "CLIENT_ID";
  public static final String CONTENT_DESCRIPTION = "CONTENT_DESCRIPTION";
  public static final String CUSTOM_DIMENSION = "CUSTOM_DIMENSION";
  public static final String CUSTOM_METRIC = "CUSTOM_METRIC";
  public static final String DOCUMENT_HOST_NAME = "DOCUMENT_HOST_NAME";
  public static final String DOCUMENT_PATH = "DOCUMENT_PATH";
  public static final String DOCUMENT_TITLE = "DOCUMENT_TITLE";
  public static final String EVENT_CATEGORY = "EVENT_CATEGORY";
  public static final String EVENT_ACTION = "EVENT_ACTION";
  public static final String EVENT_LABEL = "EVENT_LABEL";
  public static final String EVENT_VALUE = "EVENT_VALUE";
  public static final String EXCEPTION_DESCRIPTION = "EXCEPTION_DESCRIPTION";
  public static final String EXCEPTION_FATAL = "EXCEPTION_FATAL";
  public static final String HIT_TYPE = "HIT_TYPE";
  public static final String IP_OVERRIDE = "IP_OVERRIDE";
  public static final String ITEM_NAME = "ITEM_NAME";
  public static final String ITEM_PRICE = "ITEM_PRICE";
  public static final String SCREEN_RESOLUTION = "SCREEN_RESOLUTION";
  public static final String SESSION_CONTROL = "SESSION_CONTROL";
  public static final String TRACKING_ID = "TRACKING_ID";
  public static final String TRANSACTION_ID = "TRANSACTION_ID";
  public static final String TRANSACTION_AFFILIATION = "TRANSACTION_AFFILIATION";
  public static final String USER_AGENT_OVERRIDE = "USER_AGENT_OVERRIDE";
  public static final String USER_ID = "USER_ID";
  public static final String USER_LANGUAGE = "USER_LANGUAGE";
  public static final String USER_TIMING_CATEGORY = "USER_TIMING_CATEGORY";
  public static final String USER_TIMING_VARIABLE_NAME = "USER_TIMING_VARIABLE_NAME";
  public static final String USER_TIMING_TIME = "USER_TIMING_TIME";
  public static final String USER_TIMING_LABEL = "USER_TIMING_LABEL";
  public static final String PAGE_LOAD_TIME = "PAGE_LOAD_TIME";
  public static final String DNS_TIME = "DNS_TIME";
  public static final String PAGE_DOWNLOAD_TIME = "PAGE_DOWNLOAD_TIME";
  public static final String REDIRECT_RESPONSE_TIME = "REDIRECT_RESPONSE_TIME";
  public static final String TCP_CONNECT_TIME = "TCP_CONNECT_TIME";
  public static final String SERVER_RESPONSE_TIME = "SERVER_RESPONSE_TIME";
  public static final String VERSION = "VERSION";
  public static final String VIEWPORT_SIZE = "VIEWPORT_SIZE";
  public static final String SOCIAL_NETWORK = "SOCIAL_NETWORK";
  public static final String SOCIAL_ACTION = "SOCIAL_ACTION";
  public static final String SOCIAL_ACTION_TARGET = "SOCIAL_ACTION_TARGET";
  public static final String TRANSACTION_REVENUE = "TRANSACTION_REVENUE";
  public static final String TRANSACTION_SHIPPING = "TRANSACTION_SHIPPING";
  public static final String TRANSACTION_TAX = "TRANSACTION_TAX";
  public static final String CURRENCY_CODE = "CURRENCY_CODE";
  public static final String ITEM_QUANTITY = "ITEM_QUANTITY";
  public static final String ITEM_CATEGORY = "ITEM_CATEGORY";
  public static final String ITEM_CODE = "ITEM_CODE";

  private RequestKeys() {
    // prevent instantiation
  }
}

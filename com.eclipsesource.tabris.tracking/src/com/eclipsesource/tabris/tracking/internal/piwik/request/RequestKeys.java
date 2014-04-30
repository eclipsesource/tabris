/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.request;

public class RequestKeys {

  public static final String SITE_ID = "SITE_ID";
  public static final String REC = "REC";
  public static final String ACTION_URL = "ACTION_URL";
  public static final String ACTION_NAME = "ACTION_NAME";
  public static final String VISITOR_ID = "VISITOR_ID";
  public static final String RANDOM = "RANDOM";
  public static final String API_VERSION = "API_VERSION";
  public static final String VISITOR_REFERRER_URL = "VISITOR_REFERRER_URL";
  public static final String VISITOR_CUSTOM_VARIABLES = "VISITOR_CUSTOM_VARIABLES";
  public static final String VISITOR_VISITS = "VISITOR_VISITS";
  public static final String VISITOR_PREVIOUS_VISIT = "VISITOR_PREVIOUS_VISIT";
  public static final String VISITOR_FIRST_VISIT = "VISITOR_FIRST_VISIT";
  public static final String VISITOR_CAMPAIGN_NAME = "VISITOR_CAMPAIGN_NAME";
  public static final String VISITOR_CAMPAIGN_KEYWORD = "VISITOR_CAMPAIGN_KEYWORD";
  public static final String VISITOR_RESOLUTION = "VISITOR_RESOLUTION";
  public static final String VISITOR_HOUR = "VISITOR_HOUR";
  public static final String VISITOR_MINUTE = "VISITOR_MINUTE";
  public static final String VISITOR_SECOND = "VISITOR_SECOND";
  public static final String VISITOR_USER_AGENT_OVERRIDE = "VISITOR_USER_AGENT_OVERRIDE";
  public static final String VISITOR_LANGUAGE_OVERRIDE = "VISITOR_LANGUAGE_OVERRIDE";
  public static final String ACTION_CUSTOM_VARIABLES = "ACTION_CUSTOM_VARIABLES";
  public static final String ACTION_OUTLINK = "ACTION_OUTLINK";
  public static final String ACTION_DOWNLOAD = "ACTION_DOWNLOAD";
  public static final String ACTION_SEARCH = "ACTION_SEARCH";
  public static final String ACTION_SEARCH_CATEGORY = "ACTION_SEARCH_CATEGORY";
  public static final String ACTION_SEARCH_COUNT = "ACTION_SEARCH_COUNT";
  public static final String ACTION_GOAL_ID = "ACTION_GOAL_ID";
  public static final String ACTION_GOAL_REVENUE = "ACTION_GOAL_REVENUE";
  public static final String ACTION_GENERATION_TIME = "ACTION_GENERATION_TIME";
  public static final String ECOMMERCE_ORDER_ID = "ECOMMERCE_ORDER_ID";
  public static final String ECOMMERCE_SUB_TOTAL = "ECOMMERCE_SUB_TOTAL";
  public static final String ECOMMERCE_TAX_AMOUNT = "ECOMMERCE_TAX_AMOUNT";
  public static final String ECOMMERCE_SHIPPING_COST = "ECOMMERCE_SHIPPING_COST";
  public static final String ECOMMERCE_DISCOUNT = "ECOMMERCE_DISCOUNT";
  public static final String ECOMMERCE_ITEMS = "ECOMMERCE_ITEMS";
  public static final String TOKEN_AUTH = "TOKEN_AUTH";
  public static final String VISITOR_IP_OVERRIDE = "VISITOR_IP_OVERRIDE";
  public static final String VISITOR_DATETIME_OVERRIDE = "VISITOR_DATETIME_OVERRIDE";
  public static final String VISITOR_ID_ENFORCED = "VISITOR_ID_ENFORCED";
  public static final String NEW_VISIT = "NEW_VISIT";
  public static final String VISITOR_COUNTRY_OVERRIDE = "VISITOR_COUNTRY_OVERRIDE";
  public static final String VISITOR_REGION_OVERRIDE = "VISITOR_REGION_OVERRIDE";
  public static final String VISITOR_CITY_OVERRIDE = "VISITOR_CITY_OVERRIDE";
  public static final String VISITOR_LATITUDE_OVERRIDE = "VISITOR_LATITUDE_OVERRIDE";
  public static final String VISITOR_LONGITUDE_OVERRIDE = "VISITOR_LONGITUDE_OVERRIDE";
  public static final String IS_BOT = "IS_BOT";

  private RequestKeys() {
    // prevent instantiation
  }
}

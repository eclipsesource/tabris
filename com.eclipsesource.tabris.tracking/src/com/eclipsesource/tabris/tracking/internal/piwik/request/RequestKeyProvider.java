/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestKeyProvider {

  static Map<String, String> requestKeys;
  static {
    Map<String, String> mapping = new HashMap<String, String>();
    mapping.put( RequestKeys.SITE_ID, "idsite" );
    mapping.put( RequestKeys.REC, "rec" );
    mapping.put( RequestKeys.ACTION_URL, "url" );
    mapping.put( RequestKeys.ACTION_NAME, "action_name" );
    mapping.put( RequestKeys.VISITOR_ID, "_id" );
    mapping.put( RequestKeys.RANDOM, "rand" );
    mapping.put( RequestKeys.API_VERSION, "apiv" );
    mapping.put( RequestKeys.VISITOR_REFERRER_URL, "urlref" );
    mapping.put( RequestKeys.VISITOR_CUSTOM_VARIABLES, "_cvar" );
    mapping.put( RequestKeys.VISITOR_VISITS, "_idvc" );
    mapping.put( RequestKeys.VISITOR_PREVIOUS_VISIT, "_viewts" );
    mapping.put( RequestKeys.VISITOR_FIRST_VISIT, "_idts" );
    mapping.put( RequestKeys.VISITOR_CAMPAIGN_NAME, "_rcn" );
    mapping.put( RequestKeys.VISITOR_CAMPAIGN_KEYWORD, "_rck" );
    mapping.put( RequestKeys.VISITOR_RESOLUTION, "res" );
    mapping.put( RequestKeys.VISITOR_HOUR, "h" );
    mapping.put( RequestKeys.VISITOR_MINUTE, "m" );
    mapping.put( RequestKeys.VISITOR_SECOND, "s" );
    mapping.put( RequestKeys.VISITOR_USER_AGENT_OVERRIDE, "ua" );
    mapping.put( RequestKeys.VISITOR_LANGUAGE_OVERRIDE, "lang" );
    mapping.put( RequestKeys.ACTION_CUSTOM_VARIABLES, "cvar" );
    mapping.put( RequestKeys.ACTION_OUTLINK, "link" );
    mapping.put( RequestKeys.ACTION_DOWNLOAD, "download" );
    mapping.put( RequestKeys.ACTION_SEARCH, "search" );
    mapping.put( RequestKeys.ACTION_SEARCH_CATEGORY, "search_cat" );
    mapping.put( RequestKeys.ACTION_SEARCH_COUNT, "search_count" );
    mapping.put( RequestKeys.ACTION_GOAL_ID, "idgoal" );
    mapping.put( RequestKeys.ACTION_GOAL_REVENUE, "revenue" );
    mapping.put( RequestKeys.ACTION_GENERATION_TIME, "gt_ms" );
    mapping.put( RequestKeys.ECOMMERCE_ORDER_ID, "ec_id" );
    mapping.put( RequestKeys.ECOMMERCE_SUB_TOTAL, "ec_st" );
    mapping.put( RequestKeys.ECOMMERCE_TAX_AMOUNT, "ec_tx" );
    mapping.put( RequestKeys.ECOMMERCE_SHIPPING_COST, "ec_sh" );
    mapping.put( RequestKeys.ECOMMERCE_DISCOUNT, "ec_dt" );
    mapping.put( RequestKeys.ECOMMERCE_ITEMS, "ec_items" );
    mapping.put( RequestKeys.TOKEN_AUTH, "token_auth" );
    mapping.put( RequestKeys.VISITOR_IP_OVERRIDE, "cip" );
    mapping.put( RequestKeys.VISITOR_DATETIME_OVERRIDE, "cdt" );
    mapping.put( RequestKeys.VISITOR_ID_ENFORCED, "cid" );
    mapping.put( RequestKeys.NEW_VISIT, "new_visit" );
    mapping.put( RequestKeys.VISITOR_COUNTRY_OVERRIDE, "country" );
    mapping.put( RequestKeys.VISITOR_REGION_OVERRIDE, "region" );
    mapping.put( RequestKeys.VISITOR_CITY_OVERRIDE, "city" );
    mapping.put( RequestKeys.VISITOR_LATITUDE_OVERRIDE, "lat" );
    mapping.put( RequestKeys.VISITOR_LONGITUDE_OVERRIDE, "long" );
    mapping.put( RequestKeys.IS_BOT, "is_bot" );
    requestKeys = Collections.unmodifiableMap( mapping );
  }

  public static String getRequestKey( String name ) {
    return requestKeys.get( name );
  }

  private RequestKeyProvider() {
    //prevent instantiation
  }
}

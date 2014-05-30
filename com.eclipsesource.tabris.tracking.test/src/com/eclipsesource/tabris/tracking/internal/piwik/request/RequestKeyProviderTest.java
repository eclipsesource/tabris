/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.request;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.requestKeys;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class RequestKeyProviderTest {

  @Test
  public void testGetRequestKeyReturnsRequestKey() {
    String requestKey = RequestKeyProvider.getRequestKey( RequestKeys.ACTION_NAME );
    assertEquals( "action_name", requestKey );
  }

  @Test
  public void testStoresSiteId() {
    assertEquals( "idsite", requestKeys.get( "SITE_ID" ) );
  }

  @Test
  public void testStoresRec() {
    assertEquals( "rec", requestKeys.get( "REC" ) );
  }

  @Test
  public void testStoresUrl() {
    assertEquals( "url", requestKeys.get( "ACTION_URL" ) );
  }

  @Test
  public void testStoresActionName() {
    assertEquals( "action_name", requestKeys.get( "ACTION_NAME" ) );
  }

  @Test
  public void testStoresVisitorId() {
    assertEquals( "_id", requestKeys.get( "VISITOR_ID" ) );
  }

  @Test
  public void testStoresRandom() {
    assertEquals( "rand", requestKeys.get( "RANDOM" ) );
  }

  @Test
  public void testStoresApiVersion() {
    assertEquals( "apiv", requestKeys.get( "API_VERSION" ) );
  }

  @Test
  public void testStoresVisitorReferrerUrl() {
    assertEquals( "urlref", requestKeys.get( "VISITOR_REFERRER_URL" ) );
  }

  @Test
  public void testStoresVisitorCustomVariables() {
    assertEquals( "_cvar", requestKeys.get( "VISITOR_CUSTOM_VARIABLES" ) );
  }

  @Test
  public void testStoresVisitorVisits() {
    assertEquals( "_idvc", requestKeys.get( "VISITOR_VISITS" ) );
  }

  @Test
  public void testStoresVisitorPreviousVisit() {
    assertEquals( "_viewts", requestKeys.get( "VISITOR_PREVIOUS_VISIT" ) );
  }

  @Test
  public void testStoresVisitorFirstVisit() {
    assertEquals( "_idts", requestKeys.get( "VISITOR_FIRST_VISIT" ) );
  }

  @Test
  public void testStoresVisitorCampaignName() {
    assertEquals( "_rcn", requestKeys.get( "VISITOR_CAMPAIGN_NAME" ) );
  }

  @Test
  public void testStoresVisitorCampaignKeyword() {
    assertEquals( "_rck", requestKeys.get( "VISITOR_CAMPAIGN_KEYWORD" ) );
  }

  @Test
  public void testStoresVisitorResolution() {
    assertEquals( "res", requestKeys.get( "VISITOR_RESOLUTION" ) );
  }

  @Test
  public void testStoresVisitorHour() {
    assertEquals( "h", requestKeys.get( "VISITOR_HOUR" ) );
  }

  @Test
  public void testStoresVisitorMinute() {
    assertEquals( "m", requestKeys.get( "VISITOR_MINUTE" ) );
  }

  @Test
  public void testStoresVisitorSecond() {
    assertEquals( "s", requestKeys.get( "VISITOR_SECOND" ) );
  }

  @Test
  public void testStoresVisitorUserAgentOverride() {
    assertEquals( "ua", requestKeys.get( "VISITOR_USER_AGENT_OVERRIDE" ) );
  }

  @Test
  public void testStoresVisitorLanguageOverride() {
    assertEquals( "lang", requestKeys.get( "VISITOR_LANGUAGE_OVERRIDE" ) );
  }

  @Test
  public void testStoresActionCustomVariables() {
    assertEquals( "cvar", requestKeys.get( "ACTION_CUSTOM_VARIABLES" ) );
  }

  @Test
  public void testStoresActionSearch() {
    assertEquals( "search", requestKeys.get( "ACTION_SEARCH" ) );
  }

  @Test
  public void testStoresActionSearchCategory() {
    assertEquals( "search_cat", requestKeys.get( "ACTION_SEARCH_CATEGORY" ) );
  }

  @Test
  public void testStoresActionSearchCount() {
    assertEquals( "search_count", requestKeys.get( "ACTION_SEARCH_COUNT" ) );
  }

  @Test
  public void testStoresActionGoalId() {
    assertEquals( "idgoal", requestKeys.get( "ACTION_GOAL_ID" ) );
  }

  @Test
  public void testStoresActionGoalRevenue() {
    assertEquals( "revenue", requestKeys.get( "ACTION_GOAL_REVENUE" ) );
  }

  @Test
  public void testStoresActionGenerationTime() {
    assertEquals( "gt_ms", requestKeys.get( "ACTION_GENERATION_TIME" ) );
  }

  @Test
  public void testStoresEcommerceOrderId() {
    assertEquals( "ec_id", requestKeys.get( "ECOMMERCE_ORDER_ID" ) );
  }

  @Test
  public void testStoresEcommerceRevenue() {
    assertEquals( "revenue", requestKeys.get( "ECOMMERCE_ORDER_TOTAL" ) );
  }

  @Test
  public void testStoresEcommerceSubtotal() {
    assertEquals( "ec_st", requestKeys.get( "ECOMMERCE_ORDER_SUBTOTAL" ) );
  }

  @Test
  public void testStoresEcommerceTaxAmount() {
    assertEquals( "ec_tx", requestKeys.get( "ECOMMERCE_ORDER_TAX" ) );
  }

  @Test
  public void testStoresEcommerceShippingCost() {
    assertEquals( "ec_sh", requestKeys.get( "ECOMMERCE_ORDER_SHIPPING" ) );
  }

  @Test
  public void testStoresEcommerceDiscount() {
    assertEquals( "ec_dt", requestKeys.get( "ECOMMERCE_ORDER_DISCOUNT" ) );
  }

  @Test
  public void testStoresEcommerceItems() {
    assertEquals( "ec_items", requestKeys.get( "ECOMMERCE_ORDER_ITEMS" ) );
  }

  @Test
  public void testStoresTokenAuth() {
    assertEquals( "token_auth", requestKeys.get( "TOKEN_AUTH" ) );
  }

  @Test
  public void testStoresVisitorIpOverride() {
    assertEquals( "cip", requestKeys.get( "VISITOR_IP_OVERRIDE" ) );
  }

  @Test
  public void testStoresVisitorDatetimeOverride() {
    assertEquals( "cdt", requestKeys.get( "VISITOR_DATETIME_OVERRIDE" ) );
  }

  @Test
  public void testStoresVisitorIdEnforced() {
    assertEquals( "cid", requestKeys.get( "VISITOR_ID_ENFORCED" ) );
  }

  @Test
  public void testStoresNewVisit() {
    assertEquals( "new_visit", requestKeys.get( "NEW_VISIT" ) );
  }

  @Test
  public void testStoresVisitorCountryOverride() {
    assertEquals( "country", requestKeys.get( "VISITOR_COUNTRY_OVERRIDE" ) );
  }

  @Test
  public void testStoresVisitorRegionOverride() {
    assertEquals( "region", requestKeys.get( "VISITOR_REGION_OVERRIDE" ) );
  }

  @Test
  public void testStoresVisitorCityOverride() {
    assertEquals( "city", requestKeys.get( "VISITOR_CITY_OVERRIDE" ) );
  }

  @Test
  public void testStoresVisitorLatitudeOverride() {
    assertEquals( "lat", requestKeys.get( "VISITOR_LATITUDE_OVERRIDE" ) );
  }

  @Test
  public void testStoresVisitorLongituteOverride() {
    assertEquals( "long", requestKeys.get( "VISITOR_LONGITUDE_OVERRIDE" ) );
  }

  @Test
  public void testStoresBots() {
    assertEquals( "is_bot", requestKeys.get( "IS_BOT" ) );
  }
}

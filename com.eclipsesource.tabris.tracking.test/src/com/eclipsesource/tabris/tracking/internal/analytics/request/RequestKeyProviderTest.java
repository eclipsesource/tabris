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

import static com.eclipsesource.tabris.tracking.internal.analytics.request.RequestKeyProvider.requestNames;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RequestKeyProviderTest {

  @Test
  public void testGetRequestKeyReturnsRequestAttribute() {
    String ra = RequestKeyProvider.getRequestKey( RequestKeys.APP_NAME );

    assertEquals( "an", ra );
  }

  @Test
  public void testGetRequestKeyReturnsRequestAttributeWithIndex() {
    String ra = RequestKeyProvider.getRequestKey( RequestKeys.CUSTOM_DIMENSION, 5 );

    assertEquals( "cd5", ra );
  }

  @Test
  public void testStoresVersion() {
    assertEquals( "v", requestNames.get( "VERSION" ) );
  }

  @Test
  public void testStoresTrackingId() {
    assertEquals( "tid", requestNames.get( "TRACKING_ID" ) );
  }

  @Test
  public void testStoresClientId() {
    assertEquals( "cid", requestNames.get( "CLIENT_ID" ) );
  }

  @Test
  public void testStoresHit() {
    assertEquals( "t", requestNames.get( "HIT_TYPE" ) );
  }

  @Test
  public void testStoresAppName() {
    assertEquals( "an", requestNames.get( "APP_NAME" ) );
  }

  @Test
  public void testStoresAppId() {
    assertEquals( "aid", requestNames.get( "APP_ID" ) );
  }

  @Test
  public void testStoresAppVersion() {
    assertEquals( "av", requestNames.get( "APP_VERSION" ) );
  }

  @Test
  public void testStoresScreenName() {
    assertEquals( "cd", requestNames.get( "SCREEN_NAME" ) );
  }

  @Test
  public void testStoresUserId() {
    assertEquals( "uid", requestNames.get( "USER_ID" ) );
  }

  @Test
  public void testStoresSessionControl() {
    assertEquals( "sc", requestNames.get( "SESSION_CONTROL" ) );
  }

  @Test
  public void testStoresIpOverride() {
    assertEquals( "uip", requestNames.get( "IP_OVERRIDE" ) );
  }

  @Test
  public void testStoresUserAgent() {
    assertEquals( "ua", requestNames.get( "USER_AGENT_OVERRIDE" ) );
  }

  @Test
  public void testStoresScreenResolution() {
    assertEquals( "sr", requestNames.get( "SCREEN_RESOLUTION" ) );
  }

  @Test
  public void testStoresViewportSize() {
    assertEquals( "vp", requestNames.get( "VIEWPORT_SIZE" ) );
  }

  @Test
  public void testStoresUserLanguage() {
    assertEquals( "ul", requestNames.get( "USER_LANGUAGE" ) );
  }

  @Test
  public void testStoresDocumentHostName() {
    assertEquals( "dh", requestNames.get( "DOCUMENT_HOST_NAME" ) );
  }

  @Test
  public void testStoresDocumentPath() {
    assertEquals( "dp", requestNames.get( "DOCUMENT_PATH" ) );
  }

  @Test
  public void testStoresDocumentTitle() {
    assertEquals( "dt", requestNames.get( "DOCUMENT_TITLE" ) );
  }

  @Test
  public void testStoresEventCategory() {
    assertEquals( "ec", requestNames.get( "EVENT_CATEGORY" ) );
  }

  @Test
  public void testStoresEventAction() {
    assertEquals( "ea", requestNames.get( "EVENT_ACTION" ) );
  }

  @Test
  public void testStoresEventLabel() {
    assertEquals( "el", requestNames.get( "EVENT_LABEL" ) );
  }

  @Test
  public void testStoresEventValue() {
    assertEquals( "ev", requestNames.get( "EVENT_VALUE" ) );
  }

  @Test
  public void testStoresTransactionId() {
    assertEquals( "ti", requestNames.get( "TRANSACTION_ID" ) );
  }

  @Test
  public void testStoresTransactionAffiliation() {
    assertEquals( "ta", requestNames.get( "TRANSACTION_AFFILIATION" ) );
  }

  @Test
  public void testStoresTransactionRevenue() {
    assertEquals( "tr", requestNames.get( "TRANSACTION_REVENUE" ) );
  }

  @Test
  public void testStoresTransactionShipping() {
    assertEquals( "ts", requestNames.get( "TRANSACTION_SHIPPING" ) );
  }

  @Test
  public void testStoresTransactionTax() {
    assertEquals( "tt", requestNames.get( "TRANSACTION_TAX" ) );
  }

  @Test
  public void testStoresCurrencyCode() {
    assertEquals( "cu", requestNames.get( "CURRENCY_CODE" ) );
  }

  @Test
  public void testStoresItemName() {
    assertEquals( "in", requestNames.get( "ITEM_NAME" ) );
  }

  @Test
  public void testStoresItemPrice() {
    assertEquals( "ip", requestNames.get( "ITEM_PRICE" ) );
  }

  @Test
  public void testStoresItemQuantity() {
    assertEquals( "iq", requestNames.get( "ITEM_QUANTITY" ) );
  }

  @Test
  public void testStoresItemCode() {
    assertEquals( "ic", requestNames.get( "ITEM_CODE" ) );
  }

  @Test
  public void testStoresItemCategory() {
    assertEquals( "iv", requestNames.get( "ITEM_CATEGORY" ) );
  }

  @Test
  public void testStoresSocialNetwork() {
    assertEquals( "sn", requestNames.get( "SOCIAL_NETWORK" ) );
  }

  @Test
  public void testStoresSocialAction() {
    assertEquals( "sa", requestNames.get( "SOCIAL_ACTION" ) );
  }

  @Test
  public void testStoresSocialActionTarget() {
    assertEquals( "st", requestNames.get( "SOCIAL_ACTION_TARGET" ) );
  }

  @Test
  public void testStoresExceptionDescription() {
    assertEquals( "exd", requestNames.get( "EXCEPTION_DESCRIPTION" ) );
  }

  @Test
  public void testStoresExceptionFatal() {
    assertEquals( "exf", requestNames.get( "EXCEPTION_FATAL" ) );
  }

  @Test
  public void testStoresUserTimingCategory() {
    assertEquals( "utc", requestNames.get( "USER_TIMING_CATEGORY" ) );
  }

  @Test
  public void testStoresUserTimingVariableName() {
    assertEquals( "utv", requestNames.get( "USER_TIMING_VARIABLE_NAME" ) );
  }

  @Test
  public void testStoresUserTimingTime() {
    assertEquals( "utt", requestNames.get( "USER_TIMING_TIME" ) );
  }

  @Test
  public void testStoresUserTimingLabel() {
    assertEquals( "utl", requestNames.get( "USER_TIMING_LABEL" ) );
  }

  @Test
  public void testStoresPageLoadTime() {
    assertEquals( "plt", requestNames.get( "PAGE_LOAD_TIME" ) );
  }

  @Test
  public void testStoresDnsTime() {
    assertEquals( "dns", requestNames.get( "DNS_TIME" ) );
  }

  @Test
  public void testStoresPageDownloadTime() {
    assertEquals( "pdt", requestNames.get( "PAGE_DOWNLOAD_TIME" ) );
  }

  @Test
  public void testStoresRedirectResponseTime() {
    assertEquals( "rrt", requestNames.get( "REDIRECT_RESPONSE_TIME" ) );
  }

  @Test
  public void testStoresTcpConnectTime() {
    assertEquals( "tcp", requestNames.get( "TCP_CONNECT_TIME" ) );
  }

  @Test
  public void testStoresServerResponseTime() {
    assertEquals( "srt", requestNames.get( "SERVER_RESPONSE_TIME" ) );
  }

  @Test
  public void testStoresCustomDimension() {
    assertEquals( "cd", requestNames.get( "CUSTOM_DIMENSION" ) );
  }

  @Test
  public void testStoresCustomMetric() {
    assertEquals( "cm", requestNames.get( "CUSTOM_METRIC" ) );
  }
}

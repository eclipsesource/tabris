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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestKeyProvider implements Serializable {

  static Map<String, String> requestNames;

  static {
    Map<String, String> mapping = new HashMap<String, String>();
    mapping.put( RequestKeys.VERSION, "v" );
    mapping.put( RequestKeys.TRACKING_ID, "tid" );
    mapping.put( RequestKeys.CLIENT_ID, "cid" );
    mapping.put( RequestKeys.HIT_TYPE, "t" );
    mapping.put( RequestKeys.APP_NAME, "an" );
    mapping.put( RequestKeys.APP_ID, "aid" );
    mapping.put( RequestKeys.APP_VERSION, "av" );
    mapping.put( RequestKeys.CONTENT_DESCRIPTION, "cd" );
    mapping.put( RequestKeys.CURRENCY_CODE, "cu" );
    mapping.put( RequestKeys.CUSTOM_DIMENSION, "cd" );
    mapping.put( RequestKeys.CUSTOM_METRIC, "cm" );
    mapping.put( RequestKeys.DOCUMENT_HOST_NAME, "dh" );
    mapping.put( RequestKeys.DOCUMENT_PATH, "dp" );
    mapping.put( RequestKeys.DOCUMENT_TITLE, "dt" );
    mapping.put( RequestKeys.DNS_TIME, "dns" );
    mapping.put( RequestKeys.EVENT_CATEGORY, "ec" );
    mapping.put( RequestKeys.EVENT_ACTION, "ea" );
    mapping.put( RequestKeys.EVENT_LABEL, "el" );
    mapping.put( RequestKeys.EVENT_VALUE, "ev" );
    mapping.put( RequestKeys.EXCEPTION_DESCRIPTION, "exd" );
    mapping.put( RequestKeys.EXCEPTION_FATAL, "exf" );
    mapping.put( RequestKeys.ITEM_NAME, "in" );
    mapping.put( RequestKeys.ITEM_PRICE, "ip" );
    mapping.put( RequestKeys.ITEM_QUANTITY, "iq" );
    mapping.put( RequestKeys.ITEM_CODE, "ic" );
    mapping.put( RequestKeys.ITEM_CATEGORY, "iv" );
    mapping.put( RequestKeys.USER_ID, "uid" );
    mapping.put( RequestKeys.PAGE_LOAD_TIME, "plt" );
    mapping.put( RequestKeys.PAGE_DOWNLOAD_TIME, "pdt" );
    mapping.put( RequestKeys.REDIRECT_RESPONSE_TIME, "rrt" );
    mapping.put( RequestKeys.SERVER_RESPONSE_TIME, "srt" );
    mapping.put( RequestKeys.SESSION_CONTROL, "sc" );
    mapping.put( RequestKeys.SOCIAL_NETWORK, "sn" );
    mapping.put( RequestKeys.SOCIAL_ACTION, "sa" );
    mapping.put( RequestKeys.SOCIAL_ACTION_TARGET, "st" );
    mapping.put( RequestKeys.TCP_CONNECT_TIME, "tcp" );
    mapping.put( RequestKeys.TRANSACTION_ID, "ti" );
    mapping.put( RequestKeys.TRANSACTION_AFFILIATION, "ta" );
    mapping.put( RequestKeys.TRANSACTION_REVENUE, "tr" );
    mapping.put( RequestKeys.TRANSACTION_SHIPPING, "ts" );
    mapping.put( RequestKeys.TRANSACTION_TAX, "tt" );
    mapping.put( RequestKeys.USER_TIMING_CATEGORY, "utc" );
    mapping.put( RequestKeys.USER_TIMING_VARIABLE_NAME, "utv" );
    mapping.put( RequestKeys.USER_TIMING_TIME, "utt" );
    mapping.put( RequestKeys.USER_TIMING_LABEL, "utl" );
    mapping.put( RequestKeys.IP_OVERRIDE, "uip" );
    mapping.put( RequestKeys.USER_AGENT_OVERRIDE, "ua" );
    mapping.put( RequestKeys.SCREEN_RESOLUTION, "sr" );
    mapping.put( RequestKeys.VIEWPORT_SIZE, "vp" );
    mapping.put( RequestKeys.USER_LANGUAGE, "ul" );
    requestNames = Collections.unmodifiableMap( mapping );
  }

  public static String getRequestKey( String name ) {
    return requestNames.get( name );
  }

  public static String getRequestKey( String name, int index ) {
    return requestNames.get( name ) + String.valueOf( index );
  }
}

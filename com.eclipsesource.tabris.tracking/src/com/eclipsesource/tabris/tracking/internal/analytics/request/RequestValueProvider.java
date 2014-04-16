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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestValueProvider {

  static Map<String, String> values;

  static {
    Map<String, String> map = new HashMap<String, String>();
    map.put( RequestValues.HIT_APPVIEW, "appview" );
    map.put( RequestValues.HIT_PAGEVIEW, "pageview" );
    map.put( RequestValues.HIT_TRANSACTION, "transaction" );
    map.put( RequestValues.HIT_ITEM, "item" );
    map.put( RequestValues.HIT_EVENT, "event" );
    map.put( RequestValues.HIT_EXCEPTION, "exception" );
    map.put( RequestValues.HIT_SOCIAL, "social" );
    map.put( RequestValues.HIT_TIMING, "timing" );
    map.put( RequestValues.SESSION_CONTROL_START, "start" );
    map.put( RequestValues.SESSION_CONTROL_END, "end" );
    values = Collections.unmodifiableMap( map );
  }

  public static String getRequestValue( String requestValue ) {
    return values.get( requestValue );
  }
}

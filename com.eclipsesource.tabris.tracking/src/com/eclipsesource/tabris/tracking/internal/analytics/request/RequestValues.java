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

public class RequestValues {

  public static final String HIT_APPVIEW = "HIT_APPVIEW";
  public static final String HIT_PAGEVIEW = "HIT_PAGEVIEW";
  public static final String HIT_TRANSACTION = "HIT_TRANSACTION";
  public static final String HIT_EVENT = "HIT_EVENT";
  public static final String HIT_EXCEPTION = "HIT_EXCEPTION";
  public static final String HIT_TIMING = "HIT_TIMING";
  public static final String HIT_SOCIAL = "HIT_SOCIAL";
  public static final String SESSION_CONTROL_START = "SESSION_CONTROL_START";
  public static final String SESSION_CONTROL_END = "SESSION_CONTROL_END";
  public static final String HIT_ITEM = "HIT_ITEM";

  private RequestValues() {
    // prevent instantiation
  }
}

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
package com.eclipsesource.tabris.interaction;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Generic launch options. Not intended to be used directly.
 * <p>
 *
 * @see BrowserOptions
 * @see MailOptions
 * @see MapsOptions
 * @see PhoneOptions
 * @see SMSOptions
 * @see TwitterOptions
 * @see FacebookOptions
 *
 * @since 0.9
 */
public class LaunchOptions implements Serializable {

  public static enum App {
    MAIL, BROWSER, MAPS, PHONE, SMS, TWITTER, FACEBOOK
  }

  private final Map<String, Object> options;
  private final App app;

  LaunchOptions( App app ) {
    whenNull( app ).throwIllegalArgument( "App must not be null" );
    this.app = app;
    this.options = new HashMap<String, Object>();
  }

  public App getApp() {
    return app;
  }

  public void add( String name, Object value ) {
    options.put( name, value );
  }

  public Map<String, Object> getOptions() {
    return new HashMap<String, Object>( options );
  }

}

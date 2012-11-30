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

import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNull;

import java.util.HashMap;
import java.util.Map;


/**
 * @since 0.9
 */
public class LaunchOptions {
  
  public enum App {
    MAIL, BROWSER, MAPS, PHONE, SMS
  }

  private final Map<String, String> options;
  private final App app;

  LaunchOptions( App app ) {
    argumentNotNull( app, "App" );
    this.app = app;
    this.options = new HashMap<String, String>();
  }
  
  public App getApp() {
    return app;
  }
  
  public void add( String name, String value ) {
    options.put( name, value );
  }
  
  public Map<String, String> getOptions() {
    return new HashMap<String, String>( options );
  }
  
}

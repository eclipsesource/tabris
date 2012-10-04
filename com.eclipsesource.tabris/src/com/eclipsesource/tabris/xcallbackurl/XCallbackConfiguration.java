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
package com.eclipsesource.tabris.xcallbackurl;

import java.util.HashMap;
import java.util.Map;


/**
 * @since 0.8
 */
public class XCallbackConfiguration {
  
  private final String targetApp;
  private final String action;
  private final String xSource;
  private final Map<String, String> actionParameters;
  
  public XCallbackConfiguration( String targetApp, String action, String xSource ) {
    checkArguments( targetApp, action, xSource );
    this.targetApp = targetApp;
    this.action = action;
    this.xSource = xSource;
    this.actionParameters = new HashMap<String, String>();
  }
  
  private void checkArguments( String targetApp, String action, String xSource ) {
    if( targetApp == null ) {
      throw new IllegalArgumentException( "targetApp must not be null" );
    }
    if( action == null ) {
      throw new IllegalArgumentException( "action must not be null" );
    }
    if( xSource == null ) {
      throw new IllegalArgumentException( "xSource must not be null" );
    }
  }

  public void addActionParameter( String name, String value ) {
    actionParameters.put( name, value );
  }
  
  public Map<String, String> getActionParameters() {
    return new HashMap<String, String>( actionParameters );
  }

  public String getTargetApp() {
    return targetApp;
  }

  public String getAction() {
    return action;
  }

  public String getXSource() {
    return xSource;
  }
}

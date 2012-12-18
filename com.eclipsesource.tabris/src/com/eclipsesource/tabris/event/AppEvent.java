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
package com.eclipsesource.tabris.event;

import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNull;

import java.util.Map;



public class AppEvent {
  
  private final EventType type;
  private final Map<String, Object> properties;

  public AppEvent( EventType type, Map<String, Object> properties ) {
    argumentNotNull( type, "EventType" );
    this.type = type;
    this.properties = properties;
  }
  
  public EventType getType() {
    return type;
  }
  
  public Object getProperty( String name ) {
    return properties.get( name );
  }
  
}

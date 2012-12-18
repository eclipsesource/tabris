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

public enum EventType {
  
  PAUSE( "Pause" ), 
  RESUME( "Resume" );
  
  private final String name;

  EventType( String name ) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  
  public static EventType fromName( String name ) {
    EventType[] values = values();
    for( EventType eventType : values ) {
      if( eventType.getName().equals( name ) ) {
        return eventType;
      }
    }
    return null;
  }
  
}
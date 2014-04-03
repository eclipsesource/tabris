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
package com.eclipsesource.tabris.app;

/**
 * <p>
 * The {@link EventType} is used to specify different types of events. An App can fire events for different reasons
 * e.g. pause, resume, inactivity and more.
 * </p>
 *
 * @see AppEvent
 *
 * @since 0.10
 */
public enum EventType {

  /**
   * <p>
   * When an App was opened externally an Event of the type {@link EventType#OPEN} will be fired. The URL used to open
   * the app can be obtained from {@link App#getOpenUrl()}.
   * </p>
   *
   * @since 1.4
   */
  OPEN( "Open" ),

  /**
   * <p>
   * When an App was sent to the background a {@link EventType#PAUSE} event will be fired.
   * </p>
   */
  PAUSE( "Pause" ),

  /**
   * <p>
   * When an App was paused and becomes active again a {@link EventType#RESUME} event will be fired. The resume
   * arguments will be passed as properties to the fired {@link AppEvent}.
   * </p>
   */
  RESUME( "Resume" ),

  /**
   * <p>
   * When the user has not interacted with the App for a specific time an {@link EventType#INACTIVE} event will be
   * fired. The time that needs to pass can be specified using {@link App#startInactivityTimer(int)}.
   * </p>
   *
   * @since 1.1
   */
  INACTIVE( "Inactive" );

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
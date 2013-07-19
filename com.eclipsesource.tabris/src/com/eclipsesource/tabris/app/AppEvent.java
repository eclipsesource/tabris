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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.io.Serializable;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;


/**
 * <p>
 * The {@link AppEvent} represents an event instance of a specific {@link EventType}. Usually an {@link AppEvent} will
 * be passed as a parameter when notifying a {@link AppListener}.
 * </p>
 *
 * @see AppListener
 *
 * @since 0.10
 */
public class AppEvent implements Serializable {

  private final EventType type;
  private final JsonObject properties;

  /**
   * <p>
   * Creates a new {@link AppEvent}.
   * </p>
   *
   * @since 1.1
   */
  public AppEvent( EventType type, JsonObject properties ) {
    whenNull( type ).throwIllegalArgument( "EventType must not be null" );
    this.type = type;
    this.properties = properties;
  }

  /**
   * <p>
   * Returns the {@link EventType} of this {@link AppEvent}.
   * </p>
   */
  public EventType getType() {
    return type;
  }

  /**
   * <p>
   * Returns a property of the {@link AppEvent}. If the event has properties or not depends on its {@link EventType}.
   * </p>
   *
   * @since 1.1
   */
  public JsonValue getProperty( String name ) {
    JsonValue result = null;
    if( properties != null ) {
      result = properties.get( name );
    }
    return result;
  }

}

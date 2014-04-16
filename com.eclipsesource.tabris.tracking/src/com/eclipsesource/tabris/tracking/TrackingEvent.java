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
package com.eclipsesource.tabris.tracking;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;


/**
 * @since 1.4
 */
@SuppressWarnings("restriction")
public class TrackingEvent {

  public static enum EventType {
    PAGE_VIEW, ACTION, SEARCH
  }

  private final EventType type;
  private final TrackingInfo info;
  private final Object detail;
  private final long timestamp;

  public TrackingEvent( EventType type, TrackingInfo info, Object detail, long timestamp ) {
    validateArguments( type, info, detail, timestamp );
    this.type = type;
    this.info = info;
    this.detail = detail;
    this.timestamp = timestamp;
  }

  private void validateArguments( EventType type, TrackingInfo info, Object detail, long timestamp ) {
    whenNull( type ).throwIllegalArgument( "EventType must not be null." );
    whenNull( info ).throwIllegalArgument( "TrackingInfo must not be null." );
    whenNull( detail ).throwIllegalArgument( "Detail must not be null." );
    when( timestamp < 0 ).throwIllegalArgument( "Timestamp must be > 0 but was " + timestamp );
  }

  public EventType getType() {
    return type;
  }

  public TrackingInfo getInfo() {
    return info;
  }

  public Object getDetail() {
    return detail;
  }

  public long getTimestamp() {
    return timestamp;
  }

}

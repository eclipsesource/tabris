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
package com.eclipsesource.tabris.tracking.internal;

import static com.eclipsesource.tabris.internal.Clauses.whenNull;

import java.util.List;

import com.eclipsesource.tabris.tracking.Tracker;
import com.eclipsesource.tabris.tracking.TrackingEvent;


@SuppressWarnings("restriction")
public class DispatchTask {

  private final List<Tracker> trackers;
  private final TrackingEvent event;

  public DispatchTask( List<Tracker> trackers, TrackingEvent event ) {
    whenNull( trackers ).throwIllegalArgument( "Trackers must not be null" );
    whenNull( event ).throwIllegalArgument( "TrackingEvent must not be null" );
    this.trackers = trackers;
    this.event = event;
  }

  public List<Tracker> getTrackers() {
    return trackers;
  }

  public TrackingEvent getEvent() {
    return event;
  }
}

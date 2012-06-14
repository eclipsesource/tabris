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
package com.eclipsesource.tabris.geolocation;

import java.util.Date;


public class Position {
  
  private final Coordinates coords;
  private final Date timestamp;

  public Position( Coordinates coords, Date timestamp ) {
    this.coords = coords;
    this.timestamp = timestamp;
  }

  public Coordinates getCoords() {
    return coords;
  }
  
  public Date getTimestamp() {
    return timestamp;
  }
  
}

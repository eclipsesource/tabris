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

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * A <code>Position</code> will be passed as parameter when a client sends it's location to the server. See
 * <code>GelocationCallback#onSuccess</code>. A <code>Position</code> object is a combination of
 * <code>Coordinates</code> and a timestamp.
 * </p>
 *
 * @see GeolocationListener
 * @since 0.6
 */
public class Position implements Serializable {

  private final Coordinates coords;
  private final Date timestamp;

  public Position( Coordinates coords, Date timestamp ) {
    this.coords = coords;
    this.timestamp = new Date( timestamp.getTime() );
  }

  /**
   * <p>
   * Returns the <code>Coordinates</code> of thsi position object.
   * </p>
   */
  public Coordinates getCoords() {
    return coords;
  }

  /**
   * <p>
   * Returns the <code>Date</code> timestamp when this position was determined.
   * </p>
   */
  public Date getTimestamp() {
    return new Date( timestamp.getTime() );
  }

}

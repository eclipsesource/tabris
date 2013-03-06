/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.geolocation;


/**
 * <p>
 * This adapter provides an empty default implementation of {@link GeolocationListener}.
 * </p>
 *
 * @see GeolocationListener
 * @since 1.0
 */
public class GeolocationAdapter implements GeolocationListener {

  @Override
  public void positionReceived( Position position ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void errorReceived( PositionError error ) {
    // intended to be implemented by subclasses.
  }
}

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


/**
 * <p>
 * A <code>PositionError</code> will be used as a parameter when the client fails to determine the location. It will
 * be passed as a parameter to <code>GeolocationCallback#onError</code>.
 * </p>
 *
 * @see GeolocationListener
 * @since 0.6
 */
public class PositionError implements Serializable {

  /**
   * <p>
   * <code>PositionErrorCode</code> is a helper enumeration to unify the most common client errors.
   * </p>
   */
  public static enum PositionErrorCode {
    /**
     * <p>
     * Means that the client app had not the sufficient permissions to determine the location. When the user doesn't
     * allow to use the device location this error occurs.
     * </p>
     */
    PERMISSION_DENIED,

    /**
     * <p>
     * The client could not determine the location, e.g. when the GPS is not activated.
     * </p>
     */
    POSITION_UNAVAILABLE,

    /**
     * <p>
     * An unknown error occured.
     * </p>
     */
    UNKNOWN;
  }

  private final PositionErrorCode code;
  private final String message;

  public PositionError( PositionErrorCode code, String message ) {
    this.code = code;
    this.message = message;
  }

  /**
   * <p>
   * Returns the error message passed from the mobile device.
   * </p>
   */
  public String getMessage() {
    return message;
  }

  /**
   * <p>
   * Returns the error code.
   * </p>
   *
   * @see PositionErrorCode
   */
  public PositionErrorCode getCode() {
    return code;
  }

}

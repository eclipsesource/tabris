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
package com.eclipsesource.rap.mobile.geolocation;


public class PositionError {
  
  public enum PositionErrorCode {
    PERMISSION_DENIED, POSITION_UNAVAILABLE, UNKNOWN;
  }
  
  private final PositionErrorCode code;
  private final String message;
  
  public PositionError( PositionErrorCode code, String message ) {
    this.code = code;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public PositionErrorCode getCode() {
    return code;
  }
  
}

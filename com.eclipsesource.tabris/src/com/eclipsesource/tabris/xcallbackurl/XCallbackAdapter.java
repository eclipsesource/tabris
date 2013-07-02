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
package com.eclipsesource.tabris.xcallbackurl;

import java.util.Map;


/**
 * @since 1.1
 */
public class XCallbackAdapter implements XCallbackListener {

  @Override
  public void onSuccess( Map<String, String> parameters ) {
    // intended to be implemented by subclasses.
  }

  @Override
  public void onCancel() {
    // intended to be implemented by subclasses.
  }

  @Override
  public void onError( int errorCode, String errorMessage ) {
    // intended to be implemented by subclasses.
  }
}

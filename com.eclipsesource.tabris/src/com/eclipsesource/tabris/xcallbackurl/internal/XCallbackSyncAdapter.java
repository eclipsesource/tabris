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
package com.eclipsesource.tabris.xcallbackurl.internal;

import com.eclipsesource.tabris.xcallbackurl.XCallback;


public class XCallbackSyncAdapter {
  
  private boolean wantsToCall;
  private XCallback callback;

  public boolean wantsToCall() {
    return wantsToCall;
  }
  
  public void setWantsToCall( boolean wantsToCall ) {
    this.wantsToCall = wantsToCall;
  }
  
  public void setCallback( XCallback callback ) {
    this.callback = callback;
  }
  
  public XCallback getCallback() {
    return callback;
  }
  
}

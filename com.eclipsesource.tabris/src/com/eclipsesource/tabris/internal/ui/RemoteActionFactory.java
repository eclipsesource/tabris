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
package com.eclipsesource.tabris.internal.ui;

import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.action.SearchAction;


public class RemoteActionFactory {

  public static RemoteAction createRemoteAction( UI ui, ActionDescriptor descriptor, String parent ) {
    if( descriptor.getAction() instanceof SearchAction ) {
      return new RemoteSearchAction( ui, descriptor, parent );
    }
    return new RemoteAction( ui, descriptor, parent );
  }

  private RemoteActionFactory() {
    // prevent instantiation
  }
}

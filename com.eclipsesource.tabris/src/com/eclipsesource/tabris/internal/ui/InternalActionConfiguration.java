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

import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.ActionConfiguration;


public class InternalActionConfiguration extends ActionConfiguration {

  public InternalActionConfiguration( String actionId, Class<? extends Action> actionType ) {
    super( actionId, actionType );
  }

  public ActionDescriptor createDescriptor() {
    Action action = InstanceCreator.createInstance( actionType );
    return new ActionDescriptor( actionId, action, title, image, visible, enabled, prominence );
  }
}

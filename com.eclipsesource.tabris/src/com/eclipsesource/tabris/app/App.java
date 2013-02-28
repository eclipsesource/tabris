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
package com.eclipsesource.tabris.app;

import org.eclipse.rap.rwt.client.service.ClientService;


/**
 * @since 0.10
 */
public interface App extends ClientService {

  void addEventListener( EventType type, AppListener listener );

  void removeEventListener( EventType type, AppListener listener );

  void addBackNavigationListener( BackNavigationListener listener );

  void removeBackNavigationListener( BackNavigationListener listener );

}

/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris;

import org.eclipse.rap.rwt.client.service.ClientService;
import org.eclipse.rap.rwt.service.UISession;


/**
 * <p>
 * A {@link VersionCheck} can be added by calling {@link TabrisClientInstaller#setVersionCheck(VersionCheck)}. It's
 * methods will be called before a {@link UISession} will be created. You can implement the
 * {@link VersionCheck#accept(String, String)} to either accept or decline the requesting client.
 * </p>
 * <p>
 * <b>Please Note:</b> The methods of the {@link VersionCheck} are called before a {@link UISession} exists. This means
 * you won't have access to any {@link ClientService} or other session scoped APIs.
 * </p>
 *
 * @since 1.4
 */
public interface VersionCheck {

  /**
   * <p>
   * Will be called before a {@link UISession} will be created. By returning <code>true</code> the requesting client
   * will be accepted and a {@link UISession} will be created. By return <code>false</code> the requesting client will
   * be declined and received the error message defined in {@link VersionCheck#getErrorMessage(String, String)}.
   * </p>
   *
   * @param clientVersion the Tabris version that requesting client submits.
   * @param serverVersion the Tabris version of the server.
   */
  boolean accept( String clientVersion, String serverVersion );

  /**
   * <p>
   * Will be called when the {@link VersionCheck#accept(String, String)} method declines a client. The returned message
   * will be displayed on the declined client.
   * </p>
   *
   * @param clientVersion the Tabris version that requesting client submits.
   * @param serverVersion the Tabris version of the server.
   */
  String getErrorMessage( String clientVersion, String serverVersion );

}

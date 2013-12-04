/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.interaction;

import org.eclipse.rap.rwt.client.service.ClientService;


/**
 * <p>
 * Can be used to open other Apps on a device. To get an instance of the {@link AppLauncher} use
 * <code>RWT.getClient().getService( AppLauncher.class );</code>.
 * </p>
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * @since 0.9
 */
public interface AppLauncher extends ClientService {

  /**
   * <p>
   * Open a specific App configured in the passed {@link LaunchOptions}. Each App has it's own set of
   * parameters. For this reason a set of more concrete {@link LaunchOptions} objects can be used.
   * </p>
   *
   * @param launchOptions The options to pass over to an App. Must not be <code>null</code>.
   *
   * @see LaunchOptions
   */
  void open( LaunchOptions launchOptions ) throws IllegalArgumentException;

  /**
   * <p>
   * Opens an URL with the best possible App. E.g. it will open a mailto url with the email app.
   * </p>
   *
   * @param url The url to open. Needs to be a valid url and must not be <code>null</code>.
   */
  void openUrl( String url ) throws IllegalArgumentException;
}
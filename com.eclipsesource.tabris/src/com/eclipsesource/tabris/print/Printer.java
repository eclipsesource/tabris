/*******************************************************************************
 * Copyright (c) 2014, 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.print;

import org.eclipse.rap.rwt.client.service.ClientService;

/**
 * <p>
 * The {@link Printer} service can be used to send resources to the client's printer queue.
 * </p>
 * <p>
 * <b>Please Note:</b> Printing is an asynchronous operation. For this reason you need to attach a {@link PrintListener}
 * to the {@link Printer} service to receive print results.
 * </p>
 *
 * @since 1.4
 *
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface Printer extends ClientService {

  /**
   * <p>
   * Sends a print job to the client. The client will be instructed to print the resource defined in the url parameter.
   * </p>
   *
   * @param url the location of the resource to print. Must not be <code>null</code> or empty.
   * @param jobName the name of the printing job
   */
  void print( String url, String jobName );

  /**
   * <p>
   * To get notified when about print results you can add a {@link PrintListener}. The listener will be called when the
   * job succeeded, failed or was canceled.
   * </p>
   *
   * @param listener the listener to add. Must not be <code>null</code>.
   */
  void addPrintListener( PrintListener listener );

  /**
   * <p>
   * Removes a {@link PrintListener}. The removed listener will no longer get print notifications.
   * </p>
   *
   * @param listener the listener to remove. Must not be <code>null</code>.
   */
  void removePrintListener( PrintListener listener );

}
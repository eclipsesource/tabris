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

import java.io.Serializable;
import java.util.Map;


/**
 * <p>
 * The {@link XCallbackListener} will be registered on {@link XCallback} instances. Once the {@link XCallback#call()}
 * method was called and the targeted App calls back the {@link XCallbackListener} methods will be called. The
 * x-callback-url specifications has three possible return values. These are success, error and cancel. For each state
 * the respecting method on the {@link XCallbackListener} will be called.
 * </p>
 *
 * @see XCallback
 *
 * @since 1.1
 */
public interface XCallbackListener extends Serializable {

  /**
   * <p>
   * The {@link XCallbackListener#onSuccess(Map)} will be called when the targeting App has completed it's task
   * successfully. Each target App can pass parameter with its call back. These parameter are passed as method
   * arguments to this method.
   * </p>
   */
  void onSuccess( Map<String, String> parameters );

  /**
   * <p>
   * The {@link XCallbackListener#onCancel()} will be called when the task of the targeting App was canceled.
   * </p>
   */
  void onCancel();

  /**
   * <p>
   * The {@link XCallbackListener#onError(String, String)} will be called when the targeting App has completed it's
   * task with an error.
   * </p>
   *
   * @param errorCode the code passed from the targeting App.
   * @param errorMessage the message passed from the targeting App.
   */
  void onError( String errorCode, String errorMessage );
}
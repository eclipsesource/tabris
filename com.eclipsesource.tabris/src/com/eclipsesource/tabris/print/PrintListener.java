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
package com.eclipsesource.tabris.print;

import java.io.Serializable;


/**
 * <p>
 * A {@link PrintListener} is usually added to the {@link Printer} service to get notification about the print results.
 * </p>
 * <p>
 * <b>Please note:</p> You can also use the {@link PrintAdapter} as a base implementation of your listener.
 * </p>
 *
 * @see Printer
 * @see PrintAdapter
 *
 * @since 1.4
 */
public interface PrintListener extends Serializable {

  /**
   * <p>
   * Will be called when the print job was successful.
   * </p>
   */
  void printSucceeded( String printer, String jobName );

  /**
   * <p>
   * Will be called when the user has canceled the print job.
   * </p>
   */
  void printCanceled( String printer, String jobName );

  /**
   * <p>
   * Will be called when the print job has failed.
   * </p>
   */
  void printFailed( PrintError error );

}

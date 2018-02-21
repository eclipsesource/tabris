/*******************************************************************************
 * Copyright (c) 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets;

import java.io.Serializable;


/**
 * <p>
 * A {@link ScanListener} is usually added to the {@link BarcodeScanner} widget to get notification about the scan results.
 * </p>
 * <p>
 * <b>Please note:</p> You can also use the {@link ScanAdapter} as a base implementation of your listener.
 * </p>
 *
 * @see BarcodeScanner
 * @see ScanAdapter
 *
 * @since 3.4
 */
public interface ScanListener extends Serializable {

  /**
   * <p>
   * Will be called when the barcode scan was successful.
   * </p>
   */
  void scanSucceeded( String format, String data );

  /**
   * <p>
   * Will be called when the barcode scan has failed. After an error occurred
   * no further events will be fired and the widget becomes unusable.
   * </p>
   */
  void scanFailed( String error );

}

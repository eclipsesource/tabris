/*******************************************************************************
 * Copyright (c) 2021 EclipseSource and others.
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
 * A {@link LoadListener} is usually added to the {@link PdfView} widget to get notification about loading
 * of the PDF document.
 * </p>
 *
 * @see PdfView
 *
 * @since 3.17
 */
public interface LoadListener extends Serializable {

  /**
   * <p>
   * Will be called when the PDF document was loaded successful.
   * </p>
   */
  void loadSucceeded();

  /**
   * <p>
   * Will be called when loading of PDF document has failed.
   * </p>
   */
  void loadFailed();

}

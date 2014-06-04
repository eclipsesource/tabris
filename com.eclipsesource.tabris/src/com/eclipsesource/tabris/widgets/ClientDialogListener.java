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
package com.eclipsesource.tabris.widgets;

import java.io.Serializable;


/**
 * <p>
 * A {@link ClientDialogListener} can be added to a {@link ClientDialog} to get notification regarding the life cycle of
 * a dialog.
 * </p>
 *  * <p>
 * <b>Please note:</p> You can also use the {@link ClientDialogAdapter} as a base implementation of your listener.
 * </p>
 *
 * @see ClientDialog
 *
 * @since 1.4
 */
public interface ClientDialogListener extends Serializable {

  /**
   * <p>
   * Will be called when a {@link ClientDialog} was opened.
   * </p>
   */
  void open();

  /**
   * <p>
   * Will be called when a {@link ClientDialog} was closed.
   * </p>
   */
  void close();

}

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
package com.eclipsesource.tabris.widgets;

import java.io.Serializable;


/**
 * <p>
 * A <code>ClientDrawListener</code> will be called when a client draws on a <code>ClientCanvas</code>.
 * </p>
 *
 * @see ClientCanvas
 * @since 0.6
 */
public interface ClientDrawListener extends Serializable {

  void receivedDrawing();

}

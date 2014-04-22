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

import org.eclipse.rap.rwt.client.service.ClientService;

/**
 * @since 1.4
 *
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface Print extends ClientService {

  void print( String url, PrintOptions options );

  void addPrintListener( PrintListener listener );

  void removePrintListener( PrintListener listener );

}
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
package com.eclipsesource.tabris;

import org.eclipse.rap.rwt.client.Client;


/**
 * <p>
 * The RWT Tabris Client. Consumers can determine if the current accessing client is a {@link TabrisClient} by checking
 * the current RWT client.
 * </p>
 * <pre>
 * if( RWT.getClient() instanceof TabrisClient ) {
 *   ...
 * }
 * </pre>
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @since 1.2
 */
public interface TabrisClient extends Client {
}

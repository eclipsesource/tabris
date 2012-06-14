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
package com.eclipsesource.tabris.internal.bootstrap;

import org.eclipse.rwt.application.ApplicationConfiguration;
import org.eclipse.rwt.internal.application.ApplicationContext;
import org.eclipse.rwt.internal.application.ApplicationImpl;

@SuppressWarnings("restriction")
public class ApplicationWrapper extends ApplicationImpl {

  public ApplicationWrapper( ApplicationImpl application, ApplicationConfiguration configuration )
  {
    super( application.getAdapter( ApplicationContext.class ), configuration );
  }

}

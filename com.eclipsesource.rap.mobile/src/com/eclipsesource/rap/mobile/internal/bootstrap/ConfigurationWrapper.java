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
package com.eclipsesource.rap.mobile.internal.bootstrap;

import org.eclipse.rwt.application.ApplicationConfigurator;
import org.eclipse.rwt.internal.application.ApplicationConfigurationImpl;
import org.eclipse.rwt.internal.application.ApplicationContext;

@SuppressWarnings("restriction")
public class ConfigurationWrapper extends ApplicationConfigurationImpl {

  public ConfigurationWrapper( ApplicationConfigurationImpl configuration, ApplicationConfigurator configurator )
  {
    super( configuration.getAdapter( ApplicationContext.class ), configurator );
  }

}

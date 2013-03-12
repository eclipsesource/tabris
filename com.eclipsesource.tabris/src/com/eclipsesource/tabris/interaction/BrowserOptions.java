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
package com.eclipsesource.tabris.interaction;

import static com.eclipsesource.tabris.internal.Constants.PROPERTY_URL;

import java.net.MalformedURLException;
import java.net.URL;

import com.eclipsesource.tabris.internal.Preconditions;


/**
 * <p>
 * Concrete launch option to open an URL in the Browser.
 * </p>
 * @since 0.9
 */
public class BrowserOptions extends LaunchOptions {

  public BrowserOptions( String url ) {
    super( App.BROWSER );
    Preconditions.checkArgumentNotNull( url, "URL" );
    validateUrl( url );
    add( PROPERTY_URL, url );
  }

  private void validateUrl( String url ) {
    try {
      new URL( url );
    } catch( MalformedURLException mue ) {
      throw new IllegalArgumentException( url + " is not a valid url", mue );
    }
  }
}

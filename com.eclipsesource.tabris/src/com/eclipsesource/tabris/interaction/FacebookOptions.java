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

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * <p>
 * Concrete launch option to update the Facebook status.
 * <p>
 * 
 * @since 0.9
 */
public class FacebookOptions extends LaunchOptions {

  private static final String TEXT = "text";
  private static final String URL = "url";

  public FacebookOptions( String text ) {
    super( App.FACEBOOK );
    checkArgumentNotNull( text, "Text" );
    add( TEXT, text );
  }

  public void setUrl( String url ) {
    checkArgumentNotNull( url, "URL" );
    validateUrl( url );
    add( URL, url );
  }
  
  private void validateUrl( String url ) {
    try {
      new URL( url );
    } catch( MalformedURLException mue ) {
      throw new IllegalArgumentException( url + " is not a valid url", mue );
    }
  }
}

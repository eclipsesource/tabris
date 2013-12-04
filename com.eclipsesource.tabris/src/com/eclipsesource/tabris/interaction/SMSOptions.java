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

import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_NUMBER;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TEXT;


/**
 * <p>
 * Concrete launch option to send a text message.
 * </p>
 *
 * @since 0.9
 */
public class SMSOptions extends LaunchOptions {

  public SMSOptions( String phoneNumber, String text ) {
    super( App.SMS );
    whenNull( phoneNumber ).throwIllegalArgument( "Phone number must not be null" );
    whenNull( text ).throwIllegalArgument( "Text must not be null" );
    add( PROPERTY_NUMBER, phoneNumber );
    add( PROPERTY_TEXT, text );
  }
}

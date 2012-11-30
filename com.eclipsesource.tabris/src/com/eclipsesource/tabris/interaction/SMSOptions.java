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

import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNull;
import static com.eclipsesource.tabris.internal.Preconditions.argumentNotNullAndNotEmpty;


/**
 * @since 0.9
 */
public class SMSOptions extends LaunchOptions {

  private static final String TEXT = "text";
  private static final String NUMBER = "number";

  public SMSOptions( String phoneNumber, String text ) {
    super( App.SMS );
    argumentNotNullAndNotEmpty( phoneNumber, "Number" );
    argumentNotNull( text, "Text" );
    add( NUMBER, phoneNumber );
    add( TEXT, text );
  }
}

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

import com.eclipsesource.tabris.internal.Preconditions;


/**
 * @since 0.9
 */
public class PhoneOptions extends LaunchOptions {
  
  private static final String NUMBER = "number";

  public PhoneOptions( String phoneNumber ) {
    super( App.PHONE );
    Preconditions.argumentNotNullAndNotEmpty( phoneNumber, "Number" );
    add( NUMBER, phoneNumber );
  }
}

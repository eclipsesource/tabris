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
public class MailOptions extends LaunchOptions {
  
  private static final String CC = "cc";
  private static final String BODY = "body";
  private static String TO = "to";
  private static String SUBJECT = "subject";
  
  public MailOptions( String to ) {
    super( App.MAIL );
    argumentNotNullAndNotEmpty( to, "To" );
    add( TO, to );
  }
  
  public MailOptions( String to, String subject ) {
    this( to );
    argumentNotNull( subject, "Subject" );
    add( SUBJECT, subject );
  }
  
  public MailOptions( String to, String subject, String body ) {
    this( to, subject );
    argumentNotNull( body, "Body" );
    add( BODY, body );
  }
  
  public void setSubject( String subject ) {
    argumentNotNull( subject, "Subject" );
    add( SUBJECT, subject );
  }
  
  public void setCC( String cc ) {
    argumentNotNullAndNotEmpty( cc, "CC" );
    add( CC, cc );
  }
  
  public void setBody( String body ) {
    argumentNotNull( body, "Body" );
    add( BODY, body );
  }

  public void setUseHtml( boolean useHtml ) {
    add( "useHtml", String.valueOf( useHtml ) );
  }
}

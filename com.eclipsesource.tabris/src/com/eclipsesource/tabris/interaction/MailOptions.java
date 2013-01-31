/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.interaction;

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;
import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNullAndNotEmpty;

/**
 * <p>
 * Concrete launch option to create a Mail within the Mail App.
 * <p>
 * 
 * @since 0.9
 */
public class MailOptions extends LaunchOptions {

  private static final String CC = "cc";
  private static final String BODY = "body";
  private static final String TO = "to";
  private static final String SUBJECT = "subject";
  private static final String USE_HTML = "useHtml";

  public MailOptions( String to ) {
    super( App.MAIL );
    checkArgumentNotNullAndNotEmpty( to, "To" );
    add( TO, to );
  }

  public MailOptions( String to, String subject ) {
    this( to );
    checkArgumentNotNull( subject, "Subject" );
    add( SUBJECT, subject );
  }

  public MailOptions( String to, String subject, String body ) {
    this( to, subject );
    checkArgumentNotNull( body, "Body" );
    add( BODY, body );
  }

  public void setSubject( String subject ) {
    checkArgumentNotNull( subject, "Subject" );
    add( SUBJECT, subject );
  }

  public void setCC( String cc ) {
    checkArgumentNotNullAndNotEmpty( cc, "CC" );
    add( CC, cc );
  }

  public void setBody( String body ) {
    checkArgumentNotNull( body, "Body" );
    add( BODY, body );
  }

  public void setUseHtml( boolean useHtml ) {
    add( USE_HTML, String.valueOf( useHtml ) );
  }
}

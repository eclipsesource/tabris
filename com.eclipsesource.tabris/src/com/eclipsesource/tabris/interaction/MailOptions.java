/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.interaction;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_BODY;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_CC;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_HTML;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_SUBJECT;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TO;

/**
 * <p>
 * Concrete launch option to create a Mail within the Mail App.
 * <p>
 *
 * @since 0.9
 */
public class MailOptions extends LaunchOptions {

  public MailOptions( String to ) {
    super( App.MAIL );
    whenNull( to ).throwIllegalArgument( "To must not be null" );
    when( to.isEmpty() ).throwIllegalArgument( "To must not be empty" );
    add( PROPERTY_TO, to );
  }

  public MailOptions( String to, String subject ) {
    this( to );
    whenNull( subject ).throwIllegalArgument( "Subject must not be null" );
    add( PROPERTY_SUBJECT, subject );
  }

  public MailOptions( String to, String subject, String body ) {
    this( to, subject );
    whenNull( body ).throwIllegalArgument( "Body must not be null" );
    add( PROPERTY_BODY, body );
  }

  public void setSubject( String subject ) {
    whenNull( subject ).throwIllegalArgument( "Subject must not be null" );
    add( PROPERTY_SUBJECT, subject );
  }

  public void setCC( String cc ) {
    whenNull( cc ).throwIllegalArgument( "CC must not be null" );
    when( cc.isEmpty() ).throwIllegalArgument( "CC must not be empty" );
    add( PROPERTY_CC, cc );
  }

  public void setBody( String body ) {
    whenNull( body ).throwIllegalArgument( "Body must not be null" );
    add( PROPERTY_BODY, body );
  }

  public void setUseHtml( boolean useHtml ) {
    add( PROPERTY_HTML, Boolean.valueOf( useHtml ) );
  }
}

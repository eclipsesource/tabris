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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.eclipsesource.tabris.interaction.LaunchOptions.App;


public class MailOptionsTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutTo() {
    new MailOptions( null );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmtpyTo() {
    new MailOptions( "" );
  }
  
  @Test
  public void testWithTo() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    assertSame( App.MAIL, mailOptions.getApp() );
    assertEquals( "foo@bar.com", mailOptions.getOptions().get( "to" ) );
  }
  
  @Test
  public void testWithToAndSubject() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com", "foo" );
    
    assertSame( App.MAIL, mailOptions.getApp() );
    assertEquals( "foo@bar.com", mailOptions.getOptions().get( "to" ) );
    assertEquals( "foo", mailOptions.getOptions().get( "subject" ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testWithToAndSubjectFailsWithoutSubject() {
    new MailOptions( "foo@bar.com", null );
  }
  
  @Test
  public void testWithToAndSubjectDoesNotFailWithEmptySubject() {
    new MailOptions( "foo@bar.com", "" );
  }
  
  @Test
  public void testWithToAndSubjectAndBody() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com", "foo" , "bar" );
    
    assertSame( App.MAIL, mailOptions.getApp() );
    assertEquals( "foo@bar.com", mailOptions.getOptions().get( "to" ) );
    assertEquals( "foo", mailOptions.getOptions().get( "subject" ) );
    assertEquals( "bar", mailOptions.getOptions().get( "body" ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testWithToAndSubjectAndBodyFailsWithoutSubject() {
    new MailOptions( "foo@bar.com", "", null );
  }
  
  @Test
  public void testWithToAndSubjectAndBodyDoesNotFailWithEmptySubject() {
    new MailOptions( "foo@bar.com", "", "" );
  }
  
  @Test
  public void testSetSubject() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setSubject( "foo" );
    
    assertEquals( "foo", mailOptions.getOptions().get( "subject" ) );
  }
  
  @Test
  public void testSetSubjectWithEmptySubject() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setSubject( "" );
    
    assertEquals( "", mailOptions.getOptions().get( "subject" ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testSetSubjectFailsWithoutSubject() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setSubject( null );
  }
  
  @Test
  public void testSetBody() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setBody( "foo" );
    
    assertEquals( "foo", mailOptions.getOptions().get( "body" ) );
  }
  
  @Test
  public void testSetBodyWithEmptyBody() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setBody( "" );
    
    assertEquals( "", mailOptions.getOptions().get( "body" ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testSetBodyFailsWithoutBody() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setBody( null );
  }
  
  @Test
  public void testSetCC() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setCC( "bar@foo.com" );
    
    assertEquals( "bar@foo.com", mailOptions.getOptions().get( "cc" ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testSetCCFailsWithEmptyCC() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setCC( "" );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testSetCCFailsWithoutCC() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setCC( null );
  }
  
  @Test
  public void testSetUseHtml() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setUseHtml( true );
    
    assertEquals( String.valueOf( true ), mailOptions.getOptions().get( "useHtml" ) );
  }
  
  @Test
  public void testSetUseNoHtml() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setUseHtml( false );
    
    assertEquals( String.valueOf( false ), mailOptions.getOptions().get( "useHtml" ) );
  }
  
  @Test
  public void testSetUseNoHtmlOverridesUse() {
    MailOptions mailOptions = new MailOptions( "foo@bar.com" );
    
    mailOptions.setUseHtml( true );
    mailOptions.setUseHtml( false );
    
    assertEquals( String.valueOf( false ), mailOptions.getOptions().get( "useHtml" ) );
  }
}

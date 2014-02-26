/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.TestRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class VersionCheckTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testFindsHasServerServsion() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    String serverVersion = versionCheck.getServerVersion();

    assertEquals( "1.3", serverVersion);
  }

  @Test
  public void testFindsClientVersionOnAndroid() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    String clientVersion = versionCheck.getClientVersion();

    assertEquals( "1.3", clientVersion );
  }

  @Test
  public void testFindsClientVersionOnIOS() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    String clientVersion = versionCheck.getClientVersion();

    assertEquals( "1.3", clientVersion );
  }

  @Test
  public void testMatchesIdenticalVersionOnAdnroid() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertTrue( matches );
  }

  @Test
  public void testMatchesIdenticalVersionOnIOS() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertTrue( matches );
  }

  @Test
  public void testMatchesMajorMinorVersionOnAdnroid() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.1 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertTrue( matches );
  }

  @Test
  public void testMatchesMajorMinorVersionOnIOS() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.3.1 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertTrue( matches );
  }

  @Test
  public void testMatchesNotDifferentMinorOnAndroid() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.4 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentMinorOnIOS() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.4 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentMajorOnAndroid() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/2.3 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentMajorOnIOS() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/2.3 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentHighMajorOnAndroid() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/41.1.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentHighMajorOnIOS() {
    TestRequest request = ( TestRequest )RWT.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/42.1.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

}

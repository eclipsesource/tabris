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

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.test.util.TabrisRequest;


public class VersionCheckTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Test
  public void testFindsHasServerServsion() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    String serverVersion = versionCheck.getServerVersion();

    assertEquals( "1.3", serverVersion);
  }

  @Test
  public void testFindsClientVersionOnAndroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    String clientVersion = versionCheck.getClientVersion();

    assertEquals( "1.3", clientVersion );
  }

  @Test
  public void testFindsClientVersionOnIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    String clientVersion = versionCheck.getClientVersion();

    assertEquals( "1.3", clientVersion );
  }

  @Test
  public void testMatchesIdenticalVersionOnAdnroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertTrue( matches );
  }

  @Test
  public void testMatchesIdenticalVersionOnIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.3.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertTrue( matches );
  }

  @Test
  public void testMatchesMajorMinorVersionOnAdnroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.1 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertTrue( matches );
  }

  @Test
  public void testMatchesMajorMinorVersionOnIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.3.1 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertTrue( matches );
  }

  @Test
  public void testMatchesNotDifferentMinorOnAndroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.4 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentMinorOnIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.4 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentMajorOnAndroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/2.3 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentMajorOnIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/2.3 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentHighMajorOnAndroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/41.1.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentHighMajorOnIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/42.1.0 (foo)" );
    VersionCheck versionCheck = new VersionCheck( "1.3" );

    boolean matches = versionCheck.matches();

    assertFalse( matches );
  }

}

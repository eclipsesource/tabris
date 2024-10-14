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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.VersionCheck;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;
import com.eclipsesource.tabris.test.util.TabrisRequest;


public class VersionCheckerTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCheck() {
    new VersionChecker( null );
  }

  @Test
  public void testFindsClientVersionOnAndroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.0 (foo)" );
    VersionCheck check = mock( VersionCheck.class );
    VersionChecker checker = new VersionChecker( check, "1.3" );

    checker.accept();

    verify( check ).accept( "1.3.0", "1.3" );
  }

  @Test
  public void testFindsClientVersionOnIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.3.0 (foo)" );
    VersionCheck check = mock( VersionCheck.class );
    VersionChecker checker = new VersionChecker( check, "1.3" );

    checker.accept();

    verify( check ).accept( "1.3.0", "1.3" );
  }

  @Test
  public void testFindsMajorMinorVersionOnAdnroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/1.3.1 (foo)" );
    VersionCheck check = mock( VersionCheck.class );
    VersionChecker checker = new VersionChecker( check, "1.3" );

    checker.accept();

    verify( check ).accept( "1.3.1", "1.3" );
  }

  @Test
  public void testFindsMajorMinorVersionOnIOS() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.ios/1.3.1 (foo)" );
    VersionCheck check = mock( VersionCheck.class );
    VersionChecker checker = new VersionChecker( check, "1.3" );

    checker.accept();

    verify( check ).accept( "1.3.1", "1.3" );
  }

  @Test
  public void testFindsHighMajorOnAndroid() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/41.1.0 (foo)" );
    VersionCheck check = mock( VersionCheck.class );
    VersionChecker checker = new VersionChecker( check, "1.3" );

    checker.accept();

    verify( check ).accept( "41.1.0", "1.3" );
  }

  @Test
  public void testDelegatesErrorMessage() {
    TabrisRequest request = environment.getRequest();
    request.setHeader( Constants.USER_AGENT, "com.eclipsesource.tabris.android/41.1.0 (foo)" );
    VersionCheck check = mock( VersionCheck.class );
    when( check.getErrorMessage( anyString(), anyString() ) ).thenReturn( "foo" );
    VersionChecker checker = new VersionChecker( check, "1.3" );

    String errorMessage = checker.getErrorMessage();

    assertEquals( "foo", errorMessage );
  }

}

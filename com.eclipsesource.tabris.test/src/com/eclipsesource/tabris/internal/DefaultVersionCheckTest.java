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


public class DefaultVersionCheckTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Test
  public void testMatchesIdenticalVersion() {
    DefaultVersionCheck versionCheck = new DefaultVersionCheck();

    boolean matches = versionCheck.accept( "1.3.0", "1.3.0" );

    assertTrue( matches );
  }

  @Test
  public void testMatchesIdenticalVersionWithNoMicroForClient() {
    DefaultVersionCheck versionCheck = new DefaultVersionCheck();

    boolean matches = versionCheck.accept( "1.3", "1.3.0" );

    assertTrue( matches );
  }

  @Test
  public void testMatchesDifferentMicro() {
    DefaultVersionCheck versionCheck = new DefaultVersionCheck();

    boolean matches = versionCheck.accept( "1.3.1", "1.3.0" );

    assertTrue( matches );
  }

  @Test
  public void testMatchesNotDifferentMinor() {
    DefaultVersionCheck versionCheck = new DefaultVersionCheck();

    boolean matches = versionCheck.accept( "1.4", "1.3" );

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentMajor() {
    DefaultVersionCheck versionCheck = new DefaultVersionCheck();

    boolean matches = versionCheck.accept( "2.3", "1.4" );

    assertFalse( matches );
  }

  @Test
  public void testMatchesNotDifferentHighMajorOnAndroid() {
    DefaultVersionCheck versionCheck = new DefaultVersionCheck();

    boolean matches = versionCheck.accept( "41.0", "1.4" );

    assertFalse( matches );
  }

  @Test
  public void testComputesErrorMessage() {
    DefaultVersionCheck check = new DefaultVersionCheck();

    String errorMessage = check.getErrorMessage( "42.1", "1.3" );

    assertEquals( "Incompatible Tabris Versions:\nClient 42.1 vs. Server 1.3", errorMessage );
  }

}

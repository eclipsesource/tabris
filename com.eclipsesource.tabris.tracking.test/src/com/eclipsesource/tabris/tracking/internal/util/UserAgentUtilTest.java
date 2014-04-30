/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.eclipsesource.tabris.device.ClientDevice.Platform;


public class UserAgentUtilTest {

  @Test
  public void testReturnsAndroidUserAgentGeneratorForAndroid() {
    UserAgentProvider provider = UserAgentUtil.getProvider( Platform.ANDROID );

    assertTrue( provider instanceof AndroidUserAgentGenerator );
  }

  @Test
  public void testReturnsIOSUserAgentGeneratorForIOS() {
    UserAgentProvider provider = UserAgentUtil.getProvider( Platform.IOS );

    assertTrue( provider instanceof IOSUserAgentGenerator );
  }

  @Test
  public void testReturnsUniversalUserAgentGeneratorForSWT() {
    UserAgentProvider provider = UserAgentUtil.getProvider( Platform.SWT );

    assertTrue( provider instanceof UniversalUserAgentProvider );
  }

  @Test
  public void testReturnsUniversalUserAgentGeneratorForWeb() {
    UserAgentProvider provider = UserAgentUtil.getProvider( Platform.WEB );

    assertTrue( provider instanceof UniversalUserAgentProvider );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testReturnsUniversalUserAgentGeneratorForNull() {
    UserAgentUtil.getProvider( null );
  }

}

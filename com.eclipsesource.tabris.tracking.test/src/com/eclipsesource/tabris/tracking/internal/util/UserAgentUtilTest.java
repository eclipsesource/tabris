/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.util;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;

import com.eclipsesource.tabris.device.ClientDevice.Platform;

public class UserAgentUtilTest {

  @Test
  public void testReturnsAndroidUserAgentGeneratorForAndroid() {
    assertTrue( UserAgentUtil.getProvider( Platform.ANDROID ) instanceof AndroidUserAgentGenerator );
  }

  @Test
  public void testReturnsIOSUserAgentGeneratorForIOS() {
    assertTrue( UserAgentUtil.getProvider( Platform.IOS ) instanceof IOSUserAgentGenerator );
  }

  @Test
  public void testReturnsUniversalUserAgentGeneratorForSWT() {
    assertTrue( UserAgentUtil.getProvider( Platform.SWT ) instanceof UniversalUserAgentProvider );
  }

  @Test
  public void testReturnsUniversalUserAgentGeneratorForWeb() {
    assertTrue( UserAgentUtil.getProvider( Platform.WEB ) instanceof UniversalUserAgentProvider );
  }
  
  @Test
  public void testReturnsUniversalUserAgentGeneratorForNull() {
    assertTrue( UserAgentUtil.getProvider( null ) instanceof UniversalUserAgentProvider );
  }

  @Test
  public void testCannotBeInstantiated() throws NoSuchMethodException, SecurityException {
    Constructor<?> constructor = UserAgentUtil.class.getDeclaredConstructor();

    assertTrue( Modifier.isPrivate( constructor.getModifiers() ) );
  }
}

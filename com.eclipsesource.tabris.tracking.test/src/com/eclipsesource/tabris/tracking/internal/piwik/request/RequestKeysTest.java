/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.request;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class RequestKeysTest {

  @Test
  public void testCannotBeInstantiated() throws NoSuchMethodException, SecurityException {
    Constructor<?> constructor = RequestKeys.class.getDeclaredConstructor();
    
    assertTrue( Modifier.isPrivate( constructor.getModifiers() ) );
  }
}

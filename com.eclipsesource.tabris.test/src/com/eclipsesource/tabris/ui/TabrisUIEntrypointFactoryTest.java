/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TabrisUIEntrypointFactoryTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( TabrisUIEntrypointFactory.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullConfiguration() {
    new TabrisUIEntrypointFactory( null );
  }

  @Test
  public void testCanCreateEntrypoints() {
    UIConfiguration uiConfiguration = new UIConfiguration();
    TabrisUIEntrypointFactory factory = new TabrisUIEntrypointFactory( uiConfiguration );

    EntryPoint entryPoint = factory.create();

    assertNotNull( entryPoint );
  }

  @Test
  public void testCreatesNewEntrypoints() {
    UIConfiguration uiConfiguration = new UIConfiguration();
    TabrisUIEntrypointFactory factory = new TabrisUIEntrypointFactory( uiConfiguration );

    EntryPoint entryPoint1 = factory.create();
    EntryPoint entryPoint2 = factory.create();

    assertNotSame( entryPoint1, entryPoint2 );
  }
}

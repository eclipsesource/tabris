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
package com.eclipsesource.tabris.internal.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.UI;


public class RemoteActionFactoryTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testCreatesRegularRemoteAction() {
    ActionDescriptor descriptor= mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( mock( Action.class ) );

    RemoteAction remoteAction = RemoteActionFactory.createRemoteAction( mock( UI.class ), descriptor, "foo" );

    assertFalse( remoteAction instanceof RemoteSearchAction );
  }

  @Test
  public void testCreatesSearchRemoteAction() {
    ActionDescriptor descriptor= mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( new TestSearchAction() );

    RemoteAction remoteAction = RemoteActionFactory.createRemoteAction( mock( UI.class ), descriptor, "foo" );

    assertTrue( remoteAction instanceof RemoteSearchAction );
  }
}

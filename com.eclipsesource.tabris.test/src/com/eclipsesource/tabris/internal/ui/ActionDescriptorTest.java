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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.ui.Action;


public class ActionDescriptorTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testGetId() {
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        mock( Action.class ),
                                                        "bar",
                                                        "testImage.png",
                                                        true,
                                                        true );

    assertEquals( "foo", descriptor.getId() );
  }

  @Test
  public void testGetAction() {
    Action action = mock( Action.class );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        action,
                                                        "bar",
                                                        "testImage.png",
                                                        true,
                                                        true );

    assertSame( action, descriptor.getAction() );
  }

  @Test
  public void testIsVisible() {
    Action action = mock( Action.class );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        action,
                                                        "bar",
                                                        "testImage.png",
                                                        true,
                                                        true );

    assertTrue( descriptor.isVisible() );
  }

  @Test
  public void testIsEnabled() {
    Action action = mock( Action.class );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        action,
                                                        "bar",
                                                        "testImage.png",
                                                        true,
                                                        true );

    assertTrue( descriptor.isEnabled() );
  }

  @Test
  public void testGetTitle() {
    Action action = mock( Action.class );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        action,
                                                        "bar",
                                                        "testImage.png",
                                                        true,
                                                        true );

    assertEquals( "bar", descriptor.getTitle() );
  }

  @Test
    public void testGetImagePath() {
      Action action = mock( Action.class );
      String image = "testImage.png";
      ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                          action,
                                                          "bar",
                                                          image,
                                                          true,
                                                          true );

      assertSame( image, descriptor.getImagePath() );
    }

}

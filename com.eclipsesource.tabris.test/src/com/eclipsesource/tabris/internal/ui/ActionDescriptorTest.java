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

import static com.eclipsesource.tabris.internal.ui.UITestUtil.createImage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.Prominence;


public class ActionDescriptorTest {

  private Display display;

  @Before
  public void setUp() {
    Fixture.setUp();
    display = new Display();
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
                                                        createImage( display ),
                                                        true,
                                                        true,
                                                        Prominence.DEFAULT );

    assertEquals( "foo", descriptor.getId() );
  }

  @Test
  public void testGetAction() {
    Action action = mock( Action.class );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        action,
                                                        "bar",
                                                        createImage( display ),
                                                        true,
                                                        true,
                                                        Prominence.DEFAULT );

    assertSame( action, descriptor.getAction() );
  }

  @Test
  public void testIsVisible() {
    Action action = mock( Action.class );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        action,
                                                        "bar",
                                                        createImage( display ),
                                                        true,
                                                        true,
                                                        Prominence.DEFAULT );

    assertTrue( descriptor.isVisible() );
  }

  @Test
  public void testIsEnabled() {
    Action action = mock( Action.class );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        action,
                                                        "bar",
                                                        createImage( display ),
                                                        true,
                                                        true,
                                                        Prominence.DEFAULT );

    assertTrue( descriptor.isEnabled() );
  }

  @Test
  public void testGetTitle() {
    Action action = mock( Action.class );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        action,
                                                        "bar",
                                                        createImage( display ),
                                                        true,
                                                        true,
                                                        Prominence.DEFAULT );

    assertEquals( "bar", descriptor.getTitle() );
  }

  @Test
  public void testGetImage() {
    Action action = mock( Action.class );
    Image image = createImage( display );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        action,
                                                        "bar",
                                                        image,
                                                        true,
                                                        true,
                                                        Prominence.DEFAULT );

    assertSame( image, descriptor.getImage() );
  }

  @Test
  public void testGetProminence() {
    Action action = mock( Action.class );
    Image image = createImage( display );
    ActionDescriptor descriptor = new ActionDescriptor( "foo", action, "bar", image, true, true, Prominence.DEFAULT );

    assertSame( Prominence.DEFAULT, descriptor.getProminence()[ 0 ] );
    assertEquals( 1, descriptor.getProminence().length );
  }

  @Test
  public void testGetProminenceIsNeverNull() {
    Action action = mock( Action.class );
    Image image = createImage( display );
    ActionDescriptor descriptor = new ActionDescriptor( "foo", action, "bar", image, true, true );

    assertNotNull( descriptor.getProminence() );
  }
}

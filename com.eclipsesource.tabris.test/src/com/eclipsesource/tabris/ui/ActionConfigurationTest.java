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

import static com.eclipsesource.tabris.ui.ActionConfiguration.newAction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.InternalActionConfiguration;
import com.eclipsesource.tabris.internal.ui.TestAction;
import com.eclipsesource.tabris.internal.ui.UITestUtil;


public class ActionConfigurationTest {

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

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullId() {
    ActionConfiguration.newAction( null, TestAction.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() {
    ActionConfiguration.newAction( "", TestAction.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullType() {
    ActionConfiguration.newAction( "foo", null );
  }

  @Test
  public void testSetsDefaultAttributes() {
    ActionConfiguration configuration = ActionConfiguration.newAction( "foo", TestAction.class );

    ActionDescriptor descriptor = ( ( InternalActionConfiguration )configuration ).createDescriptor();

    assertEquals( "foo", descriptor.getId() );
    assertTrue( descriptor.getAction() instanceof TestAction );
    assertNull( descriptor.getImage() );
    assertEquals( "", descriptor.getTitle() );
    assertTrue( descriptor.isEnabled() );
    assertTrue( descriptor.isVisible() );
  }

  @Test
  public void testSetsTitle() {
    ActionConfiguration configuration = newAction( "foo", TestAction.class ).setTitle( "bar" );

    ActionDescriptor descriptor = ( ( InternalActionConfiguration )configuration ).createDescriptor();

    assertEquals( "bar", descriptor.getTitle() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetTitleFailsWithNull() {
    newAction( "foo", TestAction.class ).setTitle( null );
  }

  @Test
  public void testSetsVisible() {
    ActionConfiguration configuration = newAction( "foo", TestAction.class ).setVisible( false );

    ActionDescriptor descriptor = ( ( InternalActionConfiguration )configuration ).createDescriptor();

    assertFalse( descriptor.isVisible() );
  }

  @Test
  public void testSetsEnabled() {
    ActionConfiguration configuration = newAction( "foo", TestAction.class ).setEnabled( false );

    ActionDescriptor descriptor = ( ( InternalActionConfiguration )configuration ).createDescriptor();

    assertFalse( descriptor.isEnabled() );
  }

  @Test
  public void testSetsImage() {
    Image image = UITestUtil.createImage( display );
    ActionConfiguration configuration = newAction( "foo", TestAction.class ).setImage( image );

    ActionDescriptor descriptor = ( ( InternalActionConfiguration )configuration ).createDescriptor();

    assertSame( image, descriptor.getImage() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetImageFailsWithNull() {
    newAction( "foo", TestAction.class ).setImage( null );
  }

}

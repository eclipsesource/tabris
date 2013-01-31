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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.InternalPageConfiguration;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.TestAction;
import com.eclipsesource.tabris.internal.ui.TestPage;
import com.eclipsesource.tabris.internal.ui.UITestUtil;


public class PageConfigurationTest {

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
    PageConfiguration.newPage( null, TestPage.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() {
    PageConfiguration.newPage( "", TestPage.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullType() {
    PageConfiguration.newPage( "foo", null );
  }

  @Test
  public void testSetsDefaultValues() {
    PageConfiguration config = PageConfiguration.newPage( "foo", TestPage.class );

    PageDescriptor descriptor = ( ( InternalPageConfiguration )config ).createDescriptor();

    assertEquals( "foo", descriptor.getId() );
    assertSame( TestPage.class, descriptor.getPageType() );
    assertFalse( descriptor.isTopLevel() );
    assertEquals( "", descriptor.getTitle() );
    assertNull( descriptor.getImage() );
    assertEquals( 0, descriptor.getPageStyle().length );
  }

  @Test
  public void testSetsTopLevel() {
    PageConfiguration config = PageConfiguration.newPage( "foo", TestPage.class ).setTopLevel( true );

    PageDescriptor descriptor = ( ( InternalPageConfiguration )config ).createDescriptor();

    assertTrue( descriptor.isTopLevel() );
  }

  @Test
  public void testSetsTitle() {
    PageConfiguration config = PageConfiguration.newPage( "foo", TestPage.class ).setTitle( "bar" );

    PageDescriptor descriptor = ( ( InternalPageConfiguration )config ).createDescriptor();

    assertEquals( "bar", descriptor.getTitle() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetTitleFailsWithNull() {
    PageConfiguration.newPage( "foo", TestPage.class ).setTitle( null );
  }

  @Test
  public void testSetsImage() {
    Image image = UITestUtil.createImage( display );
    PageConfiguration config = PageConfiguration.newPage( "foo", TestPage.class ).setImage( image );

    PageDescriptor descriptor = ( ( InternalPageConfiguration )config ).createDescriptor();

    assertSame( image, descriptor.getImage() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetImageFailsWithNull() {
    PageConfiguration.newPage( "foo", TestPage.class ).setImage( null );
  }

  @Test
  public void testSetsStyle() {
    PageConfiguration config = PageConfiguration.newPage( "foo", TestPage.class ).setStyle( PageStyle.DEFAULT, PageStyle.FULLSCREEN );

    PageDescriptor descriptor = ( ( InternalPageConfiguration )config ).createDescriptor();

    PageStyle[] pageStyle = descriptor.getPageStyle();
    assertEquals( 2, pageStyle.length );
    assertSame( PageStyle.DEFAULT, pageStyle[ 0 ] );
    assertSame( PageStyle.FULLSCREEN, pageStyle[ 1 ] );
  }

  @Test
  public void testAddsAction() {
    ActionConfiguration actionConfig = ActionConfiguration.newAction( "bar", TestAction.class );
    PageConfiguration config = PageConfiguration.newPage( "foo", TestPage.class ).addAction( actionConfig );

    PageDescriptor descriptor = ( ( InternalPageConfiguration )config ).createDescriptor();

    List<ActionDescriptor> actions = descriptor.getActions();
    assertEquals( 1, actions.size() );
    assertEquals( actions.get( 0 ).getId(), "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddActionFailsWithNullAction() {
    PageConfiguration.newPage( "foo", TestPage.class ).addAction( null );
  }

}

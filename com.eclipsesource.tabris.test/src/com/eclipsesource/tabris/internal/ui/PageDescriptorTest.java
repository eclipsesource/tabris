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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageStyle;


public class PageDescriptorTest {

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
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class, "", null, true, PageStyle.DEFAULT );

    assertEquals( "foo", descriptor.getId() );
  }

  @Test
  public void testGetPage() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class, "", null, true, PageStyle.DEFAULT );

    assertSame( TestPage.class, descriptor.getPageType() );
  }

  @Test
  public void testGetTitle() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class, "bar", null, true, PageStyle.DEFAULT );

    assertEquals( "bar", descriptor.getTitle() );
  }

  @Test
  public void testGetImagePath() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class, "bar", "testImage.png", true, PageStyle.DEFAULT );

    assertSame( "testImage.png", descriptor.getImagePath() );
  }

  @Test
  public void testGetTransitionHint() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class, "", null, true, PageStyle.DEFAULT );

    assertSame( PageStyle.DEFAULT, descriptor.getPageStyle()[ 0 ] );
  }

  @Test
  public void testIsTopLevel() {
    PageDescriptor descriptor1 = new PageDescriptor( "foo", TestPage.class, "", null, true, PageStyle.DEFAULT );
    PageDescriptor descriptor2 = new PageDescriptor( "foo1", TestPage.class, "", null, false, PageStyle.DEFAULT );

    assertTrue( descriptor1.isTopLevel() );
    assertFalse( descriptor2.isTopLevel() );
  }

  @Test
  public void testAddAction() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class, "", null, true, PageStyle.DEFAULT );

    descriptor.addAction( new ActionConfiguration( "foo", TestAction.class ) );

    List<ActionDescriptor> actions = descriptor.getActions();
    assertEquals( 1, actions.size() );
    ActionDescriptor actionDescriptor = actions.get( 0 );
    assertEquals( "foo", actionDescriptor.getId() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddActionFailsWithNullConfiguration() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class, "", null, true, PageStyle.DEFAULT );

    descriptor.addAction( null );
  }

}

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
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.ui.Action;


public class DescriptorHolderTest {

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

  @Test( expected = IllegalStateException.class )
  public void testFailsAddingPageDescriptorTwice() {
    DescriptorHolder holder = new DescriptorHolder();
    PageDescriptor descriptor = createDescriptor( "foo" );

    holder.add( descriptor );
    holder.add( descriptor );
  }

  @Test
  public void testAddsPageDescriptor() {
    DescriptorHolder holder = new DescriptorHolder();
    PageDescriptor descriptor = createDescriptor( "foo" );

    holder.add( descriptor );

    assertSame( descriptor, holder.getPageDescriptor( "foo" ) );
  }

  private PageDescriptor createDescriptor( String id ) {
    return new PageDescriptor( id, TestPage.class, "", null, true );
  }

  @Test
  public void testAddsAction() {
    DescriptorHolder holder = new DescriptorHolder();
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        mock( Action.class ),
                                                        "bar",
                                                        createImage( display ),
                                                        true,
                                                        true );

    holder.add( descriptor );

    assertSame( descriptor, holder.getActionDescriptor( "foo" ) );
  }

  @Test
  public void testGetGlobalActions() {
    DescriptorHolder holder = new DescriptorHolder();
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        mock( Action.class ),
                                                        "bar",
                                                        createImage( display ),
                                                        true,
                                                        true );

    holder.add( descriptor );

    List<ActionDescriptor> actions = holder.getGlobalActions();
    assertEquals( 1, actions.size() );
    assertSame( descriptor, actions.get( 0 ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testAddsActionTwiceFails() {
    DescriptorHolder holder = new DescriptorHolder();
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        mock( Action.class ),
                                                        "bar",
                                                        createImage( display ),
                                                        true,
                                                        true );

    holder.add( descriptor );
    holder.add( descriptor );
  }

  @Test
  public void testGetRootPagesReturnsOnlyRootPages() {
    DescriptorHolder holder = new DescriptorHolder();
    PageDescriptor descriptor1 = createDescriptor( "foo1", true );
    PageDescriptor descriptor2 = createDescriptor( "foo2", false );
    PageDescriptor descriptor3 = createDescriptor( "foo3", true );

    holder.add( descriptor1 );
    holder.add( descriptor2 );
    holder.add( descriptor3 );

    List<PageDescriptor> rootPages = holder.getRootPages();
    assertEquals( 2, rootPages.size() );
    assertSame( descriptor1, rootPages.get( 0 ) );
    assertSame( descriptor3, rootPages.get( 1 ) );
  }

  private PageDescriptor createDescriptor( String id, boolean isRoot ) {
    return new PageDescriptor( id, TestPage.class, "", null, isRoot );
  }
}

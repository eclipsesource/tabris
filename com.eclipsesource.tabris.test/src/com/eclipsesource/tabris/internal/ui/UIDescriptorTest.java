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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.TabrisClient;
import com.eclipsesource.tabris.internal.ui.web.WebRendererFactory;
import com.eclipsesource.tabris.ui.TransitionListener;


public class UIDescriptorTest {

  @Before
  public void setUp() {
    Fixture.setUp();
    new Display();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( UIDescriptor.class ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsAddingPageDescriptorTwice() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    PageDescriptor descriptor = createDescriptor( "foo" );

    uiDescriptor.add( descriptor );
    uiDescriptor.add( descriptor );
  }

  @Test
  public void testAddsPageDescriptor() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    PageDescriptor descriptor = createDescriptor( "foo" );

    uiDescriptor.add( descriptor );

    assertSame( descriptor, uiDescriptor.getPageDescriptor( "foo" ) );
  }

  private PageDescriptor createDescriptor( String id ) {
    return new PageDescriptor( id, TestPage.class, "", null, true );
  }

  @Test
  public void testAddsAction() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    InputStream image = UIDescriptorTest.class.getResourceAsStream( "testImage.png" );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        new TestAction(),
                                                        "bar",
                                                        ImageUtil.getBytes( image ),
                                                        true,
                                                        true );

    uiDescriptor.add( descriptor );

    assertSame( descriptor, uiDescriptor.getActionDescriptor( "foo" ) );
  }

  @Test
  public void testRemovesAction() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    InputStream image = UIDescriptorTest.class.getResourceAsStream( "testImage.png" );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        new TestAction(),
                                                        "bar",
                                                        ImageUtil.getBytes( image ),
                                                        true,
                                                        true );
    uiDescriptor.add( descriptor );

    uiDescriptor.removeAction( "foo" );

    assertNull( uiDescriptor.getActionDescriptor( "foo" ) );
  }

  @Test
  public void testAddsTransistionListener() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    TransitionListener listener = mock( TransitionListener.class );

    uiDescriptor.addTransitionListener( listener );

    List<TransitionListener> transitionListeners = uiDescriptor.getTransitionListeners();
    assertTrue( transitionListeners.contains( listener ) );
  }

  @Test
  public void testRemovesTransistionListener() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    TransitionListener listener = mock( TransitionListener.class );
    uiDescriptor.addTransitionListener( listener );

    uiDescriptor.removeTransitionListener( listener );

    List<TransitionListener> transitionListeners = uiDescriptor.getTransitionListeners();
    assertFalse( transitionListeners.contains( listener ) );
  }

  @Test
  public void testGetGlobalActions() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    InputStream image = UIDescriptorTest.class.getResourceAsStream( "testImage.png" );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        new TestAction(),
                                                        "bar",
                                                        ImageUtil.getBytes( image ),
                                                        true,
                                                        true );

    uiDescriptor.add( descriptor );

    List<ActionDescriptor> actions = uiDescriptor.getGlobalActions();
    assertEquals( 1, actions.size() );
    assertSame( descriptor, actions.get( 0 ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testAddsActionTwiceFails() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    InputStream image = UIDescriptorTest.class.getResourceAsStream( "testImage.png" );
    ActionDescriptor descriptor = new ActionDescriptor( "foo",
                                                        new TestAction(),
                                                        "bar",
                                                        ImageUtil.getBytes( image ),
                                                        true,
                                                        true );

    uiDescriptor.add( descriptor );
    uiDescriptor.add( descriptor );
  }

  @Test
  public void testGetRootPagesReturnsOnlyRootPages() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    PageDescriptor descriptor1 = createDescriptor( "foo1", true );
    PageDescriptor descriptor2 = createDescriptor( "foo2", false );
    PageDescriptor descriptor3 = createDescriptor( "foo3", true );

    uiDescriptor.add( descriptor1 );
    uiDescriptor.add( descriptor2 );
    uiDescriptor.add( descriptor3 );

    List<PageDescriptor> rootPages = uiDescriptor.getRootPages();
    assertEquals( 2, rootPages.size() );
    assertSame( descriptor1, rootPages.get( 0 ) );
    assertSame( descriptor3, rootPages.get( 1 ) );
  }

  @Test
  public void testGetRendererFactory_tabris() {
    Fixture.fakeClient( mock( TabrisClient.class ) );
    UIDescriptor uiDescriptor = new UIDescriptor();

    assertTrue( uiDescriptor.getRendererFactory() instanceof RemoteRendererFactory );
  }

  @Test
  public void testGetRendererFactory_web() {
    Fixture.fakeClient( mock( WebClient.class ) );
    UIDescriptor uiDescriptor = new UIDescriptor();

    assertTrue( uiDescriptor.getRendererFactory() instanceof WebRendererFactory );
  }

  private PageDescriptor createDescriptor( String id, boolean isRoot ) {
    return new PageDescriptor( id, TestPage.class, "", null, isRoot );
  }
}

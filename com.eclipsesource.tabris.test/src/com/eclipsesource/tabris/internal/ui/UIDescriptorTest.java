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

import java.io.Serializable;
import java.util.List;

import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.internal.ui.web.WebRendererFactory;
import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.ui.ActionListener;
import com.eclipsesource.tabris.ui.TransitionListener;


@RunWith( RWTRunner.class )
public class UIDescriptorTest {

  @Before
  public void setUp() {
    new Display();
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

  @Test
  public void testRemovesPageDescriptor() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    PageDescriptor descriptor = createDescriptor( "foo" );
    uiDescriptor.add( descriptor );

    uiDescriptor.removePageDescriptor( "foo" );

    assertNull( uiDescriptor.getPageDescriptor( "foo" ) );
  }

  private PageDescriptor createDescriptor( String id ) {
    return new PageDescriptor( id, TestPage.class );
  }

  @Test
  public void testAddsAction() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    ActionDescriptor descriptor = new ActionDescriptor( "foo", new TestAction() );

    uiDescriptor.add( descriptor );

    assertSame( descriptor, uiDescriptor.getActionDescriptor( "foo" ) );
  }

  @Test
  public void testRemovesAction() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    ActionDescriptor descriptor = new ActionDescriptor( "foo", new TestAction());
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
  public void testAddsActionListener() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    ActionListener listener = mock( ActionListener.class );

    uiDescriptor.addActionListener( listener );

    List<ActionListener> actionListeners = uiDescriptor.getActionListeners();
    assertTrue( actionListeners.contains( listener ) );
  }

  @Test
  public void testRemovesActionListener() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    ActionListener listener = mock( ActionListener.class );
    uiDescriptor.addActionListener( listener );

    uiDescriptor.removeActionListener( listener );

    List<ActionListener> actionListeners = uiDescriptor.getActionListeners();
    assertFalse( actionListeners.contains( listener ) );
  }

  @Test
  public void testGetGlobalActions() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    ActionDescriptor descriptor = new ActionDescriptor( "foo", new TestAction() );

    uiDescriptor.add( descriptor );

    List<ActionDescriptor> actions = uiDescriptor.getGlobalActions();
    assertEquals( 1, actions.size() );
    assertSame( descriptor, actions.get( 0 ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testAddsActionTwiceFails() {
    UIDescriptor uiDescriptor = new UIDescriptor();
    ActionDescriptor descriptor = new ActionDescriptor( "foo", new TestAction() );

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
    return new PageDescriptor( id, TestPage.class ).setTopLevel( isRoot );
  }
}

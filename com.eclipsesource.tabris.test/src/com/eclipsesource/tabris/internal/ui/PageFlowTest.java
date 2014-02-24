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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.List;

import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UI;


@RunWith( RWTRunner.class )
public class PageFlowTest {

  private Shell shell;

  @Before
  public void setUp() {
    Display display = new Display();
    shell = new Shell( display );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PageFlow.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullRoot() {
    new PageFlow( null );
  }

  @Test
  public void testGetRootRenderer() {
    RemotePage rootPage = mock( RemotePage.class );
    PageFlow flow = new PageFlow( rootPage );

    assertSame( rootPage, flow.getRootRenderer() );
  }

  @Test
  public void testGetCurrentRenderer() {
    RemotePage rootPage = mock( RemotePage.class );
    PageFlow flow = new PageFlow( rootPage );
    RemotePage currentPage = mock( RemotePage.class );

    flow.add( currentPage );

    assertSame( currentPage, flow.getCurrentRenderer() );
  }

  @Test
  public void testGetPreviousRenderer() {
    RemotePage previousPage = mock( RemotePage.class );
    PageFlow flow = new PageFlow( previousPage );
    RemotePage currentPage = mock( RemotePage.class );

    flow.add( currentPage );

    assertSame( previousPage, flow.getPreviousRenderer() );
  }

  @Test
  public void testAddTwoRenderersSetsCurrentToLast() {
    PageFlow flow = new PageFlow( mock( RemotePage.class ) );
    RemotePage renderer1 = mock( RemotePage.class );
    RemotePage renderer2 = mock( RemotePage.class );

    flow.add( renderer1 );
    flow.add( renderer2 );

    assertSame( renderer2, flow.getCurrentRenderer() );
  }

  @Test
  public void testAddTwoRendererWithPopSetsCurrentToFirst() {
    PageFlow flow = new PageFlow( mock( RemotePage.class ) );
    RemotePage renderer1 = mock( RemotePage.class );
    RemotePage renderer2 = createPage();

    flow.add( renderer1 );
    flow.add( renderer2 );
    PageRenderer popedDescriptor = flow.pop();

    assertSame( renderer1, flow.getCurrentRenderer() );
    assertSame( renderer2, popedDescriptor );
  }

  @Test( expected = IllegalStateException.class )
  public void testPopFaislWithRootOnly() {
    PageFlow flow = new PageFlow( mock( RemotePage.class ) );

    flow.pop();
  }

  @Test
  public void testDestroyDisposesControls() {
    RemotePage root = mock( RemotePage.class );
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    when( root.getDescriptor() ).thenReturn( descriptor );
    PageFlow flow = new PageFlow( root );
    RemotePage renderer1 = createPage();
    RemotePage renderer2 = createPage();
    flow.add( renderer1 );
    flow.add( renderer2 );

    flow.destroy();

    assertTrue( renderer1.getControl().isDisposed() );
    assertTrue( renderer2.getControl().isDisposed() );
  }

  @Test
  public void testDestroyDestroysPageRenderes() {
    RemotePage root = mock( RemotePage.class );
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    when( root.getDescriptor() ).thenReturn( descriptor );
    PageFlow flow = new PageFlow( root );
    RemotePage renderer1 = createPage();
    RemotePage renderer2 = createPage();
    flow.add( renderer1 );
    flow.add( renderer2 );

    flow.destroy();

    InOrder order = inOrder( renderer1, renderer2 );
    order.verify( renderer1 ).destroy();
    order.verify( renderer2 ).destroy();
  }

  @Test
  public void testDestroyDoesNotDisposeRootControl() {
    RemotePage root = createPage();
    PageDescriptor descriptor = root.getDescriptor();
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    PageFlow flow = new PageFlow( root );

    flow.destroy();

    assertFalse( root.getControl().isDisposed() );
  }

  @Test
  public void testDestroyDoesNotDestroyRootPageRenderer() {
    RemotePage root = createPage();
    PageDescriptor descriptor = root.getDescriptor();
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    PageFlow flow = new PageFlow( root );

    flow.destroy();

    verify( root, never() ).destroy();
  }

  @Test
    public void testGetAllRenderers() {
      RemotePage root = mock( RemotePage.class );
      PageDescriptor descriptor = mock( PageDescriptor.class );
      doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
      when( root.getDescriptor() ).thenReturn( descriptor );
      PageFlow flow = new PageFlow( root );
      RemotePage renderer1 = createPage();
      RemotePage renderer2 = createPage();
      flow.add( renderer1 );
      flow.add( renderer2 );

      List<PageRenderer> allPages = flow.getAllRenderers();

      assertEquals( 3, allPages.size() );
      assertSame( root, allPages.get( 0 ) );
      assertSame( renderer1, allPages.get( 1 ) );
      assertSame( renderer2, allPages.get( 2 ) );
    }

  private RemotePage createPage() {
    PageDescriptor descriptor = spy( new PageDescriptor( "foo", TestPage.class ) );
    UI ui = mock( UI.class );
    RemoteUI uiRenderer = mock( RemoteUI.class );
    when( uiRenderer.getRemoteUIId() ).thenReturn( "foo" );
    RemotePage renderer2 = new RemotePage( ui, uiRenderer , descriptor, mock( PageData.class ) );
    renderer2.createControl( shell );
    return spy( renderer2 );
  }
}

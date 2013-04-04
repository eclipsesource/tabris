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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UI;


public class PageFlowTest {

  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    Display display = new Display();
    shell = new Shell( display );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
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
  public void testGetRootPage() {
    RemotePage rootPage = mock( RemotePage.class );
    PageFlow flow = new PageFlow( rootPage );

    assertSame( rootPage, flow.getRootPage() );
  }


  @Test
  public void testgetCurrentPage() {
    RemotePage rootPage = mock( RemotePage.class );
    PageFlow flow = new PageFlow( rootPage );
    RemotePage currentPage = mock( RemotePage.class );

    flow.add( currentPage );

    assertSame( currentPage, flow.getCurrentPage() );
  }

  @Test
  public void testgetPreviousPage() {
    RemotePage previousPage = mock( RemotePage.class );
    PageFlow flow = new PageFlow( previousPage );
    RemotePage currentPage = mock( RemotePage.class );

    flow.add( currentPage );

    assertSame( previousPage, flow.getPreviousPage() );
  }

  @Test
  public void testAddTwoPagesSetsCurrentToLast() {
    PageFlow flow = new PageFlow( mock( RemotePage.class ) );
    RemotePage page1 = mock( RemotePage.class );
    RemotePage page2 = mock( RemotePage.class );

    flow.add( page1 );
    flow.add( page2 );

    assertSame( page2, flow.getCurrentPage() );
  }

  @Test
  public void testAddTwoPageWithPopSetsCurrentToFirst() {
    PageFlow flow = new PageFlow( mock( RemotePage.class ) );
    RemotePage page1 = mock( RemotePage.class );
    RemotePage page2 = createPage();

    flow.add( page1 );
    flow.add( page2 );
    RemotePage popedDescriptor = flow.pop();

    assertSame( page1, flow.getCurrentPage() );
    assertSame( page2, popedDescriptor );
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
    RemotePage page1 = createPage();
    RemotePage page2 = createPage();
    flow.add( page1 );
    flow.add( page2 );

    flow.destroy();

    assertTrue( page1.getControl().isDisposed() );
    assertTrue( page2.getControl().isDisposed() );
  }

  @Test
  public void testDestroyDestroysRemotePages() {
    RemotePage root = mock( RemotePage.class );
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    when( root.getDescriptor() ).thenReturn( descriptor );
    PageFlow flow = new PageFlow( root );
    RemotePage page1 = createPage();
    RemotePage page2 = createPage();
    flow.add( page1 );
    flow.add( page2 );

    flow.destroy();

    InOrder order = inOrder( page1, page2 );
    order.verify( page1 ).destroy();
    order.verify( page2 ).destroy();
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
  public void testDestroyDoesNotDestroyRootRemotePage() {
    RemotePage root = createPage();
    PageDescriptor descriptor = root.getDescriptor();
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    PageFlow flow = new PageFlow( root );

    flow.destroy();

    verify( root, never() ).destroy();
  }

  @Test
  public void testGetAllPages() {
    RemotePage root = mock( RemotePage.class );
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( Boolean.TRUE ).when( descriptor ).isTopLevel();
    when( root.getDescriptor() ).thenReturn( descriptor );
    PageFlow flow = new PageFlow( root );
    RemotePage page1 = createPage();
    RemotePage page2 = createPage();
    flow.add( page1 );
    flow.add( page2 );

    List<RemotePage> allPages = flow.getAllPages();

    assertEquals( 3, allPages.size() );
    assertSame( root, allPages.get( 0 ) );
    assertSame( page1, allPages.get( 1 ) );
    assertSame( page2, allPages.get( 2 ) );
  }

  private RemotePage createPage() {
    PageDescriptor descriptor = spy( new PageDescriptor( "foo", TestPage.class, "", null, false ) );
    UI ui = mock( UI.class );
    RemotePage page2 = new RemotePage( ui, descriptor, "foo", mock( PageData.class ) );
    page2.createControl( shell );
    return spy( page2 );
  }
}

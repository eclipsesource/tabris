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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UI;


public class RemoteRendererFactoryTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testCreatesRemoteUi() {
    RemoteRendererFactory rendererFactory = new RemoteRendererFactory();
    Display display = new Display();
    Shell shell = new Shell( display );

    UIRenderer uiRenderer = rendererFactory.createUIRenderer( shell );

    assertTrue( uiRenderer instanceof RemoteUI );
  }

  @Test
  public void testCreatesRemotePage() {
    RemoteRendererFactory rendererFactory = new RemoteRendererFactory();
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( TestPage.class ).when( descriptor ).getPageType();

    PageRenderer renderer = rendererFactory.createPageRenderer( mock( UI.class ), descriptor, "foo", new PageData() );

    assertTrue( renderer instanceof RemotePage );
  }

  @Test
  public void testCreatesRegularRemoteAction() {
    RemoteRendererFactory rendererFactory = new RemoteRendererFactory();
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( mock( Action.class ) );

    ActionRenderer remoteAction = rendererFactory.createActionRenderer( mock( UI.class ), descriptor, "foo" );

    assertFalse( remoteAction instanceof RemoteSearchAction );
  }

  @Test
  public void testCreatesSearchRemoteAction() {
    RemoteRendererFactory rendererFactory = new RemoteRendererFactory();
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( new TestSearchAction() );

    ActionRenderer remoteAction = rendererFactory.createActionRenderer( mock( UI.class ), descriptor, "foo" );

    assertTrue( remoteAction instanceof RemoteSearchAction );
  }
}

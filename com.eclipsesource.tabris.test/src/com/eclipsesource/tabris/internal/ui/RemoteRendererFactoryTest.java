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

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UI;


@RunWith( RWTRunner.class )
public class RemoteRendererFactoryTest {

  private RemoteRendererFactory rendererFactory;
  private UI ui;
  private RemoteUI uiRenderer;

  @Before
  public void setUp() {
    rendererFactory = ( RemoteRendererFactory )RemoteRendererFactory.getInstance();
    ui = mock( UI.class );
    uiRenderer = mock( RemoteUI.class );
  }

  @Test
  public void testCreatesRemoteUi() {
    Shell shell = new Shell( new Display() );

    UIRenderer uiRenderer = rendererFactory.createUIRenderer( shell );

    assertTrue( uiRenderer instanceof RemoteUI );
  }

  @Test
  public void testCreatesRemotePage() {
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( TestPage.class ).when( descriptor ).getPageType();

    PageRenderer renderer = rendererFactory.createPageRenderer( ui, uiRenderer, descriptor, new PageData() );

    assertTrue( renderer instanceof RemotePage );
  }

  @Test
  public void testCreatesRegularRemoteAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( mock( Action.class ) );

    ActionRenderer remoteAction = rendererFactory.createActionRenderer( ui, uiRenderer, descriptor );

    assertFalse( remoteAction instanceof RemoteSearchAction );
  }

  @Test
  public void testCreatesSearchRemoteAction() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( new TestSearchAction() );

    ActionRenderer remoteAction = rendererFactory.createActionRenderer( ui, uiRenderer, descriptor );

    assertTrue( remoteAction instanceof RemoteSearchAction );
  }

}

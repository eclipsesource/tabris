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
package com.eclipsesource.tabris.internal.ui.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.TestPage;
import com.eclipsesource.tabris.internal.ui.TestSearchAction;
import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.test.RWTEnvironment;
import com.eclipsesource.tabris.ui.Action;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UI;


public class WebRendererFactoryTest {

  @Rule
  public RWTEnvironment environment = new RWTEnvironment();

  private WebRendererFactory rendererFactory;
  private Shell shell;
  private UI ui;
  private WebUI uiRenderer;

  @Before
  public void setUp() {
    shell = new Shell( new Display() );
    rendererFactory = ( WebRendererFactory )WebRendererFactory.getInstance();
    ui = mock( UI.class );
    uiRenderer = mock( WebUI.class );
  }

  @Test
  public void testCreateUIRenderer() {
    UIRenderer renderer = rendererFactory.createUIRenderer( shell );

    assertTrue( renderer instanceof WebUI );
  }

  @Test
  public void testCreatePageRenderer() {
    PageDescriptor descriptor = mock( PageDescriptor.class );
    doReturn( TestPage.class ).when( descriptor ).getPageType();

    PageRenderer renderer = rendererFactory.createPageRenderer( ui, uiRenderer, descriptor, new PageData() );

    assertTrue( renderer instanceof WebPage );
  }

  @Test
  public void testCreateActionRenderer_regular() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( mock( Action.class ) );

    ActionRenderer renderer = rendererFactory.createActionRenderer( ui, uiRenderer, descriptor );

    assertFalse( renderer instanceof WebSearchAction );
  }

  @Test
  public void testCreateActionRenderer_search() {
    ActionDescriptor descriptor = mock( ActionDescriptor.class );
    when( descriptor.getAction() ).thenReturn( new TestSearchAction() );

    ActionRenderer renderer = rendererFactory.createActionRenderer( ui, uiRenderer, descriptor );

    assertTrue( renderer instanceof WebSearchAction );
  }

}

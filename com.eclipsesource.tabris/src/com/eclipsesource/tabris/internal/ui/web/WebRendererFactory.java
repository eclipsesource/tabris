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

import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.RendererFactory;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.action.SearchAction;


public class WebRendererFactory implements RendererFactory {

  private static RendererFactory instance;

  public static RendererFactory getInstance() {
    if( instance == null ) {
      instance = new WebRendererFactory();
    }
    return instance;
  }

  @Override
  public UIRenderer createUIRenderer( Shell shell ) {
    return new WebUI( shell );
  }

  @Override
  public PageRenderer createPageRenderer( UI ui, UIRenderer uiRenderer, PageDescriptor descriptor, PageData data ) {
    return new WebPage( ui, ( WebUI )uiRenderer, descriptor, data );
  }

  @Override
  public ActionRenderer createActionRenderer( UI ui, UIRenderer uiRenderer, ActionDescriptor descriptor ) {
    if( descriptor.getAction() instanceof SearchAction ) {
      return new WebSearchAction( ui, ( WebUI )uiRenderer, descriptor );
    }
    return new WebAction( ui, ( WebUI )uiRenderer, descriptor );
  }

  private WebRendererFactory() {
    // prevent instantiation
  }

}

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

import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.internal.ui.rendering.ActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.PageRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.RendererFactory;
import com.eclipsesource.tabris.internal.ui.rendering.UIRenderer;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UI;
import com.eclipsesource.tabris.ui.action.SearchAction;


public class RemoteRendererFactory implements RendererFactory {

  @Override
  public UIRenderer createUIRenderer( Shell shell ) {
    return new RemoteUI( shell );
  }

  @Override
  public PageRenderer createPageRenderer( UI ui, PageDescriptor descriptor, String parentId, PageData data ) {
    return new RemotePage( ui, descriptor, parentId, data );
  }

  @Override
  public ActionRenderer createActionRenderer( UI ui, ActionDescriptor descriptor, String parentId ) {
    if( descriptor.getAction() instanceof SearchAction ) {
      return new RemoteSearchAction( ui, descriptor, parentId );
    }
    return new RemoteAction( ui, descriptor, parentId );
  }

}

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
package com.eclipsesource.tabris.internal.ui.rendering;

import java.io.Serializable;

import org.eclipse.swt.widgets.Shell;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.ui.PageData;
import com.eclipsesource.tabris.ui.UI;


public interface RendererFactory extends Serializable {

  UIRenderer createUIRenderer( Shell shell );

  PageRenderer createPageRenderer( UI ui, UIRenderer uiRenderer, PageDescriptor descriptor, PageData data );

  ActionRenderer createActionRenderer( UI ui, UIRenderer uiRenderer, ActionDescriptor descriptor );

}

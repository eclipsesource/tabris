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
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageData;


public interface PageRenderer extends Serializable {

  void createActions( RendererFactory rendererFactory, Composite uiParent );

  void update( PageDescriptor descriptor, RendererFactory rendererFactory, Composite uiParent );

  void setTitle( String title );

  void createControl( Composite pageParent );

  Control getControl();

  List<ActionRenderer> getActionRenderers();

  void destroyActions();

  PageDescriptor getDescriptor();

  Page getPage();

  PageData getData();

  void destroy();

}

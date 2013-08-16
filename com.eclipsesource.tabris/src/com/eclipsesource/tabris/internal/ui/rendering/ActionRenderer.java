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

import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.ui.UI;


public interface ActionRenderer {

  void createUi( Composite uiParent );

  void setEnabled( boolean enabled );

  void setVisible( boolean visible );

  void destroy();

  ActionDescriptor getDescriptor();

  UI getUI();
}

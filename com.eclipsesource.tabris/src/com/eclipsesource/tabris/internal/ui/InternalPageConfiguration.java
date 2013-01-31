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

import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.Page;
import com.eclipsesource.tabris.ui.PageConfiguration;


public class InternalPageConfiguration extends PageConfiguration {

  public InternalPageConfiguration( String id, Class<? extends Page> pageType ) {
    super( id, pageType );
  }

  public PageDescriptor createDescriptor() {
    PageDescriptor pageDescriptor = new PageDescriptor( id, pageType, title, image, topLevel, style );
    for( ActionConfiguration configuration : actions ) {
      pageDescriptor.addAction( configuration );
    }
    return pageDescriptor;
  }
}

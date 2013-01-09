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
package com.eclipsesource.tabris.widgets.swipe;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * @since 0.10
 */
public interface SwipeItem {
  
  boolean isPreloadable();

  Control load( Composite parent );
  
  void activate( SwipeContext context );
  
  void deactivate( SwipeContext context );
  
}

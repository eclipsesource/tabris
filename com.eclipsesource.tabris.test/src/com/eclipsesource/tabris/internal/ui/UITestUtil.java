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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


public class UITestUtil {

  public static Image createImage( Display display ) {
    return new Image( display, UITestUtil.class.getResourceAsStream( "testImage.png" ) );
  }
}

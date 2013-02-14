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
package com.eclipsesource.tabris.ui;


/**
 * <p>
 * A {@link PageStyle} defines the style of a {@link Page}. E.g. if a page should be rendered full screen than a
 * {@link PageStyle} needs to be used within a {@link PageConfiguration}.
 * </p>
 *
 * @see PageConfiguration#setStyle(PageStyle...)
 *
 * @since 0.11
 */
public enum PageStyle {

  /**
   * <p>
   * Default page style. Renders a {@link Page} without any special behavior.
   * </p>
   */
  DEFAULT,

  /**
   * <p>
   * When used a {@link Page} will be rendered full screen.
   * </p>
   */
  FULLSCREEN

}

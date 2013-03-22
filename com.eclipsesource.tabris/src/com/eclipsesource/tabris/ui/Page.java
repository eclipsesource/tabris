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

import org.eclipse.swt.widgets.Composite;


/**
 * <p>
 * A {@link Page} object represents a single part of a Tabris UI. A Tabris UI consists of several {@link Page} objects.
 * Usually a {@link Page} takes the most of the device's screen. The responsibility of a {@link Page} is to render the
 * applications content. Basically it's the only point where you will create widgets and other visual representations.
 * </p>
 * <p>
 * {@link Page} implementations will be registered by their type. This is because a page object needs to created
 * newly when it's shown the first time. Let's take a look at an example:
 * </p>
 * <p>
 * When you create a book browsing app you will probably have a book page. In this app you will need to browse through
 * books, right? And the big feature will be that you can browse from book to book in a chain, right? So, as a result
 * a new book page needs to be created for every new book. But when you want to browse back to the last book it
 * doesn't need a new page object because it already exist, right?. And this is exactly how the Tabris UI works.
 * </p>
 * <p>
 * <b>NOTE:</b> A {@link Page} needs to provide a no-argument constructor because instances will be created by the
 *              framework.
 * </p>
 * <p>
 * {@link Page} will be created while browsing forward through an app. But reused when you browse back
 * with {@link PageOperator#closeCurrentPage()}.
 * </p>
 *
 * @see PageConfiguration
 * @see PageOperator
 *
 * @since 0.11
 */
public interface Page {

  /**
   * <p>
   * Will be called by the Tabris UI during the instantiation of a {@link Page}. Within this method the UI code should
   * be created.
   * </p>
   *
   * @param parent the parent to use for creating SWT widgets.
   * @param ui the ui to control the application flow and share data.
   * @since 1.0
   */
  void createContents( Composite parent, UI ui );

  /**
   * <p>
   * Will be called every time the page will be activated regardless if the user browses forward or backward.
   * </p>
   *
   * @since 1.0
   */
  void activate();

  /**
   * <p>
   * Will be called every time the page will be deactivated regardless if the user browses forward or backward.
   * </p>
   *
   * @since 1.0
   */
  void deactivate();

}

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
 * A Tabris UI consists of {@link Page} and {@link Action} objects that are loosely coupled. The point where these are
 * defined is this {@link UIConfiguration} interface. The {@link UIConfiguration#configure(UI, UIContext)} method will
 * be called when the application will be created.
 * </p>
 * <p>
 * Two types of pages exist. These are top level pages and free pages. A top level page marks the beginning of an
 * application flow. E.g. when you developing a book browsing app the top level page may be your shelf from where you
 * can browse the books. Of course in such an app you will need to browse from a book to another book without the way
 * back to the shelf. This means the book page needs to be a free page that can be chained to another.</br>
 * This is what free pages are, the second type of pages. They can appear everywhere in the application. The depth
 * level of such a page is not defined.
 * </p>
 * <p>
 * Actions also exist in two types. The first type are global actions. Global actions are visible in the whole
 * application regardless on which page you are currently on. The other type of actions are page actions. These
 * actions are having the same lifecycle as a Pages and they are also only visible when the related page is visible.
 * </p>
 * <p>
 * Your responsibility as a developer is to define the different pages of your application and decide which pages
 * should be top level pages and which are free pages. This is the same with actions. When this task is done these
 * pages needs to be implemented and hooked into the Tabris UI within the
 * {@link UIConfiguration#configure(UI, UIContext)} method. An instance of a {@link UIConfiguration} implementation
 * needs to be passed into the constructor of a {@link TabrisUI} object.
 * </p>
 *
 * @see TabrisUI
 * @see Page
 * @see PageConfiguration
 * @see Action
 * @see ActionConfiguration
 *
 * @since 0.11
 */
public interface UIConfiguration {

  /**
   * <p>
   * When you implement a {@link UIConfiguration} you need to add your {@link Page}s and {@link Action}s to the passed
   * in {@link UI} object. The {@link UIContext} can be used to store global data with application scope to allwo the
   * pages/actions to share data globally.
   * </p>
   *
   * @param ui the {@link UI} instance to add {@link Page}s and {@link Action}s.
   * @param context the {@link UIContext} to store global data.
   *
   * @see UIContext
   */
  void configure( UI ui, UIContext context );
}

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

import com.eclipsesource.tabris.Store;


/**
 * <p>
 * The {@link PageManager} can be used to handle pages. This includes the navigation or the data transmission between
 * single pages. To get an instance use {@link UIContext#getPageManager()}.
 * </p>
 *
 * @see UIContext
 * @see Page
 *
 * @since 0.11
 */
public interface PageManager {

  /**
   * <p>
   * When called the specified page will be shown on the client device.
   * </p>
   *
   * @param pageId the Id of the page to show. Must not be empty or <code>null</code>.
   *
   * @throws IllegalStateException when no page exist for the given id.
   */
  void showPage( String pageId ) throws IllegalStateException;

  /**
   * <p>
   * When called the specified page will be shown on the client device. The passed in store will be the page store for
   * the new page. See {@link PageManager#getPageStore()}.
   * </p>
   *
   * @param pageId the Id of the page to show. Must not be empty or <code>null</code>.
   * @param store the page store for the new page. Must not be <code>null</code>.
   *
   * @throws IllegalStateException when no page exist for the given id.
   */
  void showPage( String pageId, Store store );

  /**
   * <p>
   * Shows the last page and destroys the current active page.
   * </p>
   *
   * @return <code>true</code> when the back navigation was successful. When <code>false</code> a back navigation was
   *         not possible e.g. when the current active page is a top level page.
   */
  boolean showPreviousPage();

  /**
   * <p>
   * Returns the current visible {@link Page} object.
   * </p>
   */
  Page getPage();

  /**
   * <p>
   * As you may have seen there is a global store in {@link UIContext#getGlobalStore()} to store data application wide.
   * But in most cases global data is not necessary. For this every page has it's own {@link Store}. You can influence
   * this store by calling the {@link PageManager#showPage(String, Store)} method with a new {@link Store}. This method
   * returns the {@link Store} for the current active page.
   * </p>
   */
  Store getPageStore();
}

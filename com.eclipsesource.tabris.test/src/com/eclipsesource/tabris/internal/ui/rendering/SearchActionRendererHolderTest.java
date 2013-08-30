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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.rendering.SearchActionRenderer;
import com.eclipsesource.tabris.internal.ui.rendering.SearchActionRendererHolder;


public class SearchActionRendererHolderTest {

  private SearchActionRendererHolder holder;

  @Before
  public void setUp() {
    holder = new SearchActionRendererHolder();
  }

  @Test
  public void testGetSearchActionRenderer_nullByDefault() {
    SearchActionRenderer renderer = holder.getSearchActionRenderer();

    assertNull( renderer );
  }

  @Test
  public void testGetSearchActionRenderer_savesRenderer() {
    SearchActionRenderer renderer = mock( SearchActionRenderer.class );

    holder.setSearchActionRenderer( renderer );
    SearchActionRenderer actualRenderer = holder.getSearchActionRenderer();

    assertSame( renderer, actualRenderer );
  }

}

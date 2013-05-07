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
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.PAGING;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.TabFolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith( MockitoJUnitRunner.class )
public class TabFolderDecoratorTest {

  @Mock
  private TabFolder folder;
  private TabFolderDecorator decorator;

  @Before
  public void setUp() {
    decorator = Widgets.onTabFolder( folder );
  }

  @Test
  public void testUsesPaging() {
    decorator.usePaging();

    verify( folder ).setData( PAGING.getKey(), Boolean.TRUE );
  }
}

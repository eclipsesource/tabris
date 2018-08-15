/*******************************************************************************
 * Copyright (c) 2018 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ACTION_STYLE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.MenuItem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;

@RunWith( MockitoJUnitRunner.class )
public class MenuItemDecoratorTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();
  private MenuItemDecorator decorator;
  private MenuItem menuItem;

  @Before
  public void setUp() {
    menuItem = mock( MenuItem.class );
    decorator = new MenuItemDecorator( menuItem );
  }

  @Test
  public void testDestructiveStyle() {
    decorator.useDestructiveStyle();

    verify( menuItem ).setData( ACTION_STYLE.getKey(), "destructive" );
  }

}

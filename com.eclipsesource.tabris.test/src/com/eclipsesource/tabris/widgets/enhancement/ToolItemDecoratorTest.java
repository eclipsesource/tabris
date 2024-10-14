/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.TITLE;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.ToolItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith( MockitoJUnitRunner.class )
public class ToolItemDecoratorTest {

  @Mock
  private ToolItem toolItem;
  private ToolItemDecorator decorator;

  @Before
  public void setUp() {
    decorator = Widgets.onToolItem( toolItem );
  }

  @Test
  public void testUseNumbersAndPunctuationKeyboard() {
    decorator.useAsTitle();

    verify( toolItem ).setData( TITLE.getKey(), Boolean.TRUE );
  }

}

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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ALT_SELECTION;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith( MockitoJUnitRunner.class )
public class ListDecoratorTest {

  @Mock
  private List list;
  private ListDecorator decorator;

  @Before
  public void setUp() {
    decorator = Widgets.onList( list );
  }

  @Test
  public void testUseTitle() {
    decorator.useTitle( "test" );

    verify( list ).setToolTipText( eq( "test" ) );
  }

  @Test
  public void testEnableAlternativeSelection() {
    decorator.enableAlternativeSelection();

    verify( list ).setData( ALT_SELECTION.getKey(), "all" );
  }

}
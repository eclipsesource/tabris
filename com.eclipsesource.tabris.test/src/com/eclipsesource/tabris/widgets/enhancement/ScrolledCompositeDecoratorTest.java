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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.PAGING;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.custom.ScrolledComposite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith( MockitoJUnitRunner.class )
public class ScrolledCompositeDecoratorTest {

  @Mock
  private ScrolledComposite composite;
  private ScrolledCompositeDecorator decorator;

  @Before
  public void setUp() {
    decorator = Widgets.onScrolledComposite( composite );
  }

  @Test
  public void testUsesPaging() {
    decorator.usePaging();

    verify( composite ).setData( PAGING.getKey(), Boolean.TRUE );
  }

}

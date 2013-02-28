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

import static com.eclipsesource.tabris.internal.WidgetsUtil.TABRIS_VARIANT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.swt.widgets.Composite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.widgets.ScrollingComposite;


@RunWith( MockitoJUnitRunner.class )
public class ScrollingCompositeDecoratorTest {

  @Mock
  private ScrollingComposite composite;
  private ScrollingCompositeDecorator decorator;

  @Before
  public void setUp() {
    decorator = Widgets.onScrollingComposite( composite );
  }

  @Test
  public void testUsesPaging() {
    Composite parent = mock( Composite.class );
    when( composite.getParent() ).thenReturn( parent );
    decorator.usePaging();

    verify( parent ).setData( TABRIS_VARIANT, "PAGINGENABLED" );
  }

}

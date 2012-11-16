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
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.Label;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.widgets.enhancement.LabelDecorator;
import com.eclipsesource.tabris.widgets.enhancement.Widgets;


@RunWith( MockitoJUnitRunner.class )
public class LabelDecoratorTest {
  
  @Mock
  private Label label;
  private LabelDecorator decorator;
  
  @Before
  public void setUp() {
    decorator = Widgets.onLabel( label );
  }
  
  @Test
  public void testUseNumbersAndPunctuationKeyboard() {
    decorator.useZoom();
    
    verify( label ).setData( TABRIS_VARIANT, "ZOOM" );
  }
  
}

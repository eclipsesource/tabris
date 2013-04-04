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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ANIMATED;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.SHOW_TOUCH;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.Serializable;

import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith( MockitoJUnitRunner.class )
public class WidgetDecoratorTest {

  @Mock
  private Widget widget;
  private WidgetDecorator decorator;

  @Before
  public void setUp() {
    decorator = Widgets.onWidget( widget );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( WidgetDecorator.class ) );
  }

  @Test
  public void testUseAnimation() {
    decorator.useAnimation();

    verify( widget ).setData( ANIMATED.getKey(), Boolean.TRUE );
  }

  @Test
  public void testShowLocalTouch() {
    decorator.showLocalTouch();

    verify( widget ).setData( SHOW_TOUCH.getKey(), Boolean.TRUE );
  }

}

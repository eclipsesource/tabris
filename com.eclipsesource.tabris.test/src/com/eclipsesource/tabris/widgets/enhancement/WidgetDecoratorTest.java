/*******************************************************************************
 * Copyright (c) 2012, 2017 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.SHOW_TOUCH;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.Serializable;

import org.eclipse.rap.rwt.RWT;
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
  public void testShowLocalTouch() {
    decorator.showLocalTouch( true );

    verify( widget ).setData( SHOW_TOUCH.getKey(), Boolean.TRUE );
  }

  @Test
  public void testHideLocalTouch() {
    decorator.showLocalTouch( false );

    verify( widget ).setData( SHOW_TOUCH.getKey(), Boolean.FALSE );
  }

  @Test
  public void testEnablesMarkup() {
    decorator.enableMarkup( true );

    verify( widget ).setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
  }

  @Test
  public void testDisablesMarkup() {
    decorator.enableMarkup( false );

    verify( widget ).setData( RWT.MARKUP_ENABLED, Boolean.FALSE );
  }

}

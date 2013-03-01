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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.KEYBOARD;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith( MockitoJUnitRunner.class )
public class TextDecoratorTest {

  @Mock
  private Text text;
  private TextDecorator decorator;

  @Before
  public void setUp() {
    decorator = Widgets.onText( text );
  }

  @Test
  public void testUseHintText() {
    decorator.useHintText( "test" );

    verify( text ).setMessage( eq( "test" ) );
  }

  @Test
  public void testUseAsciiKeyboard() {
    decorator.useAsciiKeyboard();

    verify( text ).setData( KEYBOARD.getKey(), "ascii" );
  }

  @Test
  public void testUseNumbersAndPunctuationKeyboard() {
    decorator.useNumbersAndPunctuationKeyboard();

    verify( text ).setData( KEYBOARD.getKey(), "numbersAndPunctuation" );
  }

  @Test
  public void testUseUrlKeyboard() {
    decorator.useUrlKeyboard();

    verify( text ).setData( KEYBOARD.getKey(), "url" );
  }

  @Test
  public void testUseNumberKeyboard() {
    decorator.useNumberKeyboard();

    verify( text ).setData( KEYBOARD.getKey(), "number" );
  }

  @Test
  public void testUsePhoneKeyboard() {
    decorator.usePhoneKeyboard();

    verify( text ).setData( KEYBOARD.getKey(), "phone" );
  }

  @Test
  public void testUseEmailKeyboard() {
    decorator.useEmailKeyboard();

    verify( text ).setData( KEYBOARD.getKey(), "email" );
  }

  @Test
  public void testUseDecimalKeyboard() {
    decorator.useDecimalKeyboard();

    verify( text ).setData( KEYBOARD.getKey(), "decimal" );
  }

}

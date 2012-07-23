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
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.WidgetsUtil.TABRIS_VARIANT;
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
    
    verify( text ).setData( TABRIS_VARIANT, "KEYBOARD_ASCII" );
  }
  
  @Test
  public void testUseNumbersAndPunctuationKeyboard() {
    decorator.useNumbersAndPunctuationKeyboard();
    
    verify( text ).setData( TABRIS_VARIANT, "KEYBOARD_NUMBERSANDPUNCTUATION" );
  }
  
  @Test
  public void testUseUrlKeyboard() {
    decorator.useUrlKeyboard();
    
    verify( text ).setData( TABRIS_VARIANT, "KEYBOARD_URL" );
  }
  
  @Test
  public void testUseNumberKeyboard() {
    decorator.useNumberKeyboard();
    
    verify( text ).setData( TABRIS_VARIANT, "KEYBOARD_NUMBER" );
  }
  
  @Test
  public void testUsePhoneKeyboard() {
    decorator.usePhoneKeyboard();
    
    verify( text ).setData( TABRIS_VARIANT, "KEYBOARD_PHONE" );
  }
  
  @Test
  public void testUseEmailKeyboard() {
    decorator.useEmailKeyboard();
    
    verify( text ).setData( TABRIS_VARIANT, "KEYBOARD_EMAIL" );
  }
  
  @Test
  public void testUseDecimalKeyboard() {
    decorator.useDecimalKeyboard();
    
    verify( text ).setData( TABRIS_VARIANT, "KEYBOARD_DECIMAL" );
  }
  
}

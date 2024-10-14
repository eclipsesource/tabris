/*******************************************************************************
 * Copyright (c) 2012, 2020 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.AUTO_CAPITALIZE;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.AUTO_CORRECT;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.DISABLE_LOOKUP_ACTION;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.DISABLE_SHARE_ACTION;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ENTER_KEY_TYPE;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.KEYBOARD;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.KEYBOARD_APPEARANCE_MODE;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.LOCAL_CLIPBOARD;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.TEXT_REPLACEMENT;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.eclipsesource.tabris.test.util.TabrisEnvironment;


@RunWith( MockitoJUnitRunner.Silent.class )
public class TextDecoratorTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Mock
  private Text text;
  private TextDecorator decorator;

  @Before
  public void setUp() {
    decorator = Widgets.onText( text );
  }

  @Test
  public void testSetAutoCorrectEnabled() {
    decorator.setAutoCorrectionEnabled( true );

    verify( text ).setData( AUTO_CORRECT.getKey(), Boolean.valueOf( true ) );
  }

  @Test
  public void testSetAutoCorrectDisabled() {
    decorator.setAutoCorrectionEnabled( false );

    verify( text ).setData( AUTO_CORRECT.getKey(), Boolean.valueOf( false ) );
  }

  @Test
  public void testSetAutoCapitalize_ALL() {
    decorator.setAutoCapitalization( TextDecorator.AutoCapitalization.ALL );

    verify( text ).setData( AUTO_CAPITALIZE.getKey(), "ALL" );
  }

  @Test
  public void testSetAutoCapitalize_NONE() {
    decorator.setAutoCapitalization( TextDecorator.AutoCapitalization.NONE );

    verify( text ).setData( AUTO_CAPITALIZE.getKey(), "NONE" );
  }

  @Test
  public void testSetAutoCapitalize_SENTENCE() {
    decorator.setAutoCapitalization( TextDecorator.AutoCapitalization.SENTENCE );

    verify( text ).setData( AUTO_CAPITALIZE.getKey(), "SENTENCE" );
  }

  @Test
  public void testSetAutoCapitalize_WORD() {
    decorator.setAutoCapitalization( TextDecorator.AutoCapitalization.WORD );

    verify( text ).setData( AUTO_CAPITALIZE.getKey(), "WORD" );
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

  @Test
  public void testUseLocalClipboard() {
    decorator.useLocalClipboard();

    verify( text ).setData( LOCAL_CLIPBOARD.getKey(), Boolean.TRUE );
  }

  @Test
  public void testDisableLookupAction() {
    decorator.disableLookupAction();
    verify( text ).setData( DISABLE_LOOKUP_ACTION.getKey(), Boolean.TRUE );
  }
   @Test
  public void testDisableShareAction() {
    decorator.disableShareAction();
    verify( text ).setData( DISABLE_SHARE_ACTION.getKey(), Boolean.TRUE );
  }

  @Test
  public void testSetsTextReplacement() {
    TextReplacementData data = mock( TextReplacementData.class );
    when( data.getId() ).thenReturn( "r42" );

    decorator.setTextReplacement( data );

    verify( text ).setData( TEXT_REPLACEMENT.getKey(), "r42" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetTextReplacementFailsWithNullData() {
    decorator.setTextReplacement( null );
  }

  @Test
  public void testDoesNotSetTextReplacementWithWebClient() {
    environment.setClient( mock( WebClient.class ) );
    TextReplacementData data = mock( TextReplacementData.class );
    when( data.getId() ).thenReturn( "r42" );

    decorator.setTextReplacement( data );

    verify( text, never() ).setData( TEXT_REPLACEMENT.getKey(), "r42" );
  }

  @Test
  public void testSetKeyboardAppearanceMode_NEVER() {
    decorator.setKeyboardAppearanceMode( TextDecorator.KeyboardAppearance.NEVER );

    verify( text ).setData( KEYBOARD_APPEARANCE_MODE.getKey(), "NEVER" );
  }

  @Test
  public void testSetKeyboardAppearanceMode_ON_TOUCH() {
    decorator.setKeyboardAppearanceMode( TextDecorator.KeyboardAppearance.ON_TOUCH );

    verify( text ).setData( KEYBOARD_APPEARANCE_MODE.getKey(), "ON_TOUCH" );
  }

  @Test
  public void testSetKeyboardAppearanceMode_ON_FOCUS() {
    decorator.setKeyboardAppearanceMode( TextDecorator.KeyboardAppearance.ON_FOCUS );

    verify( text ).setData( KEYBOARD_APPEARANCE_MODE.getKey(), "ON_FOCUS" );
  }

  @Test
  public void testSetEnterKeyType_DEFAULT() {
    decorator.setEnterKeyType( TextDecorator.EnterKeyType.DEAFAUL );

    verify( text ).setData( ENTER_KEY_TYPE.getKey(), "DEAFAUL" );
  }

  @Test
  public void testSetEnterKeyType_DONE() {
    decorator.setEnterKeyType( TextDecorator.EnterKeyType.DONE );

    verify( text ).setData( ENTER_KEY_TYPE.getKey(), "DONE" );
  }

  @Test
  public void testSetEnterKeyType_NEXT() {
    decorator.setEnterKeyType( TextDecorator.EnterKeyType.NEXT );

    verify( text ).setData( ENTER_KEY_TYPE.getKey(), "NEXT" );
  }

  @Test
  public void testSetEnterKeyType_SEND() {
    decorator.setEnterKeyType( TextDecorator.EnterKeyType.SEND );

    verify( text ).setData( ENTER_KEY_TYPE.getKey(), "SEND" );
  }

  @Test
  public void testSetEnterKeyType_SEARCH() {
    decorator.setEnterKeyType( TextDecorator.EnterKeyType.SEARCH );

    verify( text ).setData( ENTER_KEY_TYPE.getKey(), "SEARCH" );
  }

  @Test
  public void testSetEnterKeyType_GO() {
    decorator.setEnterKeyType( TextDecorator.EnterKeyType.GO );

    verify( text ).setData( ENTER_KEY_TYPE.getKey(), "GO" );
  }

}

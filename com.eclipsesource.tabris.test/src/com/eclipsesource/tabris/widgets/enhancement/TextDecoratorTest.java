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

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.AUTO_CAPITALIZE;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.AUTO_CORRECT;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.KEYBOARD;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.TEXT_REPLACEMENT;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.TabrisClient;


@RunWith( MockitoJUnitRunner.class )
public class TextDecoratorTest {

  @Mock
  private Text text;
  private TextDecorator decorator;

  @Before
  public void setUp() {
    Fixture.setUp();
    decorator = Widgets.onText( text );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
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
  public void testSetAutoCapitalizeEnabled() {
    decorator.setAutoCapitalizationEnabled( true );

    verify( text ).setData( AUTO_CAPITALIZE.getKey(), Boolean.valueOf( true ) );
  }

  @Test
  public void testSetAutoCapitalizeDisabled() {
    decorator.setAutoCapitalizationEnabled( false );

    verify( text ).setData( AUTO_CAPITALIZE.getKey(), Boolean.valueOf( false ) );
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
  public void testSetsTextReplacement() {
    Fixture.fakeClient( mock( TabrisClient.class ) );
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
    Fixture.fakeClient( mock( WebClient.class ) );
    TextReplacementData data = mock( TextReplacementData.class );
    when( data.getId() ).thenReturn( "r42" );

    decorator.setTextReplacement( data );

    verify( text, never() ).setData( TEXT_REPLACEMENT.getKey(), "r42" );
  }

}

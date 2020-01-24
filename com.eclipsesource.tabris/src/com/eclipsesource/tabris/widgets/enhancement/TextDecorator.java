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


import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.AUTO_CAPITALIZE;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.AUTO_CORRECT;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.DISABLE_LOOKUP_ACTION;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.DISABLE_SHARE_ACTION;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.ENTER_KEY_TYPE;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.KEYBOARD;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.KEYBOARD_APPEARANCE_MODE;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.LOCAL_CLIPBOARD;
import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.TEXT_REPLACEMENT;
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Text;

import com.eclipsesource.tabris.TabrisClient;


/**
 * @since 0.8
 */
public class TextDecorator extends WidgetDecorator<TextDecorator> {

  /**
   * Controls how text input is capitalized.
   *
   * @since 3.4
   */
  public enum AutoCapitalization {
    /**
     * Do not change any text input.
     */
    NONE,
    /**
     * Capitalize the first word of a sentence.
     */
    SENTENCE,
    /**
     * Capitalize every word.
     */
    WORD,
    /**
     * Capitalize every letter.
     */
    ALL
  }

  /**
   * Controls the appearance of virtual keyboard.
   *
   * @since 3.6
   */
  public enum KeyboardAppearance {
    /**
     * The keyboard is never shown when focus is gained.
     */
    NEVER,
    /**
     * The keyboard is not shown when the focus is set programmatically. Only the blinking cursor will be shown.
     * Touching the `Text` widget will show the keyboard.
     */
    ON_TOUCH,
    /**
     * The keyboard is always shown when the `Text` widget gains focus.
     */
    ON_FOCUS
  }

  /**
   * Label or icon to display on the keyboard ‘confirmation’ key.
   *
   * @since 3.10
   */
  public enum EnterKeyType {
    DEAFAUL,
    DONE,
    NEXT,
    SEND,
    SEARCH,
    GO
  }

  private final Text text;

  TextDecorator( Text text ) {
    super( text );
    this.text = text;
  }

  /**
   * <p>
   * Defines the grayed text within a text field. This text will be removed when a user selects the {@link Text}
   * widget.
   * </p>
   *
   * @since 0.8
   */
  public TextDecorator useHintText( String hint ) {
    text.setMessage( hint );
    return this;
  }

  /**
   * <p>
   * Instructs a client to enable/disable auto capitalization on a {@link Text} widget.
   * </p>
   *
   * @since 3.4
   */
  public TextDecorator setAutoCapitalization( AutoCapitalization capitalization ) {
    setData( text, AUTO_CAPITALIZE, capitalization.toString() );
    return this;
  }

  /**
   * <p>
   * Instructs a client to enable/disable auto correction on a {@link Text} widget.
   * </p>
   *
   * @since 1.4
   */
  public TextDecorator setAutoCorrectionEnabled( boolean enabled ) {
    setData( text, AUTO_CORRECT, Boolean.valueOf( enabled ) );
    return this;
  }

  /**
   * <p>
   * Instructs the {@link Text} to open a keyboard with an ascii layout when the user wan't to type text.
   * </p>
   *
   * @since 0.8
   */
  public TextDecorator useAsciiKeyboard() {
    setData( text, KEYBOARD, "ascii" );
    return this;
  }

  /**
   * <p>
   * Instructs the {@link Text} to open a keyboard with a numbers and punctuation layout when the user wan't to
   * type text.
   * </p>
   *
   * @since 0.8
   */
  public TextDecorator useNumbersAndPunctuationKeyboard() {
    setData( text, KEYBOARD, "numbersAndPunctuation" );
    return this;
  }

  /**
   * <p>
   * Instructs the {@link Text} to open a keyboard with an url layout when the user wan't to type text.
   * </p>
   *
   * @since 0.8
   */
  public TextDecorator useUrlKeyboard() {
    setData( text, KEYBOARD, "url" );
    return this;
  }

  /**
   * <p>
   * Instructs the {@link Text} to open a keyboard with a number layout when the user wan't to type text.
   * </p>
   *
   * @since 0.8
   */
  public TextDecorator useNumberKeyboard() {
    setData( text, KEYBOARD, "number" );
    return this;
  }

  /**
   * <p>
   * Instructs the {@link Text} to open a keyboard with a phone layout when the user wan't to type text.
   * </p>
   *
   * @since 0.8
   */
  public TextDecorator usePhoneKeyboard() {
    setData( text, KEYBOARD, "phone" );
    return this;
  }

  /**
   * <p>
   * Instructs the {@link Text} to open a keyboard with an email layout when the user wan't to type text.
   * </p>
   *
   * @since 0.8
   */
  public TextDecorator useEmailKeyboard() {
    setData( text, KEYBOARD, "email" );
    return this;
  }

  /**
   * <p>
   * Instructs the {@link Text} to open a keyboard with a decimal layout when the user wan't to type text.
   * </p>
   *
   * @since 0.8
   */
  public TextDecorator useDecimalKeyboard() {
    setData( text, KEYBOARD, "decimal" );
    return this;
  }

  /**
   * <p>
   * Instructs the {@link Text} to keep cut / copied text on client
   * (i.e. disable 'clipboard sharing' on iOs devices). iOS only.
   * </p>
   *
   * @since 3.4
   */
  public TextDecorator useLocalClipboard() {
    setData( text, LOCAL_CLIPBOARD, Boolean.TRUE );
    return this;
  }

  /**
   * <p>
   * Instructs the {@link Text} to disable the iOs 'Lookup' action.
   * </p>
   *
   * @since 3.6
   */
  public TextDecorator disableLookupAction() {
    setData( text, DISABLE_LOOKUP_ACTION, Boolean.TRUE );
    return this;
  }
   /**
   * <p>
   * Instructs the {@link Text} to disable the iOs 'Share' action.
   * </p>
   *
   * @since 3.6
   */
  public TextDecorator disableShareAction() {
    setData( text, DISABLE_SHARE_ACTION, Boolean.TRUE );
    return this;
  }

  /**
   * <p>
   * Sets the {@link TextReplacementData} on the decorated {@link Text} widget.
   * </p>
   *
   * @see TextReplacementData
   *
   * @since 1.4
   */
  public TextDecorator setTextReplacement( TextReplacementData data ) {
    whenNull( data ).throwIllegalArgument( "TextReplacementData must not be null" );
    if( RWT.getClient() instanceof TabrisClient ) {
      setData( text, TEXT_REPLACEMENT, data.getId() );
    }
    return this;
  }

  /**
   * <p>
   * Allows to control when to show the virtual keyboard on a {@link Text} widget on Android.
   * </p>
   *
   * @since 3.10
   */
  public TextDecorator setKeyboardAppearanceMode ( KeyboardAppearance appearance ) {
    setData( text, KEYBOARD_APPEARANCE_MODE, appearance.toString() );
    return this;
  }

  /**
   * <p>
   * Allows to control label or icon to display on the keyboard ‘confirmation’ key.
   * Setting an <code>enterKeyType</code> other than default will change the key
   * behavior to not close the keyboard automatically.
   * </p>
   *
   * @since 3.10
   */
  public TextDecorator setEnterKeyType ( EnterKeyType type ) {
    setData( text, ENTER_KEY_TYPE, type.toString() );
    return this;
  }

}

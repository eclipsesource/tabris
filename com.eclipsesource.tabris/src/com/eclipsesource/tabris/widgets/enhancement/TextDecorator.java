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
import static com.eclipsesource.tabris.internal.WidgetsUtil.setData;

import org.eclipse.swt.widgets.Text;


/**
 * @since 0.8
 */
public class TextDecorator extends WidgetDecorator<TextDecorator> {

  private final Text text;

  TextDecorator( Text text ) {
    super( text );
    this.text = text;
  }

  public TextDecorator useHintText( String hint ) {
    text.setMessage( hint );
    return this;
  }

  public TextDecorator useAsciiKeyboard() {
    setData( text, KEYBOARD, "ascii" );
    return this;
  }

  public TextDecorator useNumbersAndPunctuationKeyboard() {
    setData( text, KEYBOARD, "numbersAndPunctuation" );
    return this;
  }

  public TextDecorator useUrlKeyboard() {
    setData( text, KEYBOARD, "url" );
    return this;
  }

  public TextDecorator useNumberKeyboard() {
    setData( text, KEYBOARD, "number" );
    return this;
  }

  public TextDecorator usePhoneKeyboard() {
    setData( text, KEYBOARD, "phone" );
    return this;
  }

  public TextDecorator useEmailKeyboard() {
    setData( text, KEYBOARD, "email" );
    return this;
  }

  public TextDecorator useDecimalKeyboard() {
    setData( text, KEYBOARD, "decimal" );
    return this;
  }

}

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


import static com.eclipsesource.tabris.internal.WidgetsUtil.setVariant;

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
    setVariant( text, "KEYBOARD_ASCII" );
    return this;
  }

  public TextDecorator useNumbersAndPunctuationKeyboard() {
    setVariant( text, "KEYBOARD_NUMBERSANDPUNCTUATION" );
    return this;
  }
  
  public TextDecorator useUrlKeyboard() {
    setVariant( text, "KEYBOARD_URL" );
    return this;
  }
  
  public TextDecorator useNumberKeyboard() {
    setVariant( text, "KEYBOARD_NUMBER" );
    return this;
  }
  
  public TextDecorator usePhoneKeyboard() {
    setVariant( text, "KEYBOARD_PHONE" );
    return this;
  }
  
  public TextDecorator useEmailKeyboard() {
    setVariant( text, "KEYBOARD_EMAIL" );
    return this;
  }
  
  public TextDecorator useDecimalKeyboard() {
    setVariant( text, "KEYBOARD_DECIMAL" );
    return this;
  }
  
}

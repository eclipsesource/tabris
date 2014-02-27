/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.passepartout.internal.instruction;

import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;

import org.eclipse.swt.graphics.Font;

import com.eclipsesource.tabris.passepartout.Instruction;


public class FontInstruction implements Instruction {

  private final Font font;

  public FontInstruction( Font font ) {
    whenNull( font ).throwIllegalArgument( "Font must not be null" );
    this.font = font;
  }

  public Font getFont() {
    return font;
  }
}

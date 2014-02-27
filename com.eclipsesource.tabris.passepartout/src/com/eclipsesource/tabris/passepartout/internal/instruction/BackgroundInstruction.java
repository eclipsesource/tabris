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

import org.eclipse.swt.graphics.Color;

import com.eclipsesource.tabris.passepartout.Instruction;


public class BackgroundInstruction implements Instruction {

  private final Color color;

  public BackgroundInstruction( Color color ) {
    whenNull( color ).throwIllegalArgument( "Color must not be null" );
    this.color = color;
  }

  public Color getColor() {
    return color;
  }
}

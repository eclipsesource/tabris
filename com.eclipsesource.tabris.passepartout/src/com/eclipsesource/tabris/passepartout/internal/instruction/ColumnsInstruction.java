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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.when;

import com.eclipsesource.tabris.passepartout.Instruction;


public class ColumnsInstruction implements Instruction {

  private final int columns;

  public ColumnsInstruction( int columns ) {
    when( columns < 0 ).throwIllegalArgument( "Columns most be >= 0 buts was " + columns );
    this.columns = columns;
  }

  public int getColumns() {
    return columns;
  }
}

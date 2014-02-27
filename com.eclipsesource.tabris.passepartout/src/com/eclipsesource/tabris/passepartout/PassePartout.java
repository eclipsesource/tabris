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
package com.eclipsesource.tabris.passepartout;

import java.math.BigDecimal;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.eclipsesource.tabris.passepartout.internal.QueryImpl;
import com.eclipsesource.tabris.passepartout.internal.ResourceImpl;
import com.eclipsesource.tabris.passepartout.internal.condition.MaxWidthCondition;
import com.eclipsesource.tabris.passepartout.internal.condition.MinWidthCondition;
import com.eclipsesource.tabris.passepartout.internal.instruction.BackgroundImageInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.BackgroundInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ColumnsInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ExcludeInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.FontInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ForegroundInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.HeightInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.ImageInstruction;
import com.eclipsesource.tabris.passepartout.internal.instruction.MarginInstruction;
import com.eclipsesource.tabris.passepartout.internal.unit.Em;
import com.eclipsesource.tabris.passepartout.internal.unit.Percentage;
import com.eclipsesource.tabris.passepartout.internal.unit.Pixel;


public class PassePartout {

  public static FluidGridLayout createGrid() {
    return createGrid( new FluidGridConfiguration( LayoutMode.AUTO, 720, 1872 ) );
  }

  public static FluidGridLayout createGrid( FluidGridConfiguration configuration ) {
    return new FluidGridLayout( configuration );
  }

  public static FluidGridData createGridData( Rule... rules  ) {
    FluidGridData gridData = new FluidGridData();
    for( Rule rule : rules ) {
      gridData.addRule( rule );
    }
    return gridData;
  }

  public static Resource createResource( Rule... rules ) {
    return new ResourceImpl( rules );
  }

  public static Query when( Condition condition ) {
    return new QueryImpl( condition );
  }

  public static Condition minWidth( Unit minWidth ) {
    return new MinWidthCondition( minWidth );
  }

  public static Condition maxWidth( Unit maxWidth ) {
    return new MaxWidthCondition( maxWidth );
  }

  public static Unit em( double value ) {
    return new Em( BigDecimal.valueOf( value ) );
  }

  public static Unit percent( double value ) {
    return new Percentage( BigDecimal.valueOf( value ) );
  }

  public static Unit px( int value ) {
    return new Pixel( value );
  }

  public static Instruction columns( int columns ) {
    return new ColumnsInstruction( columns );
  }

  public static Instruction height( Unit unit ) {
    return new HeightInstruction( unit );
  }

  public static Instruction margins( Unit top, Unit right, Unit bottom, Unit left ) {
    return new MarginInstruction( top, right, bottom, left );
  }

  public static Instruction exclude() {
    return new ExcludeInstruction();
  }

  public static Instruction font( Font font ) {
    return new FontInstruction( font );
  }

  public static Instruction foreground( Color color ) {
    return new ForegroundInstruction( color );
  }

  public static Instruction background( Color color ) {
    return new BackgroundInstruction( color );
  }

  public static Instruction image( Image image ) {
    return new ImageInstruction( image );
  }

  public static Instruction backgroundImage( Image image ) {
    return new BackgroundImageInstruction( image );
  }

  private PassePartout() {
    // prevent instantiation
  }

}

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


/**
 * <p>
 * The whole Passe-Partout API uses {@link Query}s, {@link Condition}s and {@link Instruction}s to define adjustable UI
 * components like a layout or resources. The {@link PassePartout} class acts as a facade to create {@link Condition}s,
 * {@link Instruction}s and {@link Unit}s of specific types.
 * </p>
 *
 * @see FluidGridLayout
 * @see Resource
 *
 * @since 0.9
 */
public final class PassePartout {

  /**
   * <p>
   * Creates a {@link FluidGridLayout} with a default configuration. The configuration defined uses the
   * {@link LayoutMode#AUTO} and the following switches for column folding:
   * <ul>
   *   <li>0px - 719px = 4 columns</li>
   *   <li>720px - 1871px = 8 columns</li>
   *   <li>1872px - unlimited = 16 columns</li>
   * </ul>
   * </p>
   *
   * @see FluidGridLayout
   */
  public static FluidGridLayout createFluidGrid() {
    return createFluidGrid( new FluidGridConfiguration( LayoutMode.AUTO, 720, 1872 ) );
  }

  /**
   * <p>
   * Creates a {@link FluidGridLayout} with the defined {@link FluidGridConfiguration}.
   * </p>
   *
   * @param configuration the {@link FluidGridConfiguration} to use in the {@link FluidGridLayout}. Must not
   *                      be <code>null</code>.
   *
   * @see FluidGridLayout
   * @see FluidGridConfiguration
   */
  public static FluidGridLayout createFluidGrid( FluidGridConfiguration configuration ) {
    return new FluidGridLayout( configuration );
  }

  /**
   * <p>
   * Create a {@link FluidGridData} with the defined {@link Rule}s.
   * <p>
   *
   * @param rules the {@link Rule}s to use in the {@link FluidGridData}.
   *
   * @see Rule
   * @see FluidGridLayout
   */
  public static FluidGridData createFluidGridData( Rule... rules  ) {
    FluidGridData gridData = new FluidGridData();
    for( Rule rule : rules ) {
      gridData.addRule( rule );
    }
    return gridData;
  }

  /**
   * <p>
   * Creates a {@link Resource} with the defined {@link Rule}s.
   * </p>
   *
   * @param rules the {@link Rule}s to use in the {@link Resource}
   *
   * @see Resource
   * @see Rule
   */
  public static Resource createResource( Rule... rules ) {
    return new ResourceImpl( rules );
  }

  /**
   * <p>
   * Creates a {@link Query} with the defined {@link Condition}. Out of a {@link Query} you can create a {@link Rule}
   * that is used together a {@link FluidGridData}, a {@link Resource} and so on.
   * </p>
   *
   * @param condition the condition to use in the {@link Query}. Must not be <code>null</code>.
   *
   * @see Query
   * @see Condition
   */
  public static Query when( Condition condition ) {
    return new QueryImpl( condition );
  }

  /**
   * <p>
   * Creates a {@link Condition} that's valid when the minimum width of a parent is equal or bigger than the defined
   * value.
   * </p>
   *
   * @param minWidth the minimum width for the {@link Condition} to become valid. Must not be <code>null</code>.
   */
  public static Condition minWidth( Unit minWidth ) {
    return new MinWidthCondition( minWidth );
  }

  /**
   * <p>
   * Creates a {@link Condition} that's valid when the maximum width of a parent is equal or smaller than the defined
   * value.
   * </p>
   *
   * @param maxWidth the maximum width for the {@link Condition} to become valid. Must not be <code>null</code>.
   */
  public static Condition maxWidth( Unit maxWidth ) {
    return new MaxWidthCondition( maxWidth );
  }

  /**
   * <p>
   * Creates a {@link Unit} that defines an <i><a href="http://en.wikipedia.org/wiki/Em_(typography)#CSS">em</a></i>.
   * An <i>em</i> is a unit of width in the field of typography, equal to the currently specified point size. In
   * CSS an <i>em</i> is heavily used to create responsive designs.
   * </p>
   *
   * @param value the value of the <i>em</i>. Needs to be > 0.
   */
  public static Unit em( double value ) {
    return new Em( BigDecimal.valueOf( value ) );
  }

  /**
   * <p>
   * Creates a {@link Unit} that defines a percentage. The percentage defined is always relative to a component's
   * parent element.
   * </p>
   *
   * @param value the percentage value to use. Must be >= 0.
   */
  public static Unit percent( double value ) {
    return new Percentage( BigDecimal.valueOf( value ) );
  }

  /**
   * <p>
   * Creates a {@link Unit} that defines an absolute pixel value.
   * </p>
   *
   * @param value defines the pixel value. Must be >= 0.
   */
  public static Unit px( int value ) {
    return new Pixel( value );
  }

  /**
   * <p>
   * Creates an {@link Instruction} that defines how many columns a component should use in a {@link FluidGridLayout}.
   * </p>
   *
   * @param columns the columns to use. Must be >= 0.
   */
  public static Instruction columns( int columns ) {
    return new ColumnsInstruction( columns );
  }

  /**
   * <p>
   * Create an {@link Instruction} to define the height of a component in a {@link FluidGridLayout}.
   * </p>
   *
   * @param height the height to use. Must not be <code>null</code>.
   */
  public static Instruction height( Unit height ) {
    return new HeightInstruction( height );
  }

  /**
   * <p>
   * Creates an {@link Instruction} to define the margins of a component in a {@link FluidGridLayout}.
   * </p>
   *
   * @param top the top margin. Must not be <code>null</code>.
   * @param right the right margin. Must not be <code>null</code>.
   * @param bottom the bottom margin. Must not be <code>null</code>.
   * @param left the left margin. Must not be <code>null</code>.
   */
  public static Instruction margins( Unit top, Unit right, Unit bottom, Unit left ) {
    return new MarginInstruction( top, right, bottom, left );
  }

  /**
   * <p>
   * Creates an {@link Instruction} that will exclude a component in a {@link FluidGridLayout} from layouting.
   * </p>
   */
  public static Instruction exclude() {
    return new ExcludeInstruction();
  }

  /**
   * <p>
   * Creates an {@link Instruction} to set a font used in a {@link Resource}.
   * </p>
   *
   * @param font the font to set. Must not be <code>null</code>.
   */
  public static Instruction font( Font font ) {
    return new FontInstruction( font );
  }

  /**
   * <p>
   * Creates an {@link Instruction} to set a foreground color used in a {@link Resource}.
   * </p>
   *
   * @param color the foreground to set. Must not be <code>null</code>.
   */
  public static Instruction foreground( Color color ) {
    return new ForegroundInstruction( color );
  }

  /**
   * <p>
   * Creates an {@link Instruction} to set a background color used in a {@link Resource}.
   * </p>
   *
   * @param color the background to set. Must not be <code>null</code>.
   */
  public static Instruction background( Color color ) {
    return new BackgroundInstruction( color );
  }

  /**
   * <p>
   * Creates an {@link Instruction} to set an image used in a {@link Resource}.
   * </p>
   *
   * @param image the image to set. Must not be <code>null</code>.
   */
  public static Instruction image( Image image ) {
    return new ImageInstruction( image );
  }

  /**
   * <p>
   * Creates an {@link Instruction} to set a background image used in a {@link Resource}.
   * </p>
   *
   * @param image the background image to set. Must not be <code>null</code>.
   */
  public static Instruction backgroundImage( Image image ) {
    return new BackgroundImageInstruction( image );
  }

  private PassePartout() {
    // prevent instantiation
  }

}

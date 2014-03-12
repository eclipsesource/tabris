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

import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNot;
import static com.eclipsesource.tabris.passepartout.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.passepartout.internal.UIEnvironmentFactory.createEnvironment;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;

import com.eclipsesource.tabris.passepartout.internal.BoundsUtil;
import com.eclipsesource.tabris.passepartout.internal.InstructionExtractor;
import com.eclipsesource.tabris.passepartout.internal.Layouter;
import com.eclipsesource.tabris.passepartout.internal.QueryNotifier;
import com.eclipsesource.tabris.passepartout.internal.RelayoutListener;


/**
 * <p>
 * With the {@link FluidGridLayout} you divide your application into a grid that responds to size changes.
 * The idea how this grid works is borrowed from the famous
 * <a href="http://goldengridsystem.com/">Golden Grid System (GGS)</a> created by Joni Korpi. Basically the GGS splits
 * your visible area into several columns that adjust their widths while the screen size changes. What makes the GGS
 * special is that it defines a concept called column folding. This concept comes from the print world. The idea is
 * that on very large screens you will have 16 columns to use. On smaller screens this number changes to 8 and on tiny
 * screens like phone displays you only have 4 columns left. The image below shows what folding means.
 *
 * <img src="http://developer.eclipsesource.com/tabris/docs/1.3/working/img/ggs.gif" />
 * </p>
 * <p>
 * The {@link FluidGridLayout} works exactly like the GGS. To compute how many columns should be used the
 * {@link FluidGridLayout} measures the width of your parent composite.
 * </p>
 * <p>
 * In a {@link Composite} using {@link FluidGridLayout}, the children need to have {@link FluidGridData} set as
 * layout data.
 * </p>
 *
 * @see FluidGridData
 * @see PassePartout
 *
 * @since 0.9
 */
public class FluidGridLayout extends Layout {

  private final QueryNotifier notifier;
  private final FluidGridConfiguration configuration;

  /**
   * <p>
   * Creates a {@link FluidGridLayout} with the defined {@link FluidGridConfiguration}.
   * </p>
   *
   * @param configuration the configuration of the layout. Must not be <code>null</code>.
   */
  public FluidGridLayout( FluidGridConfiguration configuration ) {
    whenNull( configuration ).throwIllegalArgument( "configuration must not be null" );
    this.configuration = configuration;
    this.notifier = new QueryNotifier();
  }

  /**
   * <p>
   * Adds a {@link QueryListener} that will be notified when a {@link Query} was activated or deactivated.
   * </p>
   *
   * @param query the query to use. Must not be <code>null</code>.
   * @param listener the listener to notify when the query was de/activated. Must not be <code>null</code>.
   */
  public void addQueryListener( Query query, QueryListener listener ) {
    notifier.addQueryListener( query, listener );
  }

  /**
   * <p>
   * Removes the listener for the specified {@link Query}.
   * </p>
   *
   * @param query the query to remove the listener for. Must not be <code>null</code>.
   */
  public void removeQueryListener( Query query ) {
    notifier.removeQueryListener( query );
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    attachRelayoutListener( composite );
    UIEnvironment environment = createEnvironment( composite );
    notifier.notifyListeners( environment );
    Layouter layouter = new Layouter( environment, configuration );
    InstructionExtractor extractor = new InstructionExtractor( environment );
    layoutChildren( composite, extractor, layouter, flushCache );
  }

  private void attachRelayoutListener( Composite composite ) {
    if( configuration.getMode() == LayoutMode.AUTO ) {
      boolean hasListener = hasRelayoutListener( composite );
      if( !hasListener ) {
        RelayoutListener listener = new RelayoutListener( composite );
        composite.addListener( SWT.Resize, listener );
      }
    }
  }

  private boolean hasRelayoutListener( Composite composite ) {
    Listener[] listeners = composite.getListeners( SWT.Resize );
    for( Listener listener : listeners ) {
      if( listener instanceof RelayoutListener ) {
        return true;
      }
    }
    return false;
  }

  private void layoutChildren( Composite composite,
                               InstructionExtractor extractor,
                               Layouter layouter,
                               boolean flushCache )
  {
    Control[] children = composite.getChildren();
    for( Control child : children ) {
      layoutChild( child, extractor, layouter, flushCache );
    }
  }

  private void layoutChild( Control child, InstructionExtractor extractor, Layouter layouter, boolean flushCache ) {
    Object layoutData = getLayoutData( child );
    validateLayoutData( layoutData );
    Bounds preferedBounds = BoundsUtil.getBounds( child.computeSize( SWT.DEFAULT, SWT.DEFAULT, flushCache ) );
    Bounds childBounds = layouter.computeBounds( preferedBounds,
                                                 extractor.extract( ( ( FluidGridData )layoutData ).getRules() ) );
    child.setBounds( BoundsUtil.getRectangle( childBounds ) );
  }

  private void validateLayoutData( Object layoutData ) {
    String errorMessage = "LayoutData must be of type FluidGridData but was of type " + layoutData.getClass().getName();
    whenNot( layoutData instanceof FluidGridData ).throwIllegalState( errorMessage );
  }

  private Object getLayoutData( Control control ) {
    Object layoutData = control.getLayoutData();
    if( layoutData == null ) {
      layoutData = new FluidGridData();
    }
    return layoutData;
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    int height = computeHeight( composite, flushCache );
    Rectangle parentBounds = BoundsUtil.getRectangle( createEnvironment( composite ).getParentBounds() );
    height = ensureValidHeight( height, parentBounds );
    int width = wHint != SWT.DEFAULT ? wHint : parentBounds.width;
    height = hHint != SWT.DEFAULT ? hHint : height;
    return new Point( width, height );
  }

  private int ensureValidHeight( int height, Rectangle parentBounds ) {
    if( height == 0 ) {
      return parentBounds.height;
    }
    return height;
  }

  private int computeHeight( Composite composite, boolean flushCache ) {
    layout( composite, flushCache );
    int lastY = 0;
    int lastHeight = 0;
    int height = 0;
    for( Control child : composite.getChildren() ) {
      Rectangle bounds = child.getBounds();
      if( bounds.y > lastY ) {
        height += bounds.height;
        lastHeight = bounds.height;
        lastY = bounds.y;
      } else {
        if( bounds.height > lastHeight ) {
          height -= lastHeight;
          lastHeight = bounds.height;
          height += bounds.height;
        }
      }

    }
    return height;
  }

}

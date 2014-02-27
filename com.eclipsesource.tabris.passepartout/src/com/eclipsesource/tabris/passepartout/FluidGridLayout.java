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


public class FluidGridLayout extends Layout {

  private final QueryNotifier notifier;
  private final FluidGridConfiguration configuration;

  public FluidGridLayout( FluidGridConfiguration configuration ) {
    whenNull( configuration ).throwIllegalArgument( "configuration must not be null" );
    this.configuration = configuration;
    this.notifier = new QueryNotifier();
  }

  public void addQueryListener( Query query, QueryListener listener ) {
    notifier.addQueryListener( query, listener );
  }

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
    Rectangle parentBounds = BoundsUtil.getRectangle( createEnvironment( composite ).getParentBounds() );
    int width = wHint != SWT.DEFAULT ? wHint : parentBounds.width;
    int height = hHint != SWT.DEFAULT ? hHint : parentBounds.height;
    return new Point( width, height );
  }

}

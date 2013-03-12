/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.widgets.enhancement;

import static com.eclipsesource.tabris.internal.Preconditions.checkArgumentNotNull;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;


/**
 * @since 0.10
 */
public class CompositeDecorator extends WidgetDecorator<CompositeDecorator>  {

  /**
   * <p>
   * When using a GroupedEvent this key can be used as a {@link RWT#CUSTOM_VARIANT} to theme the overlayed composite.
   * </p>
   *
   * @since 1.0
   */
  public static final String GROUPED_EVENT_COMPOSITE = "groupedEventComposite";

  private final Composite composite;

  CompositeDecorator( Composite composite ) {
    super( composite );
    this.composite = composite;
  }

  /**
   * <p>
   * Adds a {@link Listener} for the defined eventType. The listener will be notified when the event occurs anywhere
   * on the {@link Composite}. It doesn't matter if the {@link Composite} holds controls or not.
   * </p>
   *
   * @since 0.10
   */
  public void addGroupedListener( int eventType, Listener listener )
    throws IllegalArgumentException, IllegalStateException
  {
    checkArgumentNotNull( listener, "Listener" );
    Composite facade = getFacade();
    facade.addListener( eventType, listener );
  }

  /**
   * <p>
   * Removes a {@link Listener} for the defined  eventType.
   * </p>
   *
   * @since 0.10
   */
  public void removeGroupedListener( int eventType, Listener listener )
    throws IllegalArgumentException, IllegalStateException
  {
    checkArgumentNotNull( listener, "Listener" );
    Composite facade = findFacade();
    if( facade != null ) {
      facade.removeListener( eventType, listener );
    }
  }

  private Composite getFacade() {
    Composite facade = findFacade();
    if( facade == null ) {
      facade = createFacade();
    }
    return facade;
  }

  private Composite findFacade() {
    String compositeId = WidgetUtil.getId( composite );
    Control[] children = composite.getChildren();
    for( Control child : children ) {
      if( compositeId.equals( child.getData( GROUPED_EVENT_COMPOSITE ) ) ) {
        return ( Composite )child;
      }
    }
    return null;
  }

  private Composite createFacade() {
    Composite facade = new Composite( composite, SWT.NONE );
    facade.setData( GROUPED_EVENT_COMPOSITE, WidgetUtil.getId( composite ) );
    facade.setData( RWT.CUSTOM_VARIANT, GROUPED_EVENT_COMPOSITE );
    addLayoutDataWithExclude( facade );
    layoutFacade( facade );
    addResizeListener( facade );
    return facade;
  }

  private void addLayoutDataWithExclude( Composite facade ) {
    Layout layout = composite.getLayout();
    if( layout instanceof RowLayout ) {
      addExcludedRowData( facade );
    } else if( layout instanceof GridLayout ) {
      addExcludedGridData( facade );
    } else {
      throw new IllegalStateException( "Composite needs to be layouted using Grid- or RowLayout" );
    }
  }

  private void addExcludedRowData( Composite facade ) {
    RowData rowData = new RowData();
    rowData.exclude = true;
    facade.setLayoutData( rowData );
  }

  private void addExcludedGridData( Composite facade ) {
    GridData gridData = new GridData();
    gridData.exclude = true;
    facade.setLayoutData( gridData );
  }

  private void addResizeListener( final Composite facade ) {
    composite.addControlListener( new ControlListener() {
      @Override
      public void controlResized( ControlEvent e ) {
        layoutFacade( facade );
      }

      @Override
      public void controlMoved( ControlEvent e ) {
        layoutFacade( facade );
      }
    } );
  }

  private void layoutFacade( final Composite facade ) {
    Rectangle bounds = composite.getBounds();
    facade.setBounds( 0, 0, bounds.width, bounds.height );
    facade.moveAbove( null );
  }

}

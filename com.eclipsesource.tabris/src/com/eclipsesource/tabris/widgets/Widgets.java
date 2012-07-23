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
package com.eclipsesource.tabris.widgets;

import static com.eclipsesource.tabris.internal.WidgetsUtil.checkComponent;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;


/**
 * @since 0.6
 */
public class Widgets {
  
  public static WidgetDecorator<WidgetDecorator> onWidget( Widget widget ) {
    checkComponent( widget );
    return new WidgetDecorator<WidgetDecorator>( widget );
  }
  
  public static TextDecorator onText( Text text ) {
    checkComponent( text );
    return new TextDecorator( text );
  }
  
  public static ToolItemDecorator onToolItem( ToolItem toolItem ) {
    checkComponent( toolItem );
    return new ToolItemDecorator( toolItem );
  }
  
  public static TreeDecorator onTree( Tree tree ) {
    checkComponent( tree );
    return new TreeDecorator( tree );
  }
  
  public static ScrolledCompositeDecorator onScrolledComposite( ScrolledComposite composite ) {
    checkComponent( composite );
    return new ScrolledCompositeDecorator( composite );
  }
  
  public static LabelDecorator onLabel( Label label ) {
    checkComponent( label );
    return new LabelDecorator( label );
  }

  private Widgets() {
    // prevent instantiation
  }
  
}

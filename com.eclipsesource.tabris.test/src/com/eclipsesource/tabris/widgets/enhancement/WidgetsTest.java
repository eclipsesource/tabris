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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.junit.Test;


public class WidgetsTest {
  
  @Test
  public void testOnWidget() {
    assertNotNull( Widgets.onWidget( mock( Widget.class ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testOnWidgetWithNull() {
    Widgets.onWidget( null );
  }
  
  @Test
  public void tesOnWidgetDoesNotCache() {
    Widget widget = mock( Widget.class );
    
    WidgetDecorator<WidgetDecorator> decorator1 = Widgets.onWidget( widget );
    WidgetDecorator<WidgetDecorator> decorator2 = Widgets.onWidget( widget );
    
    assertNotSame( decorator1, decorator2 );
  }
  
  @Test
  public void testOnText() {
    assertNotNull( Widgets.onText( mock( Text.class ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testOnTextWithNull() {
    Widgets.onText( null );
  }
  
  @Test
  public void tesOnTextDoesNotCache() {
    Text widget = mock( Text.class );
    
    WidgetDecorator<TextDecorator> decorator1 = Widgets.onText( widget );
    WidgetDecorator<TextDecorator> decorator2 = Widgets.onText( widget );
    
    assertNotSame( decorator1, decorator2 );
  }
  
  @Test
  public void testOnTree() {
    assertNotNull( Widgets.onTree( mock( Tree.class ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testOnTreeWithNull() {
    Widgets.onTree( null );
  }
  
  @Test
  public void tesOnTreeDoesNotCache() {
    Tree widget = mock( Tree.class );
    
    WidgetDecorator<TreeDecorator> decorator1 = Widgets.onTree( widget );
    WidgetDecorator<TreeDecorator> decorator2 = Widgets.onTree( widget );
    
    assertNotSame( decorator1, decorator2 );
  }
  
  @Test
  public void testOnLabel() {
    assertNotNull( Widgets.onLabel( mock( Label.class ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testOnLabelWithNull() {
    Widgets.onLabel( null );
  }
  
  @Test
  public void tesOnLabelDoesNotCache() {
    Label widget = mock( Label.class );
    
    WidgetDecorator<LabelDecorator> decorator1 = Widgets.onLabel( widget );
    WidgetDecorator<LabelDecorator> decorator2 = Widgets.onLabel( widget );
    
    assertNotSame( decorator1, decorator2 );
  }
  
  @Test
  public void testOnScrolledComposite() {
    assertNotNull( Widgets.onScrolledComposite( mock( ScrolledComposite.class ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testOnScrolledCompositeWithNull() {
    Widgets.onScrolledComposite( null );
  }
  
  @Test
  public void tesOnScrolledCompositeDoesNotCache() {
    ScrolledComposite widget = mock( ScrolledComposite.class );
    
    WidgetDecorator<ScrolledCompositeDecorator> decorator1 = Widgets.onScrolledComposite( widget );
    WidgetDecorator<ScrolledCompositeDecorator> decorator2 = Widgets.onScrolledComposite( widget );
    
    assertNotSame( decorator1, decorator2 );
  }
  
  @Test
  public void testOnToolItem() {
    assertNotNull( Widgets.onToolItem( mock( ToolItem.class ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testOnToolItemWithNull() {
    Widgets.onToolItem( null );
  }
  
  @Test
  public void testOnToolItemDoesNotCache() {
    ToolItem widget = mock( ToolItem.class );
    
    ToolItemDecorator decorator1 = Widgets.onToolItem( widget );
    ToolItemDecorator decorator2 = Widgets.onToolItem( widget );
    
    assertNotSame( decorator1, decorator2 );
  }
 
  @Test
  public void testOnList() {
    assertNotNull( Widgets.onList( mock( List.class ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testOnListWithNull() {
    Widgets.onList( null );
  }
  
  @Test
  public void testOnListDoesNotCache() {
    List widget = mock( List.class );
    
    ListDecorator decorator1 = Widgets.onList( widget );
    ListDecorator decorator2 = Widgets.onList( widget );
    
    assertNotSame( decorator1, decorator2 );
  }
  
  @Test
  public void testOnComposite() {
    assertNotNull( Widgets.onComposite( mock( Composite.class ) ) );
  }
  
  @Test( expected = IllegalArgumentException.class )
  public void testOnCompositeWithNull() {
    Widgets.onComposite( null );
  }
  
  @Test
  public void testOnCompositeDoesNotCache() {
    Composite widget = mock( Composite.class );
    
    CompositeDecorator decorator1 = Widgets.onComposite( widget );
    CompositeDecorator decorator2 = Widgets.onComposite( widget );
    
    assertNotSame( decorator1, decorator2 );
  }
 
}

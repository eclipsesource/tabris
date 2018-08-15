/*******************************************************************************
 * Copyright (c) 2012, 2018 EclipseSource and others.
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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
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
  public void testOnWidgetDoesNotCache() {
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
  public void testOnTextDoesNotCache() {
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
  public void testOnTreeDoesNotCache() {
    Tree widget = mock( Tree.class );

    WidgetDecorator<TreeDecorator> decorator1 = Widgets.onTree( widget );
    WidgetDecorator<TreeDecorator> decorator2 = Widgets.onTree( widget );

    assertNotSame( decorator1, decorator2 );
  }

  @Test
  public void testTabFolderComposite() {
    assertNotNull( Widgets.onTabFolder( mock( TabFolder.class ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testOnTabFolderCompositeWithNull() {
    Widgets.onTabFolder( null );
  }

  @Test
  public void testTabFolderCompositeDoesNotCache() {
    TabFolder widget = mock( TabFolder.class );

    WidgetDecorator<TabFolderDecorator> decorator1 = Widgets.onTabFolder( widget );
    WidgetDecorator<TabFolderDecorator> decorator2 = Widgets.onTabFolder( widget );

    assertNotSame( decorator1, decorator2 );
  }

  @Test
  public void testOnTabItem() {
    assertNotNull( Widgets.onTabItem( mock( TabItem.class ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testOnTabItemWithNull() {
    Widgets.onTabItem( null );
  }

  @Test
  public void testOnTabItemDoesNotCache() {
    TabItem widget = mock( TabItem.class );

    TabItemDecorator decorator1 = Widgets.onTabItem( widget );
    TabItemDecorator decorator2 = Widgets.onTabItem( widget );

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

  @Test
  public void testOnShell() {
    assertNotNull( Widgets.onShell( mock( Shell.class ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testOnShellWithNull() {
    Widgets.onShell( null );
  }

  @Test
  public void testOnShellDoesNotCache() {
    Shell widget = mock( Shell.class );

    ShellDecorator decorator1 = Widgets.onShell( widget );
    ShellDecorator decorator2 = Widgets.onShell( widget );

    assertNotSame( decorator1, decorator2 );
  }

  @Test
  public void testOnProgressBar() {
    assertNotNull( Widgets.onProgressBar( mock( ProgressBar.class ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testOnPrgoressBarWithNull() {
    Widgets.onProgressBar( null );
  }

  @Test
  public void testOnProgressBarDoesNotCache() {
    ProgressBar widget = mock( ProgressBar.class );

    ProgressBarDecorator decorator1 = Widgets.onProgressBar( widget );
    ProgressBarDecorator decorator2 = Widgets.onProgressBar( widget );

    assertNotSame( decorator1, decorator2 );
  }

  @Test
  public void testOnMenuItem() {
    assertNotNull( Widgets.onMenuItem( mock( MenuItem.class ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testOnMenuItemWithNull() {
    Widgets.onMenuItem( null );
  }

  @Test
  public void testOnMenuItemDoesNotCache() {
    MenuItem widget = mock( MenuItem.class );

    MenuItemDecorator decorator1 = Widgets.onMenuItem( widget );
    MenuItemDecorator decorator2 = Widgets.onMenuItem( widget );

    assertNotSame( decorator1, decorator2 );
  }

}

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
package com.eclipsesource.tabris.ui;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.PageDescriptor;
import com.eclipsesource.tabris.internal.ui.TestAction;
import com.eclipsesource.tabris.internal.ui.TestPage;
import com.eclipsesource.tabris.internal.ui.UITestUtil;
import com.eclipsesource.tabris.internal.ui.UIUpdater;
import com.eclipsesource.tabris.internal.ui.UpdateUtil;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


public class PageConfigurationTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PageConfiguration.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullId() {
    new PageConfiguration( null, TestPage.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() {
    new PageConfiguration( "", TestPage.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullType() {
    new PageConfiguration( "foo", null );
  }

  @Test
  public void testCanCreateDescriptor() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setTopLevel( true );

    PageDescriptor descriptor = config.getAdapter( PageDescriptor.class );

    assertNotNull( descriptor );
  }

  @Test
  public void testHasId() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class );

    String pageId = config.getId();

    assertEquals( pageId, "foo" );
  }

  @Test
  public void testSetsDefaultValues() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class );

    PageDescriptor descriptor = config.getAdapter( PageDescriptor.class );

    assertEquals( "foo", descriptor.getId() );
    assertSame( TestPage.class, descriptor.getPageType() );
    assertFalse( descriptor.isTopLevel() );
    assertEquals( "", descriptor.getTitle() );
    assertNull( descriptor.getBackCaption() );
    assertNull( descriptor.getImage() );
    assertEquals( 0, descriptor.getPageStyle().length );
  }

  @Test
  public void testSetsTopLevel() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setTopLevel( true );

    PageDescriptor descriptor = config.getAdapter( PageDescriptor.class );

    assertTrue( descriptor.isTopLevel() );
  }

  @Test
  public void testHasTopLevel() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setTopLevel( true );

    boolean topLevel = config.isTopLevel();

    assertTrue( topLevel );
  }

  @Test
  public void testSetsTitle() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setTitle( "bar" );

    PageDescriptor descriptor = config.getAdapter( PageDescriptor.class );

    assertEquals( "bar", descriptor.getTitle() );
  }

  @Test
  public void testHasTitle() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setTitle( "bar" );

    String title = config.getTitle();

    assertEquals( "bar", title );
  }

  @Test
  public void testSetsBackCaption() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setBackCaption( "Leave" );

    PageDescriptor descriptor = config.getAdapter( PageDescriptor.class );

    assertEquals( "Leave", config.getBackCaption() );
    assertEquals( "Leave", descriptor.getBackCaption() );
  }

  @Test
  public void testSetsBackCaptionReturnsPageConfig() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setTitle( "bar" );

    PageConfiguration actualConfig = config.setBackCaption( "backCaption" );

    assertSame( config, actualConfig );
  }

  @Test
  public void testBackCaptionIsNullByDefault() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setTitle( "bar" );

    String backCaption = config.getBackCaption();

    assertNull( backCaption );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetTitleFailsWithNull() {
    new PageConfiguration( "foo", TestPage.class ).setTitle( null );
  }

  @Test
  public void testSetsImage() {
    InputStream image = UITestUtil.class.getResourceAsStream( "testImage.png" );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setImage( image );

    PageDescriptor descriptor = config.getAdapter( PageDescriptor.class );

    assertArrayEquals( UITestUtil.getImageBytes(), descriptor.getImage() );
  }

  @Test
  public void testHasImage() {
    InputStream image = UITestUtil.class.getResourceAsStream( "testImage.png" );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setImage( image );

    byte[] actualImage = config.getImage();

    assertArrayEquals( UITestUtil.getImageBytes(), actualImage );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetImageFailsWithNull() {
    new PageConfiguration( "foo", TestPage.class ).setImage( null );
  }

  @Test
  public void testSetsStyle() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setStyle( PageStyle.DEFAULT, PageStyle.FULLSCREEN );

    PageDescriptor descriptor = config.getAdapter( PageDescriptor.class );

    PageStyle[] pageStyle = descriptor.getPageStyle();
    assertEquals( 2, pageStyle.length );
    assertSame( PageStyle.DEFAULT, pageStyle[ 0 ] );
    assertSame( PageStyle.FULLSCREEN, pageStyle[ 1 ] );
  }

  @Test
  public void testHasStyle() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).setStyle( PageStyle.DEFAULT, PageStyle.FULLSCREEN );

    List<PageStyle> style = config.getStyle();

    assertEquals( 2, style.size() );
    assertSame( PageStyle.DEFAULT, style.get( 0 ) );
    assertSame( PageStyle.FULLSCREEN, style.get( 1 ) );
  }

  @Test
  public void testAddsAction() {
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).addActionConfiguration( actionConfig );

    PageDescriptor descriptor = config.getAdapter( PageDescriptor.class );

    List<ActionDescriptor> actions = descriptor.getActions();
    assertEquals( 1, actions.size() );
    assertEquals( actions.get( 0 ).getId(), "bar" );
  }

  @Test
  public void testAddsActionTriggersPageUpdate() {
    UIUpdater updater = mock( UIUpdater.class );
    UpdateUtil.registerUpdater( updater );
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class );

    config.addActionConfiguration( actionConfig );

    verify( updater ).update( config );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddActionConfigurationFailsWithNullAction() {
    new PageConfiguration( "foo", TestPage.class ).addActionConfiguration( null );
  }

  @Test
  public void testCanGetActionConfiguration() {
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).addActionConfiguration( actionConfig );

    ActionConfiguration actualActionConfiguration = config.getActionConfiguration( "bar" );

    assertSame( actionConfig, actualActionConfiguration );
  }

  @Test
  public void testGetActionConfigurationReturnsNullIfActionIsNonExistent() {
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).addActionConfiguration( actionConfig );

    ActionConfiguration actualActionConfiguration = config.getActionConfiguration( "bar2" );

    assertNull( actualActionConfiguration );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetActionConfigurationFailsWithNullId() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class );

    config.getActionConfiguration( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetActionConfigurationFailsWithEmptyId() {
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class );

    config.getActionConfiguration( "" );
  }

  @Test
  public void testRemovesAction() {
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).addActionConfiguration( actionConfig );

    config.removeActionConfiguration( "bar" );

    PageDescriptor descriptor = config.getAdapter( PageDescriptor.class );
    List<ActionDescriptor> actions = descriptor.getActions();
    assertTrue( actions.isEmpty() );
  }

  @Test
  public void testRemoveActionTriggersPageUpdate() {
    UIUpdater updater = mock( UIUpdater.class );
    UpdateUtil.registerUpdater( updater );
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class );
    config.addActionConfiguration( actionConfig );

    config.removeActionConfiguration( "bar" );

    verify( updater, times( 2 ) ).update( config );
  }

  @Test
  public void testRemoveActionReturnsPageConfiguration() {
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).addActionConfiguration( actionConfig );

    PageConfiguration pageConfiguration = config.removeActionConfiguration( "bar" );

    assertSame( config, pageConfiguration );
  }

  @Test( expected = IllegalStateException.class )
  public void testRemoveActionFailsWithNonExistingAction() {
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).addActionConfiguration( actionConfig );

    config.removeActionConfiguration( "bar2" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveActionFailsWithNullId() {
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).addActionConfiguration( actionConfig );

    config.removeActionConfiguration( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveActionFailsWithEmptyNullId() {
    ActionConfiguration actionConfig = new ActionConfiguration( "bar", TestAction.class );
    PageConfiguration config = new PageConfiguration( "foo", TestPage.class ).addActionConfiguration( actionConfig );

    config.removeActionConfiguration( "" );
  }

}

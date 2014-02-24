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
package com.eclipsesource.tabris.internal.ui;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.ui.ActionConfiguration;
import com.eclipsesource.tabris.ui.PageStyle;


@RunWith( RWTRunner.class )
public class PageDescriptorTest {

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PageDescriptor.class ) );
  }

  @Test
  public void testGetId() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );

    assertEquals( "foo", descriptor.getId() );
  }

  @Test
  public void testGetPage() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );

    assertSame( TestPage.class, descriptor.getPageType() );
  }

  @Test
  public void testGetTitle() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );
    descriptor.setTitle( "bar" );

    assertEquals( "bar", descriptor.getTitle() );
  }

  @Test
  public void testSetTitleReturnsDescritor() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );

    PageDescriptor actualDescriptor = descriptor.setTitle( "bar" );

    assertSame( descriptor, actualDescriptor );
  }

  @Test
  public void testGetImage() {
    InputStream image = PageDescriptorTest.class.getResourceAsStream( "testImage.png" );
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );
    descriptor.setImage( ImageUtil.getBytes( image ) );

    assertArrayEquals( UITestUtil.getImageBytes(), descriptor.getImage() );
  }

  @Test
  public void testGetStyle() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );
    descriptor.setPageStyle( PageStyle.DEFAULT );

    assertSame( PageStyle.DEFAULT, descriptor.getPageStyle()[ 0 ] );
  }

  @Test
  public void testSetStyleReturnsDescriptor() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );

    PageDescriptor actualDescriptor = descriptor.setPageStyle( PageStyle.DEFAULT );

    assertSame( descriptor, actualDescriptor );
  }

  @Test
  public void testIsTopLevel() {
    PageDescriptor descriptor1 = new PageDescriptor( "foo", TestPage.class ).setTopLevel( true );
    PageDescriptor descriptor2 = new PageDescriptor( "foo1", TestPage.class ).setTopLevel( false );

    assertTrue( descriptor1.isTopLevel() );
    assertFalse( descriptor2.isTopLevel() );
  }

  @Test
  public void testSetTopLevelReturnsDescriptor() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );

    PageDescriptor actualDescriptor = descriptor.setTopLevel( true );

    assertSame( descriptor, actualDescriptor );
  }

  @Test
  public void testAddAction() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );

    descriptor.addAction( new ActionConfiguration( "foo", TestAction.class ) );

    List<ActionDescriptor> actions = descriptor.getActions();
    assertEquals( 1, actions.size() );
    ActionDescriptor actionDescriptor = actions.get( 0 );
    assertEquals( "foo", actionDescriptor.getId() );
  }

  @Test
  public void testAddActionReturnsDescriptor() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );

    PageDescriptor actualDescriptor = descriptor.addAction( new ActionConfiguration( "foo", TestAction.class ) );

    assertSame( descriptor, actualDescriptor );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddActionFailsWithNullConfiguration() {
    PageDescriptor descriptor = new PageDescriptor( "foo", TestPage.class );

    descriptor.addAction( null );
  }

}

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
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Collection;

import org.eclipse.rap.rwt.internal.lifecycle.WidgetDataUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry;


@SuppressWarnings("restriction")
public class DataWhitelistTest {

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( DataWhitelist.class ) );
  }

  @Test
  public void testRegistersKeys() {
    DataWhitelist.register();
    Collection<String> registeredKeys = WidgetDataUtil.getDataKeys();

    WhiteListEntry[] values = DataWhitelist.WhiteListEntry.values();
    for( WhiteListEntry whiteListEntry : values ) {
      assertTrue( registeredKeys.contains( whiteListEntry.getKey() ) );
    }
  }

  @Test
  public void testHasKeyAnimation() {
    assertContains( "animated" );
  }

  @Test
  public void testHasKeyShowTouch() {
    assertContains( "showTouch" );
  }

  @Test
  public void testHasKeyTitle() {
    assertContains( "title" );
  }

  @Test
  public void testHasKeyKeyboard() {
    assertContains( "keyboard" );
  }

  @Test
  public void testHasKeySwipe() {
    assertContains( "swipe" );
  }

  @Test
  public void testHasKeyClientCanvas() {
    assertContains( "clientCanvas" );
  }

  @Test
  public void testHasKeyAltSelection() {
    assertContains( "altSelection" );
  }

  @Test
  public void testHasKeyZoom() {
    assertContains( "zoom" );
  }

  @Test
  public void testHasKeyBackFocus() {
    assertContains( "backFocus" );
  }

  @Test
  public void testHasPaging() {
    assertContains( "paging" );
  }

  @Test
  public void testHasOverlayColor() {
    assertContains( "overlayColor" );
  }

  @Test
  public void testHasTextReplacement() {
    assertContains( "textReplacement" );
  }
  
  private void assertContains( String actualKey ) {
    WhiteListEntry[] keys = DataWhitelist.WhiteListEntry.values();
    boolean foundKey = false;
    for( WhiteListEntry key : keys ) {
      if( key.getKey().equals( actualKey ) ) {
        foundKey = true;
      }
    }
    assertTrue( foundKey );
  }
}

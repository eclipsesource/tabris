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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry;


public class DataWhitelistTest {

  private DataWhitelist whitelist;

  @Before
  public void setUp() {
    whitelist = new DataWhitelist();
  }

  @Test
  public void testHasKeys() {
    String[] keys = whitelist.getKeys();

    assertNotNull( keys );
  }

  @Test
  public void testContainsAllWhiteListentries() {
    int whiteListSize = WhiteListEntry.values().length;

    String[] keys = whitelist.getKeys();

    assertEquals( whiteListSize, keys.length );
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

  private void assertContains( String actualKey ) {
    String[] keys = whitelist.getKeys();
    boolean foundKey = false;
    for( String key : keys ) {
      if( key.equals( actualKey ) ) {
        foundKey = true;
      }
    }
    assertTrue( foundKey );
  }
}

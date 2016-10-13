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
import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry;
import com.eclipsesource.tabris.test.util.TabrisEnvironment;


@SuppressWarnings("restriction")
public class DataWhitelistTest {

  @Rule
  public TabrisEnvironment environment = new TabrisEnvironment();

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
  public void testHasKeyTitle() {
    assertContains( "title" );
  }

  @Test
  public void testHasKeyKeyboard() {
    assertContains( "keyboard" );
  }

  @Test
  public void testHasPaging() {
    assertContains( "paging" );
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
  public void testHasKeyClientCanvas() {
    assertContains( "clientCanvas" );
  }

  @Test
  public void testHasKeySwipe() {
    assertContains( "swipe" );
  }

  @Test
  public void testHasSpinningIndicator() {
    assertContains( "spinningIndicator" );
  }

  @Test
  public void testHasKeyShowTouch() {
    assertContains( "showTouch" );
  }

  @Test
  public void testHasBadgeValue() {
    assertContains( "badgeValue" );
  }

  @Test
  public void testHasOverlayColor() {
    assertContains( "overlayColor" );
  }

  @Test
  public void testHasTextReplacement() {
    assertContains( "textReplacement" );
  }

  @Test
  public void testHasAutoCapitalize() {
    assertContains( "autoCapitalize" );
  }

  @Test
  public void testHasAutoCorrect() {
    assertContains( "autoCorrect" );
  }

  @Test
  public void testHasRefreshHandler() {
    assertContains( "refreshHandler" );
  }

  @Test
  public void testHasLocalClipboard() {
    assertContains( "localClipboard" );
  }

  @Test
  public void testHasDisableLookupAction() {
    assertContains( "disableLookupAction" );
  }

  @Test
  public void testHasDisableShareAction() {
    assertContains( "disableShareAction" );
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

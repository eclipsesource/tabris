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
package com.eclipsesource.tabris.internal.textreplacement;

import static com.eclipsesource.tabris.internal.DataWhitelist.WhiteListEntry.TEXT_REPLACEMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.widgets.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.eclipsesource.tabris.internal.TabrisClient;
import com.eclipsesource.tabris.test.TabrisTestUtil;

@RunWith( MockitoJUnitRunner.class )
public class TextReplacementDataTest {

  @Mock
  private Text text;

  @Before
  public void setUp() {
    Fixture.setUp();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testEnableTextReplacement() {
    TextReplacementData data = mock( TextReplacementData.class );
    when( data.getAdapter( String.class ) ).thenReturn( "r42" );

    TextReplacementData.enableTextReplacement( text, data );

    verify( text ).setData( TEXT_REPLACEMENT.getKey(), "r42" );
  }

  @Test
  public void testReplacementKeyInDataWhitelist() {
    assertEquals( "textReplacement", TEXT_REPLACEMENT.getKey() );
  }

  @Test
  public void testDontEnableTextReplacementOnBrowser() {
    TextReplacementData data = mock( TextReplacementData.class );
    when( data.getAdapter( String.class ) ).thenReturn( null );

    TextReplacementData.enableTextReplacement( text, data );

    verify( text, never() ).setData( eq( TEXT_REPLACEMENT.getKey() ), any( String.class ) );
  }

  @Test
  public void testPutsTextReplacement() {
    TextReplacementData data = new TextReplacementData();

    data.put( "shortcut", "replacement" );

    assertEquals( "replacement", data.get( "shortcut" ) );
  }

  @Test
  public void testPutCreatesSetOperation() {
    Fixture.fakeClient( new TabrisClient() );
    RemoteObject remoteObject = TabrisTestUtil.mockRemoteObject();
    TextReplacementData data = new TextReplacementData();

    data.put( "shortcut", "replacement" );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject, times( 1 ) ).set( eq( "texts" ), captor.capture() );
    List<JsonObject> values = captor.getAllValues();
    assertEquals( JsonObject.valueOf("replacement"), values.get( 0 ).get( "shortcut" ) );
  }

  @Test
  public void testUpdateTextReplacementWithSameShortcut() {
    TextReplacementData data = new TextReplacementData();
    data.put( "shortcut", "replacement" );

    data.put( "shortcut", "replacement2" );

    assertEquals( "replacement2", data.get( "shortcut" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPutNullShortcutAndFail() {
    TextReplacementData data = new TextReplacementData();

    data.put( null, "replacement" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPutEmptyShortcutAndFail() {
    TextReplacementData data = new TextReplacementData();

    data.put( "", "replacement" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPutNullReplacementAndFail() {
    TextReplacementData data = new TextReplacementData();

    data.put( "shortcut", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPutEmptyReplacementAndFail() {
    TextReplacementData data = new TextReplacementData();

    data.put( "shortcut", "" );
  }

  @Test
  public void testGetReturnsNullIfShortcutDoesNotExist() {
    TextReplacementData data = new TextReplacementData();

    String replacement = data.get( "doesNotExist" );

    assertNull( replacement );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetFailsWithEmptyShortcut() {
    TextReplacementData data = new TextReplacementData();

    data.get( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetFailsWithNullShortcut() {
    TextReplacementData data = new TextReplacementData();

    data.get( null );
  }

  @Test
  public void testRemoveTextReplacement() {
    TextReplacementData data = new TextReplacementData();
    data.put( "shortcut", "replacement" );

    data.remove( "shortcut" );

    assertNull( data.get( "shortcut" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveFailsWithEmptyShortcut() {
    TextReplacementData data = new TextReplacementData();

    data.remove( "" );
  }

  @Test
  public void testRemoveCreatesSetOperations() {
    Fixture.fakeClient( new TabrisClient() );
    RemoteObject remoteObject = TabrisTestUtil.mockRemoteObject();
    TextReplacementData data = new TextReplacementData();
    data.put( "shortcut", "replacement" );

    data.remove( "shortcut" );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( remoteObject, times( 2 ) ).set( eq( "texts" ), captor.capture() );
    List<JsonObject> values = captor.getAllValues();
    assertTrue( values.get( 1 ).isEmpty() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveFailsWithEmptyNull() {
    TextReplacementData data = new TextReplacementData();

    data.remove( null );
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( TextReplacementData.class ) );
  }
}
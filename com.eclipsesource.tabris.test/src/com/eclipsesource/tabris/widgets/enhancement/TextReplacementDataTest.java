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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.TabrisClient;
import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.test.TabrisTestUtil;


@RunWith( RWTRunner.class )
public class TextReplacementDataTest {

  private RemoteObject remoteObject;

  @Before
  public void setUp() {
    Fixture.fakeClient( mock( TabrisClient.class ) );
    remoteObject = TabrisTestUtil.mockRemoteObject();
  }

  @Test
  public void testCreatesRemoteObject() {
    TextReplacementData data = new TextReplacementData();

    data.put( "shortcut", "replacement" );

    Connection connection = RWT.getUISession().getConnection();
    verify( connection ).createRemoteObject( "tabris.TextReplacement" );
  }

  @Test
  public void testCreatesNoRemoteObjectWithWebClient() {
    Fixture.fakeClient( mock( WebClient.class ) );
    Fixture.fakePhase( PhaseId.RENDER );
    TextReplacementData data = new TextReplacementData();

    data.put( "shortcut", "replacement" );

    Connection connection = RWT.getUISession().getConnection();
    verify( connection, never() ).createRemoteObject( "tabris.TextReplacement" );
  }

  @Test
  public void testHasRemoteObjectId() {
    TextReplacementData data = new TextReplacementData();

    String id = data.getId();

    assertEquals( id, remoteObject.getId() );
  }

  @Test
  public void testHasNullIdWithWebClient() {
    Fixture.fakeClient( mock( WebClient.class ) );
    TextReplacementData data = new TextReplacementData();

    String id = data.getId();

    assertNull( id );
  }

  @Test
  public void testPutsTextReplacement() {
    TextReplacementData data = new TextReplacementData();

    data.put( "shortcut", "replacement" );

    assertEquals( "replacement", data.get( "shortcut" ) );
  }

  @Test
  public void testPutCreatesSetOperation() {
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
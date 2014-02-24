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
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.test.RWTRunner;
import com.eclipsesource.tabris.test.TabrisTestUtil;


@SuppressWarnings("restriction")
@RunWith( RWTRunner.class )
public class ClientStoreImplTest {

  private RemoteObject serviceObject;

  @Before
  public void setUp() {
    serviceObject = TabrisTestUtil.mockServiceObject();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddFailsWithNullKey() {
    ClientStoreImpl store = new ClientStoreImpl();

    store.add( null, "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddFailsWithEmptyKey() {
    ClientStoreImpl store = new ClientStoreImpl();

    store.add( "", "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddFailsWithNullValue() {
    ClientStoreImpl store = new ClientStoreImpl();

    store.add( "foo", null );
  }

  @Test
  public void testAddPutsValue() {
    ClientStoreImpl store = new ClientStoreImpl();

    store.add( "foo", "bar" );

    assertEquals( "bar", store.get( "foo" ) );
  }

  @Test
  public void testRemovesValue() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );

    store.remove( "foo" );

    assertNull( store.get( "foo" ) );
  }

  @Test
  public void testBulkRemovesValue() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );

    store.remove( "foo" ,"foo1" );

    assertNull( store.get( "foo" ) );
    assertNull( store.get( "foo1" ) );
  }

  @Test
  public void testRemoveSendsAllRemovedKeys() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );

    store.remove( "foo" );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( serviceObject ).call( eq( "remove" ), captor.capture() );
    JsonArray removedKeys = captor.getValue().get( "keys" ).asArray();
    assertEquals( JsonValue.valueOf( "foo" ), removedKeys.get( 0 ) );
    assertEquals( 1, removedKeys.size() );
  }

  @Test
  public void testEmptyRemoveDoesntSendsRemovedKeys() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );

    store.remove();

    verify( serviceObject, never() ).call( eq( "remove" ), any( JsonObject.class ) );
  }

  @Test
  public void testBulkRemovesSendsAllRemovedKeys() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );

    store.remove( "foo" ,"foo1" );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( serviceObject ).call( eq( "remove" ), captor.capture() );
    JsonArray removedKeys = captor.getValue().get( "keys" ).asArray();
    assertEquals( JsonValue.valueOf( "foo" ), removedKeys.get( 0 ) );
    assertEquals( JsonValue.valueOf( "foo1" ), removedKeys.get( 1 ) );
    assertEquals( 2, removedKeys.size() );
  }

  @Test
  public void testBulkRemovesNotAllValues() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );
    store.add( "foo3", "bar" );

    store.remove( "foo" ,"foo1" );

    assertNull( store.get( "foo" ) );
    assertNull( store.get( "foo1" ) );
    assertEquals( "bar", store.get( "foo3" ) );
  }

  @Test
  public void testClearRemovesAllValues() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );
    store.add( "foo3", "bar" );

    store.clear();

    assertNull( store.get( "foo" ) );
    assertNull( store.get( "foo1" ) );
    assertNull( store.get( "foo3" ) );
  }

  @Test
  public void testClearSendsClearCall() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );
    store.add( "foo3", "bar" );

    store.clear();

    verify( serviceObject ).call( "clear", null );
  }

  @Test
  public void testAddSendsAddCall() {
    ClientStoreImpl store = new ClientStoreImpl();

    store.add( "foo", "bar" );

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass( JsonObject.class );
    verify( serviceObject ).call( eq( "add" ), captor.capture() );
    assertEquals( "foo", captor.getValue().get( "key" ).asString() );
    assertEquals( "bar", captor.getValue().get( "value" ).asString() );
  }

  @Test
  public void testSynchronizeCallAddsValues() {
    ClientStoreImpl store = new ClientStoreImpl();
    when( ( ( RemoteObjectImpl )serviceObject ).getHandler() ).thenReturn( store );
    JsonObject properties = new JsonObject();
    properties.add( "foo", "bar" );
    properties.add( "foo1", "bar1" );

    TabrisTestUtil.dispatchCall( serviceObject, "synchronize", properties  );

    assertEquals( "bar", store.get( "foo" ) );
    assertEquals( "bar1", store.get( "foo1" ) );
  }
}

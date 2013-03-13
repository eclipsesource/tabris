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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.tabris.test.TabrisTestUtil;


@SuppressWarnings("restriction")
public class ClientStoreImplTest {

  private RemoteObject serviceObject;

  @Before
  public void setUp() {
    Fixture.setUp();
    serviceObject = TabrisTestUtil.mockServiceObject();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
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
  @SuppressWarnings("unchecked")
  public void testRemoveSendsAllRemovedKeys() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );

    store.remove( "foo" );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( serviceObject ).call( eq( "remove" ), captor.capture() );
    String[] removedKeys = ( String[] )captor.getValue().get( "keys" );
    assertArrayEquals( new String[] { "foo" },  removedKeys );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testEmptyRemoveDoesntSendsRemovedKeys() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );

    store.remove();

    verify( serviceObject, never() ).call( eq( "remove" ), anyMap() );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testBulkRemovesSendsAllRemovedKeys() {
    ClientStoreImpl store = new ClientStoreImpl();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );

    store.remove( "foo" ,"foo1" );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( serviceObject ).call( eq( "remove" ), captor.capture() );
    String[] removedKeys = ( String[] )captor.getValue().get( "keys" );
    assertArrayEquals( new String[] { "foo", "foo1" },  removedKeys );
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
  @SuppressWarnings("unchecked")
  public void testAddSendsAddCall() {
    ClientStoreImpl store = new ClientStoreImpl();

    store.add( "foo", "bar" );

    ArgumentCaptor<Map> captor = ArgumentCaptor.forClass( Map.class );
    verify( serviceObject ).call( eq( "add" ), captor.capture() );
    assertEquals( "foo", captor.getValue().get( "key" ) );
    assertEquals( "bar", captor.getValue().get( "value" ) );
  }

  @Test
  public void testSynchronizeCallAddsValues() {
    ClientStoreImpl store = new ClientStoreImpl();
    when( ( ( RemoteObjectImpl )serviceObject ).getHandler() ).thenReturn( store );
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put( "foo", "bar" );
    properties.put( "foo1", "bar1" );

    Fixture.dispatchCall( serviceObject, "synchronize", properties  );

    assertEquals( "bar", store.get( "foo" ) );
    assertEquals( "bar1", store.get( "foo1" ) );
  }
}

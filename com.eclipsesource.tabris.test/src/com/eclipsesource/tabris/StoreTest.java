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
package com.eclipsesource.tabris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class StoreTest {

  @Test
  public void testGetValue() {
    Store store = new Store();
    store.add( "foo", "bar" );

    assertEquals( "bar", store.get( "foo", String.class ) );
  }

  @Test
  public void testGetAllValues() {
    Store store = new Store();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );

    Map<String, Object> values = store.getAll();

    assertEquals( "bar", values.get( "foo" ) );
    assertEquals( "bar", values.get( "foo1" ) );
    assertEquals( 2, values.size() );
  }

  @Test
  public void testGetAllValuesFromStoreOfStore() {
    Store store = new Store();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );
    Store store2 = new Store( store );

    Map<String, Object> values = store2.getAll();

    assertEquals( "bar", values.get( "foo" ) );
    assertEquals( "bar", values.get( "foo1" ) );
    assertEquals( 2, values.size() );
  }

  @Test
  public void testAddAll() {
    Store store = new Store();
    store.add( "foo", "bar" );
    store.add( "foo1", "bar" );
    Store store2 = new Store();
    store2.addStore( store );

    Map<String, Object> values = store2.getAll();

    assertEquals( "bar", values.get( "foo" ) );
    assertEquals( "bar", values.get( "foo1" ) );
    assertEquals( 2, values.size() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetValueWithWrongType() {
    Store store = new Store();
    store.add( "foo", "bar" );

    store.get( "foo", Integer.class );
  }

  @Test
  public void testGetWithNonExistingKeyReturnsNull() {
    Store store = new Store();

    assertNull( store.get( "foo", String.class ) );
  }

  @Test
  public void testGetList() {
    Store store = new Store();
    ArrayList<Object> list = new ArrayList<Object>();

    store.add( "foo", list );
    List actualList = store.get( "foo", List.class );

    assertSame( list, actualList );
  }
}

package com.eclipsesource.tabris.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class PageStoreTest {

  @Test
  public void testGetValue() {
    PageStore store = new PageStore();
    store.set( "foo", "bar" );

    assertEquals( "bar", store.get( "foo", String.class ) );
  }

  @Test
  public void testGetAllValues() {
    PageStore store = new PageStore();
    store.set( "foo", "bar" );
    store.set( "foo1", "bar" );

    Map<String, Object> values = store.getAll();

    assertEquals( "bar", values.get( "foo" ) );
    assertEquals( "bar", values.get( "foo1" ) );
    assertEquals( 2, values.size() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetValueWithWrongType() {
    PageStore store = new PageStore();
    store.set( "foo", "bar" );

    store.get( "foo", Integer.class );
  }

  @Test
  public void testGetWithNonExistingKeyReturnsNull() {
    PageStore store = new PageStore();

    assertNull( store.get( "foo", String.class ) );
  }

  @Test
  public void testGetList() {
    PageStore store = new PageStore();
    ArrayList<Object> list = new ArrayList<Object>();

    store.set( "foo", list );
    List actualList = store.get( "foo", List.class );

    assertSame( list, actualList );
  }
}

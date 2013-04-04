package com.eclipsesource.tabris.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class PageDataTest {

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( PageData.class ) );
  }

  @Test
  public void testGetValue() {
    PageData data = new PageData();
    data.set( "foo", "bar" );

    assertEquals( "bar", data.get( "foo", String.class ) );
  }

  @Test
  public void testGetAllValues() {
    PageData data = new PageData();
    data.set( "foo", "bar" );
    data.set( "foo1", "bar" );

    Map<String, Object> values = data.getAll();

    assertEquals( "bar", values.get( "foo" ) );
    assertEquals( "bar", values.get( "foo1" ) );
    assertEquals( 2, values.size() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetValueWithWrongType() {
    PageData data = new PageData();
    data.set( "foo", "bar" );

    data.get( "foo", Integer.class );
  }

  @Test
  public void testGetWithNonExistingKeyReturnsNull() {
    PageData data = new PageData();

    assertNull( data.get( "foo", String.class ) );
  }

  @Test
  public void testGetList() {
    PageData data = new PageData();
    ArrayList<Object> list = new ArrayList<Object>();

    data.set( "foo", list );
    List actualList = data.get( "foo", List.class );

    assertSame( list, actualList );
  }
}

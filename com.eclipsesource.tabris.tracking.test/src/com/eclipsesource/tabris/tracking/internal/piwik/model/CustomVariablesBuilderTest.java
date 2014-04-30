/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class CustomVariablesBuilderTest {

  private CustomVariablesBuilder builder;

  @Before
  public void setUp() {
    builder = new CustomVariablesBuilder();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullKey() {
    builder.addCustomVariable( null, "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyKey() {
    builder.addCustomVariable( "", "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullValue() {
    builder.addCustomVariable( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyValue() {
    builder.addCustomVariable( "foo", "" );
  }

  @Test
  public void testBuildsCustomVariablesJsonArray() {
    String json = builder.addCustomVariable( "key", "value" ).addCustomVariable( "key2", "value2" ).getJson();
    assertEquals( "{\"1\":[\"key\",\"value\"],\"2\":[\"key2\",\"value2\"]}", json );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithNoCustomVariablesAdded() {
    builder.getJson();
  }
}

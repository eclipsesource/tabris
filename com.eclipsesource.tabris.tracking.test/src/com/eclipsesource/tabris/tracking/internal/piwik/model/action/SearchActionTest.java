/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_SEARCH;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_SEARCH_CATEGORY;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_SEARCH_COUNT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class SearchActionTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSearchTerms() {
    new SearchAction( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptySearchTerms() {
    new SearchAction( "foo", "" );
  }

  @Test
  public void testAddsSearchTermsToParameters() throws Exception {
    SearchAction searchAction = new SearchAction( "foo", "bar" );

    assertEquals( "bar", searchAction.getParameter().get( getRequestKey( ACTION_SEARCH ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCategory() {
    SearchAction searchAction = new SearchAction( "foo", "bar" );

    searchAction.setCategory( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCategory() {
    SearchAction searchAction = new SearchAction( "foo", "bar" );

    searchAction.setCategory( "" );
  }

  @Test
  public void testAddsCategoryToParametsrs() throws Exception {
    SearchAction searchAction = new SearchAction( "foo", "bar" ).setCategory( "baz" );

    assertEquals( "baz", searchAction.getParameter().get( getRequestKey( ACTION_SEARCH_CATEGORY ) ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCount() {
    SearchAction searchAction = new SearchAction( "foo", "bar" );

    searchAction.setCount( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCount() {
    SearchAction searchAction = new SearchAction( "foo", "bar" );

    searchAction.setCount( "" );
  }

  @Test
  public void testAddsCountToParametsrs() throws Exception {
    SearchAction searchAction = new SearchAction( "foo", "bar" ).setCount( "baz" );

    assertEquals( "baz", searchAction.getParameter().get( getRequestKey( ACTION_SEARCH_COUNT ) ) );
  }
}

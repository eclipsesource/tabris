/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.test.util;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;


/**
 * <b>Please Note:</b> This class is preliminary API and may change in future version
 */
public class TestRemoteObject implements RemoteObject {

  private final String id;
  private OperationHandler handler;

  public TestRemoteObject( String id ) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void set( String name, int value ) {
  }

  @Override
  public void set( String name, double value ) {
  }

  @Override
  public void set( String name, boolean value ) {
  }

  @Override
  public void set( String name, String value ) {
  }

  @Override
  public void set( String name, JsonValue value ) {
  }

  @Override
  public void listen( String eventType, boolean listen ) {
  }

  @Override
  public void call( String method, JsonObject parameters ) {
  }

  @Override
  public void destroy() {
  }

  @Override
  public void setHandler( OperationHandler handler ) {
    this.handler = handler;
  }

  public OperationHandler getHandler() {
    return handler;
  }

}
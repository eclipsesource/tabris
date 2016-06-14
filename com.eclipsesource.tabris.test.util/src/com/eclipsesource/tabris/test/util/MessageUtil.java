/*******************************************************************************
 * Copyright (c) 2014, 2016 EclipseSource and others.
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
import org.eclipse.rap.rwt.internal.protocol.Operation;
import org.eclipse.rap.rwt.internal.protocol.Operation.CallOperation;
import org.eclipse.rap.rwt.internal.protocol.Operation.CreateOperation;
import org.eclipse.rap.rwt.internal.protocol.Operation.ListenOperation;
import org.eclipse.rap.rwt.internal.protocol.Operation.SetOperation;
import org.eclipse.rap.rwt.testfixture.internal.Fixture;
import org.eclipse.rap.rwt.testfixture.internal.TestMessage;


/**
 * <b>Please Note:</b> This class is preliminary API and may change in future version
 */
@SuppressWarnings("restriction")
public class MessageUtil {

  public static enum OperationType {
    CREATE, SET, CALL, LISTEN
  }

  public static boolean hasCreateOperation( String type ) {
    boolean found = false;
    TestMessage protocolMessage = Fixture.getProtocolMessage();
    int operationCount = protocolMessage.getOperationCount();
    for( int i = 0; i < operationCount; i++ ) {
      Operation operation = protocolMessage.getOperation( i );
      if( operation instanceof CreateOperation ) {
        CreateOperation create = ( CreateOperation )operation;
        if( create.getType().equals( type ) ) {
          found = true;
        }
      }
    }
    return found;
  }

  public static boolean isParentOfCreate( String type, String parentId ) {
    boolean found = false;
    TestMessage protocolMessage = Fixture.getProtocolMessage();
    int operationCount = protocolMessage.getOperationCount();
    for( int i = 0; i < operationCount; i++ ) {
      Operation operation = protocolMessage.getOperation( i );
      if( operation instanceof CreateOperation ) {
        CreateOperation create = ( CreateOperation )operation;
        if( create.getType().equals( type ) && create.getProperties().get( "parent" ).asString().equals( parentId ) ) {
          found = true;
        }
      }
    }
    return found;
  }

  public static JsonObject getHead() {
    return Fixture.getProtocolMessage().getHead();
  }

  public static boolean hasOperation( String target, OperationType type, String operationName ) {
    TestMessage message = Fixture.getProtocolMessage();
    if( type == OperationType.CREATE ) {
      CreateOperation operation = message.findCreateOperation( target );
      if( operation != null ) {
        return true;
      }
    } else if( type == OperationType.CALL ) {
      CallOperation operation = message.findCallOperation( target, operationName );
      if( operation != null ) {
        return true;
      }
    } else if( type == OperationType.SET ) {
      SetOperation operation = findSetOperation( message, target );
      if( operation != null ) {
        return true;
      }
    } else if( type == OperationType.LISTEN ) {
      ListenOperation operation = findListenOperation( message, target );
      if( operation != null ) {
        return true;
      }
    }
    return false;
  }

  public static JsonObject getOperationProperties( String target, OperationType type, String operationName ) {
    TestMessage message = Fixture.getProtocolMessage();
    if( type == OperationType.CREATE ) {
      CreateOperation operation = message.findCreateOperation( target );
      if( operation != null ) {
        return operation.getProperties();
      }
    } else if( type == OperationType.CALL ) {
      CallOperation operation = message.findCallOperation( target, operationName );
      if( operation != null ) {
        return operation.getParameters();
      }
    } else if( type == OperationType.SET ) {
      SetOperation operation = findSetOperation( message, target );
      if( operation != null ) {
        return operation.getProperties();
      }
    } else if( type == OperationType.LISTEN ) {
      ListenOperation operation = findListenOperation( message, target );
      if( operation != null ) {
        return operation.getProperties();
      }
    }
    return new JsonObject();
  }

  private static SetOperation findSetOperation( TestMessage message, String target ) {
    TestMessage protocolMessage = Fixture.getProtocolMessage();
    int operationCount = protocolMessage.getOperationCount();
    for( int i = 0; i < operationCount; i++ ) {
      Operation operation = protocolMessage.getOperation( i );
      if( operation instanceof SetOperation ) {
        SetOperation set = ( SetOperation )operation;
        if( set.getTarget().equals( target ) ) {
          return set;
        }
      }
    }
    return null;
  }

  private static ListenOperation findListenOperation( TestMessage message, String target ) {
    TestMessage protocolMessage = Fixture.getProtocolMessage();
    int operationCount = protocolMessage.getOperationCount();
    for( int i = 0; i < operationCount; i++ ) {
      Operation operation = protocolMessage.getOperation( i );
      if( operation instanceof ListenOperation ) {
        ListenOperation listen = ( ListenOperation )operation;
        if( listen.getTarget().equals( target ) ) {
          return listen;
        }
      }
    }
    return null;
  }

  private MessageUtil() {
    // prevent instantiation
  }

}

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
package com.eclipsesource.tabris.internal.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageUtil {

  public static byte[] getBytes( InputStream stream ) {
    byte[] result = null;
    if( stream != null ) {
      try {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[ 16384 ];
        while( ( read = stream.read( data, 0, data.length ) ) != -1 ) {
          buffer.write( data, 0, read );
        }
        buffer.flush();
        result = buffer.toByteArray();
      } catch( IOException e ) {
        throw new IllegalStateException( e );
      }
    }
    return result;
  }

  private ImageUtil() {
    // prevent instantiation
  }
}

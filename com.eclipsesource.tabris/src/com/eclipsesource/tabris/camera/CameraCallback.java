/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.camera;

import org.eclipse.swt.graphics.Image;


/**
 * <p>
 * The <code>CameraCallback</code> is used to receive notifications from a mobile client when it's done with 
 * taking/selecting pictures. In the case of a success the picture will be passed as an <code>Image</code> object.
 * </p>
 * 
 * @see Image
 * @since 0.8
 */
public interface CameraCallback {

  /**
   * <p>
   * Called in the case of successfully taking or selecting a picture from a mobile client's camera/photo album.
   * </p>
   *  
   *  @see Image
   */
  void onSuccess( Image image );

  /**
   * <p>
   * Called in the case of an error while selecting/taking a picture from a mobile client's camera/photo album.
   * </p>
   */
  void onError();
  
}

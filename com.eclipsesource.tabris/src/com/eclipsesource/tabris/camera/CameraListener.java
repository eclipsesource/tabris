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

import java.io.Serializable;

import org.eclipse.swt.graphics.Image;


/**
 * <p>
 * The {@link CameraListener} is used to receive notifications from a mobile client when it's done with
 * taking/selecting pictures. In the case of a success the picture will be passed as an <code>Image</code> object.
 * </p>
 *
 * @see Image
 * @since 1.0
 */
public interface CameraListener extends Serializable {

  /**
   * <p>
   * Called in the case of successfully taking or selecting a picture from a mobile client's camera/photo album.
   * </p>
   *
   * @param image the picture transfered form the client. May be <code>null</code> when there was any error on the
   * client side.
   *
   *  @see Image
   */
  void receivedPicture( Image image );

}

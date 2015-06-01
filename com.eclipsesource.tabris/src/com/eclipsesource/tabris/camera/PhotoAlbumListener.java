/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
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
 * The {@link PhotoAlbumListener} is used to receive notifications from a mobile client when it's done with
 * selecting pictures from the photo album. In the case of a success the picture will be passed as
 * an <code>Image</code> object.
 * </p>
 *
 * @see Image
 *
 * @since 1.4
 */
public interface PhotoAlbumListener extends Serializable {

  /**
   * <p>
   * Called in the case of successfully selecting a picture from a mobile client's photo album.
   * </p>
   *
   * @param image the picture transfered form the client. May be <code>null</code> if there was an error or the user
   *              canceled.
   *
   * @see Image
   */
  void receivedImage( Image image );

}

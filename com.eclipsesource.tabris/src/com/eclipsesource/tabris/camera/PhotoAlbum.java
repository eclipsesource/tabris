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

import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.client.service.ClientService;


/**
 * <p>
 * The <code>PhotoAlbum</code> component can be used to receive pictures from a mobile client's photo
 * album. The picture will be sent to the server side and a callback will be called. See
 * <code>PhotoAlbumListener</code>. An instance of {@link PhotoAlbum} can be accessed using
 * <code>RWT.getClient().getService( PhotoAlbum.class )</code>.
 * </p>
 *
 * @see PhotoAlbumListener
 * @see PhotoAlbumOptions
 * @see Client
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * @since 1.4
 */
public interface PhotoAlbum extends ClientService {

  /**
   * <p>
   * Instructs the client to open the photo album. The added {@link PhotoAlbumListener}s will be called when the
   * user has selected a picture.
   * </p>
   *
   * @param options The options that should be used as the configuration for taking a picture. Must not
   *                be <code>null</code>.
   *
   * @see PhotoAlbumListener
   */
  void open( PhotoAlbumOptions options );

  /**
   * <p>
   * Adds a {@link PhotoAlbumListener} to get notified about image events.
   * </p>
   */
  void addPhotoAlbumListener( PhotoAlbumListener listener );

  /**
   * <p>
   * Removes a {@link PhotoAlbumListener}.
   * </p>
   */
  void removePhotoAlbumListener( PhotoAlbumListener listener );

}

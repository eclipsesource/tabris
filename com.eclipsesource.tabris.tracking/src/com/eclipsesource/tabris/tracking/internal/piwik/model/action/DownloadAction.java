/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.tracking.internal.piwik.model.action;

import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeyProvider.getRequestKey;
import static com.eclipsesource.tabris.tracking.internal.piwik.request.RequestKeys.ACTION_DOWNLOAD;

public class DownloadAction extends Action {

  public DownloadAction( String actionUrl ) {
    super( actionUrl );
    addParameter( getRequestKey( ACTION_DOWNLOAD ), actionUrl );
  }
}

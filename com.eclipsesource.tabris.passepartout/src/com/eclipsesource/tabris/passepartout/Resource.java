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
package com.eclipsesource.tabris.passepartout;

import org.eclipse.swt.widgets.Widget;


/**
 * <p>
 * When doing responsive design it's not enough to only adjust the application layout to changing screen sizes.
 * What's often needed are responsive resources like fonts, images and colors. For this reason the {@link Resource}
 * type exist.
 * </p>
 * <p>
 * A {@link Resource} needs to be created using {@link PassePartout#createResource(Rule...)}. As you can see you
 * define rules as you do in a {@link FluidGridData}. These rules define what happens when the parent's size change e.g.
 * setting an image, changing the fonts and so on. For this reason {@link Instruction}s exist e.g.
 * {@link PassePartout#image(org.eclipse.swt.graphics.Image)}.
 * </p>
 * <p>
 * After creating a {@link Resource} with {@link Rule}s you need to bind it to one or more {@link Widget}s. This can be
 * achieved called the {@link Resource#bindTo(Widget...)} method. After the {@link Resource} is bound to a widget it
 * reacts on size changes of it's parent or next higher UI element.
 * </p>
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * @since 0.9
 */
public interface Resource {

  /**
   * <p>
   * Binds a {@link Resource} to one or more {@link Widget}s. After this method is called the {@link Resource} adjusts
   * itself to size changes of the {@link Widget}'s parent or next higher UI element.
   * </p>
   */
  void bindTo( Widget... widgets );

}

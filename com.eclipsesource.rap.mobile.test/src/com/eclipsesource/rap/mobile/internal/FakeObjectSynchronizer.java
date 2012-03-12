package com.eclipsesource.rap.mobile.internal;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.protocol.IClientObject;

import com.eclipsesource.rap.mobile.internal.AbstractObjectSynchronizer;

public class FakeObjectSynchronizer extends AbstractObjectSynchronizer {

	public FakeObjectSynchronizer(Adaptable object) {
		super(object);
	}

	@Override
	protected void renderInitialization(IClientObject clientObject, Object object) {

	}

	@Override
	protected void readData(Object object) {

	}

	@Override
	protected void preserveValues(Object object) {

	}

	@Override
	protected void processAction(Object object) {

	}

	@Override
	protected void renderChanges(Object object) {

	}

	@Override
	protected void destroy(IClientObject clientObject) {

	}

}

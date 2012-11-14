package com.eclipsesource.tabris.internal;

import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.rap.rwt.internal.protocol.IClientObject;


@SuppressWarnings("restriction")
public class FakeObjectSynchronizer extends AbstractObjectSynchronizer<Adaptable> {

	public FakeObjectSynchronizer(Adaptable object) {
		super(object);
	}

	@Override
	protected void renderInitialization(IClientObject clientObject, Adaptable object) {

	}

	@Override
	protected void readData(Adaptable object) {

	}

	@Override
	protected void preserveValues(Adaptable object) {

	}

	@Override
	protected void processAction(Adaptable object) {

	}

	@Override
	protected void renderChanges(Adaptable object) {

	}

}

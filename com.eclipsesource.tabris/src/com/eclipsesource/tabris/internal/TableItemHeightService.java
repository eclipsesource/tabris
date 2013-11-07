package com.eclipsesource.tabris.internal;

import java.util.Map;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ClientService;
import org.eclipse.rap.rwt.internal.lifecycle.LifeCycleUtil;
import org.eclipse.rap.rwt.internal.remote.ConnectionImpl;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

public class TableItemHeightService implements ClientService, OperationHandler {

	public static final String GRID_ITEM_HEIGHT_SETTER = "gridItemHeightSetter";
	private static final String SET_ITEM_HEIGHT = "setItemHeight";
	private static final String ITEM_HEIGHT = "itemHeight";
	private static final String TARGET = "target";
	private final RemoteObject remoteObject;

	public TableItemHeightService() {
		remoteObject = ((ConnectionImpl) RWT.getUISession().getConnection())
				.createServiceObject(GRID_ITEM_HEIGHT_SETTER);
		remoteObject.setHandler(this);
	}

	public void handleSet(Map<String, Object> properties) {
	}

	public void handleCall(String method, Map<String, Object> parameters) {
		if ((SET_ITEM_HEIGHT).equals(method)) {
			Display display = LifeCycleUtil.getSessionDisplay();
			Shell shell = display.getActiveShell();
			String widgetID = (String) parameters.get(TARGET);
			Integer itemHeight = (Integer) parameters.get(ITEM_HEIGHT);
			Widget widget = WidgetUtil.find(shell, widgetID);
			if (widget instanceof Table) {
				Table table = (Table) widget;
				table.setData(RWT.CUSTOM_ITEM_HEIGHT, itemHeight);
				return;
			} else if (widget instanceof Tree) {
				Tree tree = (Tree) widget;
				tree.setData(RWT.CUSTOM_ITEM_HEIGHT, itemHeight);
				return;
			}
		}
	}

	public void handleNotify(String event, Map<String, Object> properties) {
	}

	@Override
	public void handleSet(JsonObject properties) {
	}

	@Override
	public void handleCall(String method, JsonObject parameters) {
		if ((SET_ITEM_HEIGHT).equals(method)) {
			Display display = LifeCycleUtil.getSessionDisplay();
			Shell shell = display.getActiveShell();
			String widgetID = parameters.get(TARGET).asString();
			int itemHeight = parameters.get(ITEM_HEIGHT).asInt();
			Widget widget = WidgetUtil.find(shell, widgetID);
			if (widget instanceof Table) {
				Table table = (Table) widget;
				table.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(itemHeight));
				return;
			} else if (widget instanceof Tree) {
				Tree tree = (Tree) widget;
				tree.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(itemHeight));
				return;
			}
		}
	}

	@Override
	public void handleNotify(String event, JsonObject properties) {
	}
}

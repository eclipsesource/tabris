package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.THEME_ID_SWT;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.internal.theme.ThemeUtil;


public class TabrisSWTClientProvider extends TabrisClientProvider {

  public static final String ID_SWT = "com.eclipsesource.tabris.swt";

		public String getPlatform() {
			String userAgent = RWT.getRequest().getHeader(Constants.USER_AGENT);
			if (userAgent != null && userAgent.contains(ID_SWT)) {
				return ID_SWT;
			}
			return null;
		}

		@Override
		public boolean accept(HttpServletRequest request) {
			String platform = getPlatform();
			if (platform != null && platform.equals(ID_SWT)) {
				ThemeUtil.setCurrentThemeId(RWT.getUISession(), THEME_ID_SWT);
				return true;
			}
			return super.accept(request);
		}

		@Override
		public Client getClient() {
			return new SWTClient();
		}
	}
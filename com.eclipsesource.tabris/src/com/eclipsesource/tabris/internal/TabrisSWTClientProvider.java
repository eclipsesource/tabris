package com.eclipsesource.tabris.internal;

import static com.eclipsesource.tabris.internal.Constants.ID_SWT;
import static com.eclipsesource.tabris.internal.Constants.THEME_ID_SWT;

import jakarta.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.internal.theme.ThemeUtil;

import com.eclipsesource.tabris.VersionCheck;

@SuppressWarnings("restriction")
public class TabrisSWTClientProvider extends TabrisClientProvider {

  public TabrisSWTClientProvider() {
    super( new NoVersionCheck() );
  }

  public String getPlatform() {
    String userAgent = RWT.getRequest().getHeader( Constants.USER_AGENT );
    if( userAgent != null && userAgent.contains( ID_SWT ) ) {
      return ID_SWT;
    }
    return null;
  }

  @Override
  public boolean accept( HttpServletRequest request ) {
    String platform = getPlatform();
    if( platform != null && platform.equals( ID_SWT ) ) {
      ThemeUtil.setCurrentThemeId( RWT.getUISession(), THEME_ID_SWT );
      return true;
    }
    return super.accept( request );
  }

  @Override
  public Client getClient() {
    return new TabrisSWTClient();
  }

  static class NoVersionCheck implements VersionCheck {

    @Override
    public boolean accept( String clientVersion, String serverVersion ) {
      return true;
    }

    @Override
    public String getErrorMessage( String clientVersion, String serverVersion ) {
      return null;
    }

  }
}
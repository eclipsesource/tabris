/*******************************************************************************
 * Copyright (c) 2014, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import org.eclipse.rap.rwt.testfixture.internal.TestRequest;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;


/**
 * <b>Please Note:</b> This class is preliminary API and may change in future version
 */
@SuppressWarnings("restriction")
public class TabrisRequest implements HttpServletRequest {

  private final TestRequest delegate;

  public TabrisRequest( TestRequest delegate ) {
    this.delegate = delegate;
  }

  @Override
  public String getAuthType() {
    return delegate.getAuthType();
  }

  public void addCookie( Cookie cookie ) {
    delegate.addCookie( cookie );
  }

  @Override
  public Cookie[] getCookies() {
    return delegate.getCookies();
  }

  @Override
  public long getDateHeader( String arg0 ) {
    return delegate.getDateHeader( arg0 );
  }

  @Override
  public String getHeader( String arg0 ) {
    return delegate.getHeader( arg0 );
  }

  public void setHeader( String arg0, String arg1) {
    delegate.setHeader( arg0, arg1 );
  }

  @Override
  public Enumeration<String> getHeaders( String arg0 ) {
    return delegate.getHeaders( arg0 );
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    return delegate.getHeaderNames();
  }

  @Override
  public int getIntHeader( String arg0 ) {
    return delegate.getIntHeader( arg0 );
  }

  @Override
  public String getMethod() {
    return delegate.getMethod();
  }

  public void setMethod( String method ) {
    delegate.setMethod( method );
  }

  @Override
  public String getPathInfo() {
    return delegate.getPathInfo();
  }

  public void setPathInfo( String pathInfo ) {
    delegate.setPathInfo( pathInfo );
  }

  @Override
  public String getPathTranslated() {
    return delegate.getPathTranslated();
  }

  @Override
  public String getContextPath() {
    return delegate.getContextPath();
  }

  public void setContextPath( String contextPath ) {
    delegate.setContextPath( contextPath );
  }

  @Override
  public String getQueryString() {
    return delegate.getQueryString();
  }

  @Override
  public String getRemoteUser() {
    return delegate.getRemoteUser();
  }

  @Override
  public boolean isUserInRole( String arg0 ) {
    return false;
  }

  @Override
  public Principal getUserPrincipal() {
    return null;
  }

  @Override
  public String getRequestedSessionId() {
    return delegate.getRequestedSessionId();
  }

  @Override
  public String getRequestURI() {
    return delegate.getRequestURI();
  }

  public void setRequestURI( String requestURI ) {
    delegate.setRequestURI( requestURI );
  }

  @Override
  public StringBuffer getRequestURL() {
    return delegate.getRequestURL();
  }

  @Override
  public String getServletPath() {
    return delegate.getServletPath();
  }

  public void setServletPath( String servletPath ) {
    delegate.setServletPath( servletPath );
  }

  @Override
  public HttpSession getSession( boolean arg0 ) {
    return delegate.getSession( arg0 );
  }

  @Override
  public HttpSession getSession() {
    return delegate.getSession();
  }

  @Override
  public boolean isRequestedSessionIdValid() {
    return delegate.isRequestedSessionIdValid();
  }

  @Override
  public boolean isRequestedSessionIdFromCookie() {
    return delegate.isRequestedSessionIdFromCookie();
  }

  @Override
  public boolean isRequestedSessionIdFromURL() {
    return delegate.isRequestedSessionIdFromURL();
  }

  @Override
  public Object getAttribute( String arg0 ) {
    return delegate.getAttribute( arg0 );
  }

  @Override
  public Enumeration<String> getAttributeNames() {
    return delegate.getAttributeNames();
  }

  @Override
  public String getCharacterEncoding() {
    return delegate.getCharacterEncoding();
  }

  @Override
  public void setCharacterEncoding( String arg0 ) throws UnsupportedEncodingException {
    delegate.setCharacterEncoding( arg0 );
  }

  @Override
  public int getContentLength() {
    return delegate.getContentLength();
  }

  @Override
  public String getContentType() {
    return delegate.getContentType();
  }

  public void setContentType( String contentType ) {
    delegate.setContentType( contentType );
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    return delegate.getInputStream();
  }

  @Override
  public String getParameter( String arg0 ) {
    return delegate.getParameter( arg0 );
  }

  @Override
  public Enumeration<String> getParameterNames() {
    return delegate.getParameterNames();
  }

  @Override
  public String[] getParameterValues( String arg0 ) {
    return delegate.getParameterValues( arg0 );
  }

  public void setParameter( String key, String value ) {
    delegate.setParameter( key, value );
  }

  public void addParameter( String key, String value ) {
    delegate.addParameter( key, value );
  }

  @Override
  public Map<String,String[]> getParameterMap() {
    return delegate.getParameterMap();
  }

  @Override
  public String getProtocol() {
    return delegate.getProtocol();
  }

  @Override
  public String getScheme() {
    return delegate.getScheme();
  }

  public void setScheme( String scheme ) {
    delegate.setScheme( scheme );
  }

  @Override
  public String getServerName() {
    return delegate.getServerName();
  }

  public void setServerName( String serverName ) {
    delegate.setServerName( serverName );
  }

  @Override
  public int getServerPort() {
    return delegate.getServerPort();
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return delegate.getReader();
  }

  public void setBody( String body ) {
    delegate.setBody( body );
  }

  public String getBody() {
    return delegate.getBody();
  }

  @Override
  public String getRemoteAddr() {
    return delegate.getRemoteAddr();
  }

  @Override
  public String getRemoteHost() {
    return delegate.getRemoteHost();
  }

  @Override
  public void setAttribute( String arg0, Object arg1 ) {
    delegate.setAttribute( arg0, arg1 );
  }

  @Override
  public void removeAttribute( String arg0 ) {
    delegate.removeAttribute( arg0 );
  }

  @Override
  public Locale getLocale() {
    return delegate.getLocale();
  }

  @Override
  public Enumeration<Locale> getLocales() {
    return delegate.getLocales();
  }

  public void setLocales( Locale... locales ) {
    delegate.setLocales( locales );
  }

  @Override
  public boolean isSecure() {
    return delegate.isSecure();
  }

  @Override
  public RequestDispatcher getRequestDispatcher( String arg0 ) {
    return delegate.getRequestDispatcher( arg0 );
  }

  public void setSession( HttpSession session ) {
    delegate.setSession( session );
  }

  @Override
  public String getLocalAddr() {
    return delegate.getLocalAddr();
  }

  @Override
  public String getLocalName() {
    return delegate.getLocalName();
  }

  @Override
  public int getLocalPort() {
    return delegate.getLocalPort();
  }

  @Override
  public int getRemotePort() {
    return delegate.getRemotePort();
  }

  @Override
  public ServletContext getServletContext() {
    return delegate.getServletContext();
  }

  @Override
  public AsyncContext startAsync() throws IllegalStateException {
    return delegate.startAsync();
  }

  @Override
  public AsyncContext startAsync( ServletRequest servletRequest, ServletResponse servletResponse ) throws IllegalStateException {
    return delegate.startAsync( servletRequest, servletResponse );
  }

  @Override
  public boolean isAsyncStarted() {
    return delegate.isAsyncStarted();
  }

  @Override
  public boolean isAsyncSupported() {
    return delegate.isAsyncSupported();
  }

  @Override
  public AsyncContext getAsyncContext() {
    return delegate.getAsyncContext();
  }

  @Override
  public DispatcherType getDispatcherType() {
    return delegate.getDispatcherType();
  }

  @Override
  public boolean authenticate( HttpServletResponse response ) throws IOException, ServletException {
    return delegate.authenticate( response );
  }

  @Override
  public void login( String username, String password ) throws ServletException {
    delegate.login( username, password );
  }

  @Override
  public void logout() throws ServletException {
    delegate.logout();
  }

  @Override
  public Collection<Part> getParts() throws IOException, ServletException {
    return delegate.getParts();
  }

  @Override
  public Part getPart( String name ) throws IOException, ServletException {
    return delegate.getPart( name );
  }

  @Override
  public long getContentLengthLong() {
    return delegate.getContentLengthLong();
  }

  @Override
  public String changeSessionId() {
    return delegate.changeSessionId();
  }

  @Override
  public <T extends HttpUpgradeHandler> T upgrade( Class<T> handlerClass )
    throws IOException, ServletException
  {
    return delegate.upgrade( handlerClass );
  }

  @Override
  public String getRequestId() {
    return delegate.getRequestId();
  }

  @Override
  public String getProtocolRequestId() {
    return delegate.getProtocolRequestId();
  }

  @Override
  public ServletConnection getServletConnection() {
    return delegate.getServletConnection();
  }

}

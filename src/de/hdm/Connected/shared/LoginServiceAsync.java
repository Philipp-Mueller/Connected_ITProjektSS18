package de.hdm.Connected.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import de.hdm.Connected.client.LoginInfo;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}

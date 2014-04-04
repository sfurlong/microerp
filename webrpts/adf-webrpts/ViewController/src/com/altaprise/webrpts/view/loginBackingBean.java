package com.altaprise.webrpts.view;

import oracle.adf.view.rich.component.rich.input.RichInputText;
import com.altaprise.webrpts.model.AppModuleImpl;
public class loginBackingBean {
  private RichInputText userIdProp;
  private RichInputText passwdProp;

  public loginBackingBean() {
  }

  public String submit_cb_action() {
    // Add event code here...
    AppModuleImpl appModule = new AppModuleImpl();
    String uid = getUserIdProp().getValue().toString();
    String pwd = getPasswdProp().getValue().toString();
    if (uid == null) uid = "";
    if (pwd == null) pwd = "";
    boolean val = appModule.validateSecurity(uid, pwd);
    if (val == true) {
      return "loginSuccess";
    } else {
      return "loginFail";
    }
  }

  public void setUserIdProp(RichInputText userIdProp) {
    this.userIdProp = userIdProp;
  }

  public RichInputText getUserIdProp() {
    return userIdProp;
  }

  public void setPasswdProp(RichInputText passwdProp) {
    this.passwdProp = passwdProp;
  }

  public RichInputText getPasswdProp() {
    return passwdProp;
  }
}

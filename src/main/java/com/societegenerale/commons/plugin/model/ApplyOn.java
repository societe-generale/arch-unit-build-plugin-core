package com.societegenerale.commons.plugin.model;


import java.io.Serializable;
import lombok.ToString;

@ToString
public class ApplyOn implements Serializable {
  static final long serialVersionUID = 1L;

  private String packageName;


  private String scope;

  //default constructor is required at runtime
  public ApplyOn() {

  }

  //convenience constructor when calling from unit tests
  public ApplyOn(String packageName, String scope) {
    this.packageName = packageName;
    this.scope = scope;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getScope() {
    return scope;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}

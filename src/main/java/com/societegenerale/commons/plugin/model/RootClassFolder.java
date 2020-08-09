package com.societegenerale.commons.plugin.model;

/**
 * wrapper around a simple String, representing the root folder where the compiled class files are. Typically,
 /classes or /test-classes in a Maven context

 */
public class RootClassFolder {

  private String value;

  public RootClassFolder(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

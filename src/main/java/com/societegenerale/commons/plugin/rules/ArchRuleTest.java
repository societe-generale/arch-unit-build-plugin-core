package com.societegenerale.commons.plugin.rules;

import java.util.Collection;

import com.societegenerale.commons.plugin.service.ScopePathProvider;

/**
 * Created by agarg020917 on 11/10/2017.
 */
@FunctionalInterface
public interface ArchRuleTest {

  /**
   *
   * @param packagePath the package from which classes should be loaded, for example "com.societegenerale"
   * @param scopePathProvider from which root directory we should load classes, either for "main" or "test" classes
   * @param excludedPaths
   */
  void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths);

}

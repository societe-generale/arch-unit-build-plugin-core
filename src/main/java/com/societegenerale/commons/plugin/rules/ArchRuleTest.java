package com.societegenerale.commons.plugin.rules;

import com.societegenerale.commons.plugin.service.ScopePathProvider;

/**
 * Created by agarg020917 on 11/10/2017.
 */
@FunctionalInterface
public interface ArchRuleTest {

  void execute(String path, ScopePathProvider scopePathProvider);

}

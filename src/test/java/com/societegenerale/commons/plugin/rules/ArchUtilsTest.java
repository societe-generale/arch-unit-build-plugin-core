package com.societegenerale.commons.plugin.rules;

import java.util.Arrays;

import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaClasses;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * These are not great test, as they will fail when new classes are added in the code base, but acceptable for now
 */
public class ArchUtilsTest {

	//instantiating to init the static logger in ArchUtils..
	ArchUtils archUtils=new ArchUtils(new SilentLog());

	@Test
	public void shouldLoadClassesFromGivenPackage() {
		JavaClasses classes = ArchUtils.importAllClassesInPackage("./target/classes/", "com/societegenerale/commons/plugin/model");

		long noOfClassesInPackage = classes.stream().count();

		assertThat(noOfClassesInPackage).isEqualTo(3);
	}

	@Test
	public void shouldLoadAllClassesWhenGivenPakageDoesntExist() {
		JavaClasses classes = ArchUtils.importAllClassesInPackage("./target/classes", "someNotExistingFolder");

		long noOfClasses = classes.stream().filter(it -> !it.isNestedClass()).count();

		assertThat(noOfClasses).isEqualTo(26);
	}

	@Test
	public void shouldIgnoreClassesFromConfiguredPaths() {

		JavaClasses classes = ArchUtils.importAllClassesInPackage("./target", "");

		assertThat(classes).isNotEmpty();

		JavaClasses classesWithTestClassesExclusions = ArchUtils.importAllClassesInPackage("./target", "",Arrays.asList("test-classes"));

		assertThat(classesWithTestClassesExclusions).isNotEmpty();

		assertThat(classes.size()).as("There should be less classes loaded when we apply the test-classes exclusion").isGreaterThan(classesWithTestClassesExclusions.size());
	}


}

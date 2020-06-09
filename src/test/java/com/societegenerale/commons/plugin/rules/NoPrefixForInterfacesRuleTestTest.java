package com.societegenerale.commons.plugin.rules;

import static com.societegenerale.commons.plugin.rules.NoPrefixForInterfacesRuleTest.NO_PREFIX_INTERFACE_VIOLATION_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.societegenerale.aut.test.IInterfaceWithIncorrectName;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.service.DefaultScopePathProvider;
import com.societegenerale.commons.plugin.utils.ArchUtils;

@RunWith(Parameterized.class)
public class NoPrefixForInterfacesRuleTestTest {

	private String path;

	private String pathIInterfaceWithIncorrectName = "./target/aut-target/test-classes/com/societegenerale/aut/test/IInterfaceWithIncorrectName.class";

	// interfacesWithProperNames
	private static String pathInterfaceWithCorrectName = "./target/aut-target/test-classes/com/societegenerale/aut/test/InterfaceWithCorrectName.class";

	private static String pathTotallyGoodInterfaceName = "./target/aut-target/test-classes/com/societegenerale/aut/test/TotallyGoodInterfaceName.class";

	public NoPrefixForInterfacesRuleTestTest(String path) {
		this.path = path;
	}

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Parameterized.Parameters
	public static Collection pathsOfClassesNotThrowingAnyException() {
		return Arrays.asList(new String[] { pathInterfaceWithCorrectName, pathTotallyGoodInterfaceName });
	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(
				() -> new NoPrefixForInterfacesRuleTest().execute(path, new DefaultScopePathProvider(), emptySet()))
						.doesNotThrowAnyException();
	}

	@Test
	public void shouldThrowViolations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoPrefixForInterfacesRuleTest().execute(pathIInterfaceWithIncorrectName, new DefaultScopePathProvider(),
					emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(IInterfaceWithIncorrectName.class.getName())
				.hasMessageContaining(NO_PREFIX_INTERFACE_VIOLATION_MESSAGE);

	}

}
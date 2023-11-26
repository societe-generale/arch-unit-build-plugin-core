package com.societegenerale.commons.plugin.rules;

import com.societegenerale.aut.test.TestSpecificScopeProvider;
import com.societegenerale.commons.plugin.SilentLog;
import com.societegenerale.commons.plugin.utils.ArchUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringFieldsThatAreActuallyDatesRuleTestTest {

	private String pathClassWithStringsEndingWithTheWordDate = "com/societegenerale/aut/main/ClassWithStringsEndingWithTheWordDate.class";

	private String pathClassWithoutStringsEndingWithTheWordDate = "com/societegenerale/aut/main/ClassWithoutStringsEndingWithTheWordDate.class";

	@BeforeEach
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolationTest() {

		assertThatThrownBy(() -> {
			new StringFieldsThatAreActuallyDatesRuleTest().execute(pathClassWithStringsEndingWithTheWordDate,
					new TestSpecificScopeProvider(), emptySet());
		}).isInstanceOf(AssertionError.class);


	}

	@Test
	public void shouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new StringFieldsThatAreActuallyDatesRuleTest()
				.execute(pathClassWithoutStringsEndingWithTheWordDate, new TestSpecificScopeProvider(), emptySet()))
						.doesNotThrowAnyException();

	}

}

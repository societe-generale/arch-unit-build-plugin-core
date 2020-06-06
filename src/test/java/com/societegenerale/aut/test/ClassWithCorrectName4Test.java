package com.societegenerale.aut.test;

import org.junit.Test;
import org.junit.jupiter.api.Nested;

public class ClassWithCorrectName4Test {

	@Nested
	class PositiveCase {

		@Nested
		class PositiveCaseOne {

		}

		@Nested
		class PositiveCaseTwo {

		}

		@Nested
		class PositiveCaseThree {

		}

	}

	@Nested
	class NegativeCase {

		@Nested
		class NegativeCaseOne {

		}

		@Nested
		class NegativeCaseTwo {

			@Test
			public void check() {
			}

		}

	}

}

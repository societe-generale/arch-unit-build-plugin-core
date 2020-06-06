package com.societegenerale.aut.test;

import org.junit.jupiter.api.Nested;

public class Class3WithNoMethodAnnotatedWithTest {

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

		}

	}

}

package com.societegenerale.aut.test;

public class ObjectThrowingGenericException {

	public void numberBiggerThanTen(int number) throws RuntimeException {
		if (number > 10) {

		} else {

			throw new RuntimeException("The number is not bigger than ten.");

		}
	}

}

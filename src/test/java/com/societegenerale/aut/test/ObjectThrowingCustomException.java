package com.societegenerale.aut.test;

import com.societegenerale.aut.test.exception.CustomException;

public class ObjectThrowingCustomException {

	public void numberBiggerThanTen(int number) throws CustomException {
		if (number > 10) {

		} else {

			throw new CustomException("The number is not bigger than ten.");

		}
	}

}

package com.devplant.introduction.exception;

public class BookPickupDateIsToFarInTheFutureException extends RuntimeException {

	public BookPickupDateIsToFarInTheFutureException(){
		super("Pickup date is to far in the future");
	}
}

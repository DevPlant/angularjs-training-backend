package com.devplant.introduction.service;

import com.devplant.introduction.domain.BookStock;
import com.devplant.introduction.domain.User;
import com.devplant.introduction.exception.*;
import com.devplant.introduction.repository.jpa.BookStockRepository;
import com.devplant.introduction.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class BookReservationService {

	@Autowired
	private BookStockRepository bookStockRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public BookStock reserveBook(Long pickupTimestamp, long bookId, String username) {

		Instant now = Instant.now();

		Instant pickupInstant = Instant.ofEpochMilli(pickupTimestamp);

		if (pickupInstant.minusSeconds(86400 * 3).isAfter(now)) {
			throw new BookPickupDateIsToFarInTheFutureException();
		}

		User user = userRepository.findOneByUsername(username);

		// If the user reserved this book already - throw an exception - he cannot get the same book twice ;)
		user.getReservedBookStocks().forEach(bookStock -> {
			if (bookStock.getBook().getId() == bookId) {
				throw new BookAlreadyReservedByUserException();
			}
		});

		// If a stock is available - reserve the first one

		BookStock bookStock = bookStockRepository.findByAvailableStocksForBook(bookId).stream().findFirst()
				.orElseThrow(BookNotAvailableForReservationException::new);

		// reserved now
		bookStock.setReservationTimestamp(now.toEpochMilli());
		// pickup defined be user
		bookStock.setPickupTimestamp(pickupInstant.toEpochMilli());
		// return date is set to 1 week after pickup

		bookStock.setReturnTimestamp(pickupInstant.plusSeconds(86400 * 7).toEpochMilli());
		// update relation in both directions
		bookStock.setUser(user);
		user.getReservedBookStocks().add(bookStock);

		// save
		userRepository.save(user);

		bookStock = bookStockRepository.save(bookStock);

		return bookStock;
	}
}

package com.devplant.introduction.rest.user;

import com.devplant.introduction.domain.User;
import com.devplant.introduction.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/api/user-management/self", method = RequestMethod.GET)
	public ResponseEntity<User> self(Principal principal) {
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} else {
			return new ResponseEntity<>(userRepository.findOneByUsername(principal.getName()), HttpStatus.OK);
		}
	}
}

package com.jwt2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt2.helper.JwtUtility;
import com.jwt2.model.JWTRequest;
import com.jwt2.model.JWTResponse;
import com.jwt2.services.UserService;

@RestController
public class HomeController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtility jwtUtility;
	@Autowired
	private UserService userService;

	@RequestMapping("/")
	@ResponseBody
	public String welcome() {
		return "Hello sir welcome /n This is welcome page...! !";
	}

	@PostMapping("/authenticate")
	public JWTResponse generateToken(@RequestBody JWTRequest jwtRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS...." + e);
		}

		final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
		final String token = jwtUtility.generateToken(userDetails);

		return new JWTResponse(token);
	}

}

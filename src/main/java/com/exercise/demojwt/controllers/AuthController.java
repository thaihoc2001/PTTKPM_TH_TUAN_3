package com.exercise.demojwt.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.demojwt.entities.Role;
import com.exercise.demojwt.entities.User;
import com.exercise.demojwt.models.AuthResponse;
import com.exercise.demojwt.models.MessageResponse;
import com.exercise.demojwt.repository.RoleRepository;
import com.exercise.demojwt.repository.UserRepository;
import com.exercise.demojwt.utils.JwtUtil;

@CrossOrigin()
@RestController()
@RequestMapping("api/auth")
public class AuthController {
    
    @Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtil jwtUtils;

    @PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {

		org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		System.out.println();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());		

		String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());
			
		return ResponseEntity.ok(new AuthResponse(jwt, roles.get(0)));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody User signUpRequest) {
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		User userCheck = userRepository.findByUsername(signUpRequest.getUsername());
		if(userCheck != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("username exists!"));
		}
		Role role = roleRepository.findByName("Customer");
		user.setRole(role);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}

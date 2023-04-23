package com.project.springboot.restapi.user;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private UserDetailsRepository repository;

	public UserDetailsCommandLineRunner(UserDetailsRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public void run(String... args) throws Exception {
		
		repository.save(new UserDetails(0, "Ravi","Admin"));
		repository.save(new UserDetails(0, "Ranga","Admin"));
		repository.save(new UserDetails(0, "John","User"));
		
		List<UserDetails> users = repository.findByRole("Admin");
		
		logger.info(users.toString());
	}

}

package com.koszczi.userswithtasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UsersWithTasksApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersWithTasksApplication.class, args);
	}

}

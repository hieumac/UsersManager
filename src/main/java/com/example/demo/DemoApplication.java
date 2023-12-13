package com.example.demo;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class  DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	BCryptPasswordEncoder brBCryptPasswordEncoder(){return  new BCryptPasswordEncoder();}

//	@Bean
//	CommandLineRunner run(UserServiceImp userServiceImp){
//		return args -> {
//			userServiceImp.saveRole(new Role(null,"ROLE_USER"));
//			userServiceImp.saveRole(new Role(null,"ROLE_ADMIN"));
//
//			userServiceImp.saveUser(new User(null, "hieu1","hieu1@gmail.com","123456",new HashSet<>()));
//			userServiceImp.saveUser(new User(null, "hieu2","hieu2@gmail.com","123456",new HashSet<>()));
//			userServiceImp.saveUser(new User(null, "hieu3","hieu3@gmail.com","123456",new HashSet<>()));
//
//			userServiceImp.addToUser("hieu1@gmail.com","ROLE_ADMIN");
//			userServiceImp.addToUser("hieu1@gmail.com","ROLE_USER");
//			userServiceImp.addToUser("hieu2@gmail.com","ROLE_USER");
//			userServiceImp.addToUser("hieu3@gmail.com","ROLE_ADMIN");
//		};
//	}
}

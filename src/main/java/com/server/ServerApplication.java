package com.server;

import com.server.entity.User;
import com.server.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
        System.out.println("Server started on port 8080");
    }

    /*@Bean
    CommandLineRunner initUser(UserRepository repo) {
        return args -> {
            if (repo.findByUsername("test").isEmpty()) {
                repo.save(new User("1", "test", "123456"));
            }
        };
    }*/
}
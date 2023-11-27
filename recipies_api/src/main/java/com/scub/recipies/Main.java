package com.scub.recipies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.transaction.Transactional;

@SpringBootApplication()
@EnableWebMvc
public class Main implements CommandLineRunner {

//    @Autowired
//    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
//        System.out.println("Fetching all users:");
//        String email = "jeandurand@mail.com";
//        String password = "jeanpassword";
//        Optional<User> user = Optional.ofNullable(userService.authenticate(email, password));
//
//        if (user.isPresent()) {
//            System.out.println("Authentication successful for user: " + user.get().getName());
//        } else {
//            System.out.println("Authentication failed for email: " + email);
//        }
    }
}
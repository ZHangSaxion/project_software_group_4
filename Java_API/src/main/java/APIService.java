package main.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The server class of the application. Inherited official class from springboot to create the server.
 *
 * @author Ziru Hang
 * @version 1.0
 * @since 02-12-2020
 */
@SpringBootApplication
public class APIService {
    public static void main(String[] args) {
        SpringApplication.run(APIService.class, args);
    }

}
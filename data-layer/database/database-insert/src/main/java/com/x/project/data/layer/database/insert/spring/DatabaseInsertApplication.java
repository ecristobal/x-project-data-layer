package com.x.project.data.layer.database.insert.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for database insert microservice JAR.
 * 
 * @author Esteban Cristóbal
 */
@SpringBootApplication
public class DatabaseInsertApplication {

    /**
     * Main method.
     * 
     * @param args
     *            program arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DatabaseInsertApplication.class, args);
    }

}

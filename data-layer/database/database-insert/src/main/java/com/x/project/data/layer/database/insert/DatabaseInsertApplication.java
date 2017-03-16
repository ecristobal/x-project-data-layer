package com.x.project.data.layer.database.insert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Main class for database insert microservice JAR.
 * 
 * @author Esteban Cristóbal
 */
@SpringBootApplication
@ImportResource("classpath:/META-INF/spring/database-insert*.xml")
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

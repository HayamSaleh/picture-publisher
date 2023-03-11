package com.picture.publishing.publisher.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.picture.publishing.publisher" })
@EnableJpaRepositories("com.picture.publishing.publisher.repository")
@EntityScan("com.picture.publishing.publisher.model")
public class PublisherApplication {

	private static final Logger logger = LoggerFactory.getLogger(PublisherApplication.class);

	@Bean
	public DozerBeanMapper dozerBeanMapper() {
		return new DozerBeanMapper();
	}

	public static void main(String[] args) {
		logger.info("Application is about to start");
		createDatabaseIfNotExists();
		SpringApplication.run(PublisherApplication.class, args);
	}

	public static void createDatabaseIfNotExists() {
		Connection connection = null;
		Statement statement = null;
		try {
			logger.debug("Creating database if not exist...");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "P@ssw0rd");
			statement = connection.createStatement();
			statement.executeQuery("SELECT count(*) FROM pg_database WHERE datname = 'publisher'");
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);

			if (count <= 0) {
				statement.executeUpdate("CREATE DATABASE publisher");
				logger.debug("Database created.");
			} else {
				logger.debug("Database already exist.");
			}
		} catch (SQLException e) {
			logger.error(e.toString());
		} finally {
			try {
				if (statement != null) {
					statement.close();
					logger.debug("Closed Statement.");
				}
				if (connection != null) {
					logger.debug("Closed Connection.");
					connection.close();
				}
			} catch (SQLException e) {
				logger.error(e.toString());
			}
		}
	}
}

package com.swaglabs.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class TestData_Reader {

	// Properties object to store configuration data
	public Properties properties;

	// Constructor to initialize ConfigReader and load properties file
	public TestData_Reader() {
		properties = new Properties();
		try (var input = Files.newInputStream(
				Path.of("src", "main", "java", "com", "swaglabs", "utilities", "TestData.properties"))) {
			properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Unable to load properties file", e);
		}
	}

	// Method to retrieve property value based on key
	public String getProperty(String key) {
		return properties.getProperty(key); // Return the value associated with the specified key
	}

}

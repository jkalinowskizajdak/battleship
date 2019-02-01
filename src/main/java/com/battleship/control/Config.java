package com.battleship.control;

import org.apache.log4j.Logger;

import java.util.Properties;

public class Config {

    private static final Logger logger = Logger.getLogger(Config.class);
    private static final String CONFIG_FILE = "config.properties";
    private static final String SECRET = "secret";

    private Properties configFile;

    public Config() {
        configFile = new java.util.Properties();
        try {
            configFile.load(this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
        } catch (Exception e) {
            logger.error("Problem with reading config file!", e);
        }
    }

    public String getSecretProperty() {
        String value = configFile.getProperty(SECRET);
        return value;
    }
}
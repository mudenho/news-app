/*
 * Copyright (C) Sportradar AG. See LICENSE for full license governing this code
 */

package com.mycompany.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {

    String result = "";
    InputStream inputStream;

    public static String nt_arts_url = "https://api-football-v1.p.rapidapi.com/v3/leagues";
    public static String nt_apiKey = "1J5WHGE0SbvXO3hXPbVk5IN1Oay4k4wo";

    public String getPropValues(String propertyKey) throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            result = prop.getProperty(propertyKey);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }
}

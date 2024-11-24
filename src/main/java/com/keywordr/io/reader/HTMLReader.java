package com.keywordr.io.reader;

import com.keywordr.exception.KeywordrRuntimeException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTMLReader {
    public static String readHtmlWithJavaNet(String urlString) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }
        }

        return result.toString();
    }

    public static String readHtmlWithSelenium(String urlString, int timeout) throws IOException {
        String pathToChromeDriver;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            pathToChromeDriver = "..\\chromedriver\\chromedriver.exe";
        } else {
            pathToChromeDriver = "../chromedriver/chromedriver.exe";
        }
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);

        // Configure ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver(options);

        // Initialize return variable
        String pageSource;

        try {
            driver.get(urlString);

            Thread.sleep(timeout);

            pageSource = driver.getPageSource();

        } catch (InterruptedException e) {
            throw new KeywordrRuntimeException("Chrome WebDriver failed to read url + " + urlString + ", with message: " + e.getMessage());
        } finally {
            driver.quit();
        }

        return pageSource;
    }
}

package com.qanexus.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class EnvConfig {
    private static final String CONFIG_FILE = "config.properties";
    private static final EnvConfig INSTANCE = new EnvConfig();

    private final Properties properties = new Properties();

    private EnvConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Missing resource: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load " + CONFIG_FILE, ex);
        }
    }

    public static EnvConfig getInstance() {
        return INSTANCE;
    }

    public String get(String key) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys;
        }
        String env = System.getenv(toEnvKey(key));
        if (env != null && !env.isBlank()) {
            return env;
        }
        return properties.getProperty(key);
    }

    public String getBaseUrl() {
        return require("api.baseUrl");
    }

    public String getLoginPath() {
        return require("api.auth.loginPath");
    }

    public String getUiBaseUrl() {
        return require("ui.baseUrl");
    }

    public String getUiLoginPath() {
        return require("ui.loginPath");
    }

    public String getBrowser() {
        return get("browser", "chrome");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(get("browser.headless", "false"));
    }

    public int getWindowWidth() {
        return Integer.parseInt(get("browser.window.width", "1920"));
    }

    public int getWindowHeight() {
        return Integer.parseInt(get("browser.window.height", "1080"));
    }

    public int getImplicitTimeout() {
        return Integer.parseInt(get("timeout.implicit", "10"));
    }

    public int getExplicitTimeout() {
        return Integer.parseInt(get("timeout.explicit", "10"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(get("timeout.page.load", "30"));
    }

    public String get(String key, String defaultValue) {
        String value = get(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    private String require(String key) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing config value for: " + key);
        }
        return value;
    }

    private String toEnvKey(String key) {
        return key.toUpperCase().replace('.', '_');
    }
}

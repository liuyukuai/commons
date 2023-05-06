package com.cutefool.commons.config.core;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public class ResourceUtils {

    private static final String[] DEFAULT_FILE_SEARCH_LOCATIONS = new String[]{"./config/", "./"};

    @SuppressWarnings("unchecked")
    public static Properties readConfigFile(String configPath, Properties defaults) {
        Properties props = new Properties();
        if (defaults != null) {
            props.putAll(defaults);
        }

        InputStream in = loadConfigFileFromDefaultSearchLocations(configPath);

        try {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException ex) {
            log.warn("Reading config failed: {}", ex.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    log.warn("Close config failed: {}", ex.getMessage());
                }
            }
        }

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (String propertyName : props.stringPropertyNames()) {
                sb.append(propertyName).append('=').append(props.getProperty(propertyName)).append('\n');

            }
            if (sb.length() > 0) {
                log.debug("Reading properties: \n" + sb);
            } else {
                log.warn("No available properties: {}", configPath);
            }
        }
        return props;
    }

    private static InputStream loadConfigFileFromDefaultSearchLocations(String configPath) {
        try {
            // load from default search locations
            for (String searchLocation : DEFAULT_FILE_SEARCH_LOCATIONS) {
                File candidate = Paths.get(searchLocation, configPath).toFile();
                if (candidate.exists() && candidate.isFile() && candidate.canRead()) {
                    log.debug("Reading config from resource {}", candidate.getAbsolutePath());
                    return new FileInputStream(candidate);
                }
            }

            // load from classpath
            URL url = ClassLoaderUtil.getLoader().getResource(configPath);

            if (url != null) {
                InputStream in = getResourceAsStream(url);

                if (in != null) {
                    log.debug("Reading config from resource {}", url.getPath());
                    return in;
                }
            }

            // load outside resource under current user path
            File candidate = new File(System.getProperty("user.dir"), configPath);
            if (candidate.exists() && candidate.isFile() && candidate.canRead()) {
                log.debug("Reading config from resource {}", candidate.getAbsolutePath());
                return new FileInputStream(candidate);
            }
        } catch (FileNotFoundException e) {
            //ignore
        }
        return null;
    }

    private static InputStream getResourceAsStream(URL url) {
        try {
            return url != null ? url.openStream() : null;
        } catch (IOException e) {
            return null;
        }
    }
}

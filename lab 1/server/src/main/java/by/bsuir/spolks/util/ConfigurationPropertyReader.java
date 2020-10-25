package by.bsuir.spolks.util;

import by.bsuir.spolks.congifuration.ServerConfiguration;
import by.bsuir.spolks.exceptions.ConfigurationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationPropertyReader {

    private static final String CONFIGURATION_FILE_PATH = "server/src/main/resources/configuration.properties";

    private static final String CONFIGURATION_IP = "server.ip";
    private static final String CONFIGURATION_PORT = "server.port";
    private static final String CONFIGURATION_MAX_CONNECTIONS = "server.maxConnections";

    public static ServerConfiguration getServerConfiguration() throws ConfigurationException {

        try (InputStream input = new FileInputStream(CONFIGURATION_FILE_PATH)) {

            Properties properties = new Properties();

            properties.load(input);

            return ServerConfiguration.builder()
                    .host(properties.getProperty(CONFIGURATION_IP))
                    .port(Integer.parseInt(properties.getProperty(CONFIGURATION_PORT)))
                    .maxClientNumber(Integer.parseInt(properties.getProperty(CONFIGURATION_MAX_CONNECTIONS)))
                    .build();
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }

    }

}

package by.bsuir.spolks.util;

import by.bsuir.spolks.entity.ClientConfiguration;
import by.bsuir.spolks.exceptions.ConfigurationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationPropertyReader {

    private static final String CONFIGURATION_FILE_PATH = "client/src/main/resources/configuration.properties";

    private static final String CONFIGURATION_IP = "server.ip";
    private static final String CONFIGURATION_PORT_TCP = "server.portTCP";
    private static final String CONFIGURATION_PORT_UDP = "server.portUDP";

    public static ClientConfiguration getServerConfiguration() throws ConfigurationException {

        try (InputStream input = new FileInputStream(CONFIGURATION_FILE_PATH)) {

            Properties properties = new Properties();

            properties.load(input);

            return ClientConfiguration.builder()
                    .host(properties.getProperty(CONFIGURATION_IP))
                    .portTCP(Integer.parseInt(properties.getProperty(CONFIGURATION_PORT_TCP)))
                    .portUDP(Integer.parseInt(properties.getProperty(CONFIGURATION_PORT_UDP)))
                    .build();
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }

    }

}

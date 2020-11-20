package by.bsuir.spolks;

import by.bsuir.spolks.configuration.UDPClientStarter;
import by.bsuir.spolks.entity.ClientConfiguration;
import by.bsuir.spolks.exceptions.ClientException;
import by.bsuir.spolks.exceptions.ConfigurationException;
import by.bsuir.spolks.util.ConfigurationPropertyReader;

public class MainClientUDP {

    public static void main(String[] args) {

        try {
            ClientConfiguration clientConfiguration = ConfigurationPropertyReader.getServerConfiguration();

            try {
                UDPClientStarter clientStarter = new UDPClientStarter(clientConfiguration);
                clientStarter.start();
            } catch (ClientException e) {
                System.out.println("Some server connection problem: " + e.getLocalizedMessage());
            }
        } catch (ConfigurationException e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        }

    }
}

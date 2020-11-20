package by.bsuir.spolks;

import by.bsuir.spolks.congifuration.ServerTCPThreadStarter;
import by.bsuir.spolks.congifuration.ServerUPDStarter;
import by.bsuir.spolks.entity.ServerConfiguration;
import by.bsuir.spolks.exceptions.ConfigurationException;
import by.bsuir.spolks.exceptions.ServerException;
import by.bsuir.spolks.exceptions.ValidationException;
import by.bsuir.spolks.util.ConfigurationPropertyReader;

public class MainServerUDP {

    public static void main(String[] args) {

        try {
            ServerConfiguration serverConfiguration = ConfigurationPropertyReader.getServerConfiguration();

            ServerUPDStarter serverUPDStarter = new ServerUPDStarter(serverConfiguration);
            serverUPDStarter.start();
        } catch (ConfigurationException | ServerException | ValidationException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}

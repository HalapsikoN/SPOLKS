package by.bsuir.spolks;

import by.bsuir.spolks.entity.ServerConfiguration;
import by.bsuir.spolks.congifuration.ServerTCPThreadStarter;
import by.bsuir.spolks.exceptions.ConfigurationException;
import by.bsuir.spolks.exceptions.ServerException;
import by.bsuir.spolks.exceptions.ValidationException;
import by.bsuir.spolks.util.ConfigurationPropertyReader;

public class MainServer {

    public static void main(String[] args) {

        try {
            ServerConfiguration serverConfiguration = ConfigurationPropertyReader.getServerConfiguration();

            ServerTCPThreadStarter serverTCPThreadStarter = new ServerTCPThreadStarter(serverConfiguration);

            System.out.println("Start...");
            serverTCPThreadStarter.start();
            System.out.println("End...");
        } catch (ValidationException | ServerException | ConfigurationException e) {
            System.out.println(e.getLocalizedMessage());
            //e.printStackTrace();
        }

    }
}

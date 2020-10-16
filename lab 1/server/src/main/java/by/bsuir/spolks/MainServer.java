package by.bsuir.spolks;

import by.bsuir.spolks.congifuration.ServerConfiguration;
import by.bsuir.spolks.congifuration.ServerThreadStarter;
import by.bsuir.spolks.exceptions.ConfigurationException;
import by.bsuir.spolks.exceptions.ServerException;
import by.bsuir.spolks.exceptions.ValidationException;
import by.bsuir.spolks.util.ConfigurationPropertyReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

    public static void main(String[] args) {

        try {
            ServerConfiguration serverConfiguration = ConfigurationPropertyReader.getServerConfiguration();

            ServerThreadStarter serverThreadStarter = new ServerThreadStarter(serverConfiguration);

            System.out.println("Start...");
            serverThreadStarter.start();
            System.out.println("End...");
        } catch (ValidationException | ServerException | ConfigurationException e) {
            System.out.println(e.getLocalizedMessage());
            //e.printStackTrace();
        }

    }
}

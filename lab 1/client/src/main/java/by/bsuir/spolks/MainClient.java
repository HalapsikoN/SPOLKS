package by.bsuir.spolks;

import by.bsuir.spolks.configuration.ClientConfiguration;
import by.bsuir.spolks.configuration.ClientStarter;
import by.bsuir.spolks.exceptions.ClientException;
import by.bsuir.spolks.exceptions.ConfigurationException;
import by.bsuir.spolks.util.ConfigurationPropertyReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClient {

    public static void main(String[] args) {

        try {
            ClientConfiguration clientConfiguration = ConfigurationPropertyReader.getServerConfiguration();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Start...(Press enter)");
            while (true) {
                String input = reader.readLine();
                if (input.equalsIgnoreCase("quit")) {
                    break;
                } else {
                    try {
                        System.out.println("Trying to connect(" + clientConfiguration.getHost() + ":" + clientConfiguration.getPort() + ")...");
                        ClientStarter clientStarter = new ClientStarter(clientConfiguration);
                        clientStarter.start();
                    } catch (ClientException e) {
                        System.out.println("Some server connection problem: " + e.getLocalizedMessage());
                    }
                }
            }
            System.out.println("End...");
        } catch (IOException | ConfigurationException e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        }
    }
}

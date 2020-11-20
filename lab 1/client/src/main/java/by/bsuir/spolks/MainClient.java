package by.bsuir.spolks;

import by.bsuir.spolks.configuration.UDPClientStarter;
import by.bsuir.spolks.entity.ClientConfiguration;
import by.bsuir.spolks.configuration.TCPClientStarter;
import by.bsuir.spolks.exceptions.ClientException;
import by.bsuir.spolks.exceptions.ConfigurationException;
import by.bsuir.spolks.service.UDPConnectionService;
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
                        System.out.println("Trying to connect(" + clientConfiguration.getHost() + ":" + clientConfiguration.getPortTCP() + ")...");
                        TCPClientStarter TCPClientStarter = new TCPClientStarter(clientConfiguration);
                        TCPClientStarter.start();
                    } catch (ClientException e) {
                        System.out.println("Some server connection problem: " + e.getLocalizedMessage());
                    }
                }
            }


        } catch (IOException | ConfigurationException e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        }
    }
}

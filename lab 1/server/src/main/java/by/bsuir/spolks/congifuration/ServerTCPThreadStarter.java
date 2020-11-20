package by.bsuir.spolks.congifuration;

import by.bsuir.spolks.entity.ServerConfiguration;
import by.bsuir.spolks.exceptions.ServerException;
import by.bsuir.spolks.exceptions.ValidationException;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EqualsAndHashCode
@RequiredArgsConstructor
public class ServerTCPThreadStarter {

    private final ServerConfiguration serverConfiguration;
    private ExecutorService executorService;

    public void start() throws ValidationException, ServerException {

        String host = serverConfiguration.getHost();
        Integer maxNumberOfClients = serverConfiguration.getMaxClientNumber();
        Integer portNumber = serverConfiguration.getPortTCP();
        if (maxNumberOfClients < 1) {
            throw new ValidationException("The number of possible clients is lower than 1");
        }
        if (portNumber < 1) {
            throw new ValidationException("The number of port is lower than 1");
        }

        executorService = Executors.newFixedThreadPool(maxNumberOfClients);

        try (ServerSocket server = new ServerSocket(portNumber, maxNumberOfClients, InetAddress.getByName(host))) {

            BufferedReader userServiceInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Server starts on host (" + host + ":" + portNumber + ") with maximum (" + maxNumberOfClients + ") available clients");

            while (!server.isClosed()) {

                if (userServiceInput.ready()) {
                    System.out.println("Main Server found any messages in channel, let's look at them.");

                    String serverCommand = userServiceInput.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Main Server initiate exiting...");
                        server.close();
                        break;
                    }
                }

                Socket client = server.accept();

                executorService.execute(new ServerTCPThread(client));
            }

            executorService.shutdown();
        } catch (IOException e) {
            throw new ServerException(e);
        }

    }
}

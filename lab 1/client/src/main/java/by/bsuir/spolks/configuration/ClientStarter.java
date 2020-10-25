package by.bsuir.spolks.configuration;

import by.bsuir.spolks.command.Command;
import by.bsuir.spolks.command.CommandProvider;
import by.bsuir.spolks.exceptions.ClientException;
import by.bsuir.spolks.util.LoadContinueService;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientStarter {

    private final ClientConfiguration clientConfiguration;
    private final CommandProvider commandProvider = CommandProvider.getInstance();

    public void start() throws ClientException {

        try (Socket socket = new Socket(clientConfiguration.getHost(), clientConfiguration.getPort())) {

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            DataInputStream fromServer = new DataInputStream(socket.getInputStream());
            DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connection is successfully established...");
            System.out.println(wrapMessageFromServer(fromServer.readUTF()));

            LoadContinueService.checkUnfinishedCommand(fromServer, toServer);

            while (!socket.isOutputShutdown()) {

                String info = userInput.readLine();

                toServer.writeUTF(info);
                toServer.flush();

                if (info.trim().equalsIgnoreCase("quit")) {
                    System.out.println(wrapMessageFromServer(fromServer.readUTF()));
                    System.out.println("Connection is terminated...");
                    break;
                }

                String[] splitCommand = info.split(" ", 2);
                Command command = commandProvider.getCommand(splitCommand[0]);
                command.execute(splitCommand.length < 2 ? "" : splitCommand[1], fromServer, toServer);
            }

        } catch (IOException e) {
            throw new ClientException(e);
        }

    }

    private String wrapMessageFromServer(String message) {
        return "Message from server: '" + message + "'";
    }
}

package by.bsuir.spolks.congifuration;

import by.bsuir.spolks.command.Command;
import by.bsuir.spolks.command.CommandProvider;
import by.bsuir.spolks.service.MemoryService;
import lombok.RequiredArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@RequiredArgsConstructor
public class ServerThread implements Runnable {

    private final Socket client;
    private final CommandProvider commandProvider = CommandProvider.getInstance();
    private final MemoryService memoryService = MemoryService.getInstance();

    @Override
    public void run() {

        try {

            DataInputStream fromClient = new DataInputStream(client.getInputStream());
            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());

            String clientId = client.getInetAddress().getHostAddress() + ":" + client.getLocalPort();

            toClient.writeUTF("Your connection id is (" + clientId + ")");
            System.out.println("Connected(" + clientId + ")...");

            memoryService.checkAndFinishForUnfinishedCommand(fromClient, toClient, clientId);

            while (!client.isClosed()) {

                String stringFromClient = fromClient.readUTF();

                if (stringFromClient.trim().equalsIgnoreCase("quit")) {
                    toClient.writeUTF("Connection (" + clientId + ") is closed...");
                    System.out.println("Disconnected(" + clientId + ")...");
                    break;
                }

                System.out.println("Command(" + clientId + "): " + stringFromClient);
                String[] splitCommand = stringFromClient.split(" ", 2);
                Command command = commandProvider.getCommand(splitCommand[0]);
                command.execute(clientId, splitCommand.length < 2 ? "" : splitCommand[1], fromClient, toClient);
            }

            fromClient.close();
            toClient.close();

            client.close();

        } catch (IOException | InterruptedException e) {
            System.out.println("Connection problem: disconnected...");
        }

    }
}

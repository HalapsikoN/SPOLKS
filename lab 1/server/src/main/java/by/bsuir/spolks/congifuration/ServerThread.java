package by.bsuir.spolks.congifuration;

import by.bsuir.spolks.command.Command;
import by.bsuir.spolks.command.CommandProvider;
import by.bsuir.spolks.exceptions.ServerException;
import lombok.RequiredArgsConstructor;

import javax.sql.rowset.serial.SerialException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@RequiredArgsConstructor
public class ServerThread implements Runnable {

    private final Socket client;
    private final CommandProvider commandProvider = CommandProvider.getInstance();

    @Override
    public void run() {

        try {

            DataInputStream fromClient = new DataInputStream(client.getInputStream());
            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());

            long threadId = Thread.currentThread().getId();
            toClient.writeUTF("Your connection id is (" + threadId + ")");
            System.out.println("Connected(" + threadId + ")...");

            while (!client.isClosed()) {

                String stringFromClient = fromClient.readUTF();

                if (stringFromClient.trim().equalsIgnoreCase("quit")) {
                    toClient.writeUTF("Connection (" + threadId + ") is closed...");
                    System.out.println("Disconnected(" + threadId + ")...");
                    break;
                }

                System.out.println("Command(" + threadId + "): " + stringFromClient);
                String[] splitCommand = stringFromClient.split(" ", 2);
                Command command = commandProvider.getCommand(splitCommand[0]);
                command.execute(splitCommand.length < 2 ? "" : splitCommand[1], fromClient, toClient);
            }

            fromClient.close();
            toClient.close();

            client.close();

        } catch (IOException e) {
            System.out.println("Connection problem: disconnected...");
        }

    }
}

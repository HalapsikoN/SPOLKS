package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;
import by.bsuir.spolks.service.Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DownloadCommand implements Command {

    @Override
    public void execute(String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException {

        try {
            Connection.doTCPDownload(fromClient, toClient, commandText, 0);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }
}

package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;
import by.bsuir.spolks.service.Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DownloadCommand implements Command {

    @Override
    public void execute(String clientId, String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException, InterruptedException {

        Connection.doTCPDownload(clientId, fromClient, toClient, commandText, 0);
    }
}

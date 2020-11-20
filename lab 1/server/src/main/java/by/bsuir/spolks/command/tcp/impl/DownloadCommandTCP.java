package by.bsuir.spolks.command.tcp.impl;

import by.bsuir.spolks.command.tcp.CommandTCP;
import by.bsuir.spolks.service.TCPConnectionService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DownloadCommandTCP implements CommandTCP {

    @Override
    public void execute(String clientId, String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException, InterruptedException {

        TCPConnectionService.doTCPDownload(clientId, fromClient, toClient, commandText, 0);
    }
}

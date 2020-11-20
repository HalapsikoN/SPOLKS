package by.bsuir.spolks.command.tcp.impl;

import by.bsuir.spolks.command.tcp.CommandTCP;
import by.bsuir.spolks.service.TCPConnectionService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DownloadCommandTCP implements CommandTCP {

    @Override
    public void execute(String commandString, DataInputStream fromServer, DataOutputStream toServer) throws IOException {
        TCPConnectionService.doTCPDownload(fromServer, toServer, commandString, null, 0);
    }
}

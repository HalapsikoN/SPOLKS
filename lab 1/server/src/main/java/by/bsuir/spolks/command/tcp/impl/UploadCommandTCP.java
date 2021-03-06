package by.bsuir.spolks.command.tcp.impl;

import by.bsuir.spolks.command.tcp.CommandTCP;
import by.bsuir.spolks.service.TCPConnectionService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UploadCommandTCP implements CommandTCP {

    private static final String FILE_PATH = "server\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;
    private static final Long TIME_TO_WAIT_MILLIS = 20000L;

    @Override
    public void execute(String clientId, String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException {

        TCPConnectionService.doTCPUpload(clientId, fromClient, toClient, commandText, null, 0);
    }
}

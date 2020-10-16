package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;
import by.bsuir.spolks.entity.CommandMemory;
import by.bsuir.spolks.service.Connection;
import by.bsuir.spolks.entity.UploadCommandMemory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UploadContinueCommand implements Command {

    private static final String FILE_PATH = "server\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;
    private static final Long TIME_TO_WAIT_MILLIS = 20000L;
    private UploadCommandMemory memory = UploadCommandMemory.getInstance();

    @Override
    public void execute(String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException {

        String info[]=commandText.split(" ",2);
        CommandMemory commandMemory = memory.getMap().get(info[0]);

        Connection.doTCPUpload(fromClient, toClient, info[0], commandMemory.getFileByteArray(), commandMemory.getDownloadedBytes());
    }
}

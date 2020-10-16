package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;
import by.bsuir.spolks.service.Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UploadCommand implements Command {

    private static final String FILE_PATH = "client\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;
    private static final Long TIME_TO_WAIT_MILLIS = 20000L;

    @Override
    public void execute(String commandString, DataInputStream fromServer, DataOutputStream toServer) throws IOException {

        try {
            Connection.doTCPUpload(fromServer, toServer, commandString, 0);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }
}

package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;
import by.bsuir.spolks.command.CommandName;
import by.bsuir.spolks.entity.UploadCommandMemory;
import by.bsuir.spolks.service.Connection;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UploadCommand implements Command {

    private static final String FILE_PATH = "server\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;
    private static final Long TIME_TO_WAIT_MILLIS = 20000L;

    @Override
    public void execute(String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException {

        Connection.doTCPUpload(fromClient, toClient, commandText, null, 0);
    }
}

package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;
import by.bsuir.spolks.service.Connection;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DownloadContinueCommand implements Command {

    private static final String FILE_PATH = "server\\src\\main\\resources\\files\\";
    private static final Integer PACKAGE_SIZE = 1024;

    @Override
    public void execute(String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException {

        String[] info=commandText.split(" ",2);

        try {
            Connection.doTCPDownload(fromClient, toClient, info[0], Integer.parseInt(info[1]));
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }


    }
}

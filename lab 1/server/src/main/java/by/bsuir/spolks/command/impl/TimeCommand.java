package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class TimeCommand implements Command {

    @Override
    public void execute(String clientId, String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException {

        toClient.writeUTF(new Date().toString());
    }
}

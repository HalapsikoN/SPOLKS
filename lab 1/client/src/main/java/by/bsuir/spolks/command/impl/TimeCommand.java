package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TimeCommand implements Command {

    @Override
    public void execute(String commandString, DataInputStream fromServer, DataOutputStream toServer) throws IOException {

        String time = fromServer.readUTF();
        System.out.println("SERVER TIME: '" + time + "'");
    }
}

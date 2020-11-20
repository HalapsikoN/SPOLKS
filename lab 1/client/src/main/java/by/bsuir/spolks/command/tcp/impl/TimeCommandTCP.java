package by.bsuir.spolks.command.tcp.impl;

import by.bsuir.spolks.command.tcp.CommandTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TimeCommandTCP implements CommandTCP {

    @Override
    public void execute(String commandString, DataInputStream fromServer, DataOutputStream toServer) throws IOException {

        String time = fromServer.readUTF();
        System.out.println("SERVER TIME: '" + time + "'");
    }
}

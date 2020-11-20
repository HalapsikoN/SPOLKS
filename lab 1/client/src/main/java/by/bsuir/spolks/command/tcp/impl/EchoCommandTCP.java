package by.bsuir.spolks.command.tcp.impl;

import by.bsuir.spolks.command.tcp.CommandTCP;
import lombok.NoArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@NoArgsConstructor
public class EchoCommandTCP implements CommandTCP {

    @Override
    public void execute(String commandString, DataInputStream fromServer, DataOutputStream toServer) throws IOException {

        String result = fromServer.readUTF();
        System.out.println("ECHO: '" + result + "'");
    }
}

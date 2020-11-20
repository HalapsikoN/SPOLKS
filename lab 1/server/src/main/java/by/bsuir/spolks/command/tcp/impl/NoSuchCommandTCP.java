package by.bsuir.spolks.command.tcp.impl;

import by.bsuir.spolks.command.tcp.CommandTCP;
import lombok.NoArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@NoArgsConstructor
public class NoSuchCommandTCP implements CommandTCP {

    @Override
    public void execute(String clientId, String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException {

        toClient.writeUTF("");
    }
}

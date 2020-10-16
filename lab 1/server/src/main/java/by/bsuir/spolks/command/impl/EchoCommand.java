package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;
import lombok.NoArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@NoArgsConstructor
public class EchoCommand implements Command {

    @Override
    public void execute(String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException {

        toClient.writeUTF(commandText);
    }
}

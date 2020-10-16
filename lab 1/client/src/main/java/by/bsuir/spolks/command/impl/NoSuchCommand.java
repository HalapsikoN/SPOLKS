package by.bsuir.spolks.command.impl;

import by.bsuir.spolks.command.Command;
import lombok.NoArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@NoArgsConstructor
public class NoSuchCommand implements Command {

    @Override
    public void execute(String commandString, DataInputStream fromServer, DataOutputStream toServer) throws IOException {

        fromServer.readUTF();
        System.out.println("There is no such command");
    }
}

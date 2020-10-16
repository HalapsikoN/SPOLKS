package by.bsuir.spolks.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Command {

    void execute(String commandString, DataInputStream fromServer, DataOutputStream toServer) throws IOException;
}

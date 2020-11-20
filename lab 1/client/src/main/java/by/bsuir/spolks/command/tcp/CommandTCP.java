package by.bsuir.spolks.command.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface CommandTCP {

    void execute(String commandString, DataInputStream fromServer, DataOutputStream toServer) throws IOException;
}

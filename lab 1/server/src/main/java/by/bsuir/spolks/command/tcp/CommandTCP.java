package by.bsuir.spolks.command.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface CommandTCP {

    void execute(String clientId, String commandText, DataInputStream fromClient, DataOutputStream toClient) throws IOException, InterruptedException;
}

package by.bsuir.spolks.command.udp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public interface CommandUDP {

    void execute(String commandString, DatagramSocket socket, InetAddress address, int port) throws IOException;
}

package by.bsuir.spolks.command.udp.impl;

import by.bsuir.spolks.command.udp.CommandUDP;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NoSuchCommandUDP implements CommandUDP {

    @Override
    public void execute(String clientId, String commandText, DatagramSocket socket, InetAddress address, int port) throws IOException, InterruptedException {

    }
}

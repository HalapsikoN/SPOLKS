package by.bsuir.spolks.command.udp.impl;

import by.bsuir.spolks.command.udp.CommandUDP;
import by.bsuir.spolks.service.UDPConnectionService;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class EchoCommandUDP implements CommandUDP {

    @Override
    public void execute(String clientId, String commandText, DatagramSocket socket, InetAddress address, int port) throws IOException, InterruptedException {

        UDPConnectionService.sendMessage(socket, address, port, commandText);
    }
}

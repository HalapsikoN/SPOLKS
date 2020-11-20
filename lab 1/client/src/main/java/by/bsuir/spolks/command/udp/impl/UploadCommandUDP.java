package by.bsuir.spolks.command.udp.impl;

import by.bsuir.spolks.command.udp.CommandUDP;
import by.bsuir.spolks.service.UDPConnectionService;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UploadCommandUDP implements CommandUDP {

    @Override
    public void execute(String commandString, DatagramSocket socket, InetAddress address, int port) throws IOException {

        try {
            UDPConnectionService.doUDPUpload(socket, address, port, commandString, 0);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}

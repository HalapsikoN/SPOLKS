package by.bsuir.spolks.command.udp.impl;

import by.bsuir.spolks.command.udp.CommandUDP;
import by.bsuir.spolks.service.UDPConnectionService;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

@NoArgsConstructor
public class TimeCommandUDP implements CommandUDP {

    @Override
    public void execute(String commandString, DatagramSocket socket, InetAddress address, int port) throws IOException {

        String message = UDPConnectionService.getMessage(socket);
        System.out.println("TIME SERVER UDP: '" + message + "'");
    }
}

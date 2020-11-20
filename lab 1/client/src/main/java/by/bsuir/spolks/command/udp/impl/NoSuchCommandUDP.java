package by.bsuir.spolks.command.udp.impl;

import by.bsuir.spolks.command.tcp.CommandTCP;
import by.bsuir.spolks.command.udp.CommandUDP;
import lombok.NoArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

@NoArgsConstructor
public class NoSuchCommandUDP implements CommandUDP {

    @Override
    public void execute(String commandString, DatagramSocket socket, InetAddress address, int port) throws IOException {

        System.out.println("There is no such command");
    }
}

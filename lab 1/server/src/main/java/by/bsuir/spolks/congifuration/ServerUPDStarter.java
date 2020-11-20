package by.bsuir.spolks.congifuration;

import by.bsuir.spolks.command.tcp.CommandTCP;
import by.bsuir.spolks.command.udp.CommandProviderUDP;
import by.bsuir.spolks.command.udp.CommandUDP;
import by.bsuir.spolks.entity.ServerConfiguration;
import by.bsuir.spolks.exceptions.ServerException;
import by.bsuir.spolks.exceptions.ValidationException;
import by.bsuir.spolks.service.MemoryService;
import by.bsuir.spolks.service.UDPConnectionService;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;

@EqualsAndHashCode
@RequiredArgsConstructor
public class ServerUPDStarter {

    private MemoryService memoryService=MemoryService.getInstance();
    private final ServerConfiguration serverConfiguration;
    private CommandProviderUDP commandProviderUDP = CommandProviderUDP.getInstance();
    private byte[] buffer = new byte[1024 * 64];

    public void start() throws ValidationException, ServerException {

        String host = serverConfiguration.getHost();
        Integer portNumber = serverConfiguration.getPortUDP();
        if (portNumber < 1) {
            throw new ValidationException("The number of port is lower than 1");
        }

        System.out.println("Server starts on host (" + host + ":" + portNumber + ")");

        try (DatagramSocket socket = new DatagramSocket(portNumber)) {

            DatagramPacket packet;

            while (true) {

                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                String clientId = address.getHostAddress() + ":" + port + "U";

                String command = new String(packet.getData(), 0, packet.getLength());

                if (command.trim().equalsIgnoreCase("stopServer")) {
                    break;
                }

                System.out.println("CommandUDP(" + clientId + "): " + command);
                String[] splitCommand = command.split(" ", 2);
                CommandUDP commandUDP = commandProviderUDP.getCommand(splitCommand[0]);
                commandUDP.execute(clientId, splitCommand.length < 2 ? "" : splitCommand[1], socket, address, port);

                memoryService.checkAndFinishForUnfinishedCommand(socket, address, port, clientId);
            }

        } catch (IOException | InterruptedException e) {
            throw new ServerException(e);
        }

    }
}

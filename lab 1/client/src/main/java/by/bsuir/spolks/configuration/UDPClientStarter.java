package by.bsuir.spolks.configuration;

import by.bsuir.spolks.command.tcp.CommandTCP;
import by.bsuir.spolks.command.udp.CommandProviderUDP;
import by.bsuir.spolks.command.udp.CommandUDP;
import by.bsuir.spolks.entity.ClientConfiguration;
import by.bsuir.spolks.exceptions.ClientException;
import by.bsuir.spolks.service.UDPConnectionService;
import by.bsuir.spolks.util.LoadContinueService;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

@RequiredArgsConstructor
public class UDPClientStarter{

    private final ClientConfiguration clientConfiguration;
    private final CommandProviderUDP commandProviderUDP = CommandProviderUDP.getInstance();

    public void start() throws ClientException {

        try (DatagramSocket socket = new DatagramSocket()) {

            InetAddress hostToSend = InetAddress.getByName(clientConfiguration.getHost());
            Integer portToSend = clientConfiguration.getPortUDP();

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("You can now send something...");

            while (true) {

                String info = userInput.readLine();

                UDPConnectionService.sendMessage(socket, hostToSend, portToSend, info);

                if (info.trim().equalsIgnoreCase("quit")) {
                    break;
                }

                String[] splitCommand = info.split(" ", 2);
                CommandUDP commandUDP = commandProviderUDP.getCommand(splitCommand[0]);
                commandUDP.execute(splitCommand.length < 2 ? "" : splitCommand[1], socket, hostToSend, portToSend);

                LoadContinueService.checkUnfinishedCommand(socket, hostToSend, portToSend);
            }

        } catch (IOException e) {
            throw new ClientException(e);
        }

    }
}

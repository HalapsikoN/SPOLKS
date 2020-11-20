package by.bsuir.spolks.service;

import by.bsuir.spolks.command.tcp.CommandNameTCP;
import by.bsuir.spolks.command.udp.CommandNameUDP;
import by.bsuir.spolks.entity.CommandMemory;
import by.bsuir.spolks.entity.FileLoadMemory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryService {

    private FileLoadMemory memory = FileLoadMemory.getInstance();
    private LoadService loadService = LoadService.getInstance();

    @Getter
    private static final MemoryService instance = new MemoryService();

    private boolean hasUnfinishedCommand(String clientId) {
        return memory.getMap().containsKey(clientId);
    }

    public void checkAndFinishForUnfinishedCommand(DataInputStream fromClient, DataOutputStream toClient, String clientId) throws IOException, InterruptedException {

        if (hasUnfinishedCommand(clientId)) {

            CommandMemory commandMemory = memory.getMap().get(clientId);

            toClient.writeUTF(commandMemory.getCommandNameTCP().name() + " " + commandMemory.getFileName() + " " + commandMemory.getDownloadedBytes());

            loadService.continueLoading(fromClient, toClient, clientId, commandMemory);
        } else {
            toClient.writeUTF("NO_COMMAND");
        }
    }

    public void checkAndFinishForUnfinishedCommand(DatagramSocket socket, InetAddress address, int port, String clientId) throws IOException, InterruptedException {

        if (hasUnfinishedCommand(clientId)) {

            CommandMemory commandMemory = memory.getMap().get(clientId);

            UDPConnectionService.sendMessage(socket, address, port,commandMemory.getCommandNameUDP().name() + " " + commandMemory.getFileName() + " " + commandMemory.getDownloadedBytes());

            loadService.continueLoading(socket, address, port, clientId, commandMemory);
        } else {
            UDPConnectionService.sendMessage(socket, address, port, "NO_COMMAND");
        }
    }

    public void saveDownloadCommandInfo(String clientId, CommandNameTCP commandNameTCP, String fileName, Integer downloadedBytes, byte[] fileByteArray) {
        CommandMemory commandMemory = CommandMemory.builder()
                .fileName(fileName)
                .commandNameTCP(commandNameTCP)
                .downloadedBytes(downloadedBytes)
                .fileByteArray(fileByteArray)
                .isSaved(true)
                .isTCPSaved(true)
                .isUDPSaved(false)
                .build();
        memory.getMap().put(clientId, commandMemory);
    }

    public void saveDownloadCommandInfo(String clientId, CommandNameUDP commandNameUDP, String fileName, Integer downloadedBytes, byte[] fileByteArray) {
        CommandMemory commandMemory = CommandMemory.builder()
                .fileName(fileName)
                .commandNameUDP(commandNameUDP)
                .downloadedBytes(downloadedBytes)
                .fileByteArray(fileByteArray)
                .isSaved(true)
                .isTCPSaved(false)
                .isUDPSaved(true)
                .build();
        memory.getMap().put(clientId, commandMemory);
    }
}

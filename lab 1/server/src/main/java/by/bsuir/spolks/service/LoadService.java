package by.bsuir.spolks.service;

import by.bsuir.spolks.entity.CommandMemory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadService {

    private MemoryService memoryService = MemoryService.getInstance();

    @Getter
    private static final LoadService instance = new LoadService();

    public void continueLoading(DataInputStream fromClient, DataOutputStream toClient, String clientId, CommandMemory commandMemory) throws IOException, InterruptedException {

        switch (commandMemory.getCommandNameTCP()) {
            case DOWNLOAD:
                TCPConnectionService.doTCPDownload(clientId, fromClient, toClient, commandMemory.getFileName(), commandMemory.getDownloadedBytes());
                break;
            case UPLOAD:
                TCPConnectionService.doTCPUpload(clientId, fromClient, toClient, commandMemory.getFileName(), commandMemory.getFileByteArray(), commandMemory.getDownloadedBytes());
                break;
            default:
                break;
        }
    }

    public void continueLoading(DatagramSocket socket, InetAddress address, int port, String clientId, CommandMemory commandMemory) throws IOException, InterruptedException {

        switch (commandMemory.getCommandNameTCP()) {
            case DOWNLOAD:
                UDPConnectionService.doUDPDownload(clientId, socket, address, port, commandMemory.getFileName(), commandMemory.getDownloadedBytes());
                break;
            case UPLOAD:
                UDPConnectionService.doUDPUpload(clientId, socket, address, port, commandMemory.getFileName(), commandMemory.getFileByteArray(), commandMemory.getDownloadedBytes());
                break;
            default:
                break;
        }
    }
}

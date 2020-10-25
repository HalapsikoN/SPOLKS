package by.bsuir.spolks.service;

import by.bsuir.spolks.entity.CommandMemory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadService {

    private MemoryService memoryService = MemoryService.getInstance();

    @Getter
    private static final LoadService instance = new LoadService();

    public void continueLoading(DataInputStream fromClient, DataOutputStream toClient, String clientId, CommandMemory commandMemory) throws IOException, InterruptedException {

        switch (commandMemory.getCommandName()) {
            case DOWNLOAD:
                Connection.doTCPDownload(clientId, fromClient, toClient, commandMemory.getFileName(), commandMemory.getDownloadedBytes());
                break;
            case UPLOAD:
                Connection.doTCPUpload(clientId, fromClient, toClient, commandMemory.getFileName(), commandMemory.getFileByteArray(), commandMemory.getDownloadedBytes());
                break;
            default:
                break;
        }
    }
}

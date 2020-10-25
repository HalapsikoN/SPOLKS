package by.bsuir.spolks.service;

import by.bsuir.spolks.command.CommandName;
import by.bsuir.spolks.entity.CommandMemory;
import by.bsuir.spolks.entity.FileLoadMemory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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

            toClient.writeUTF(commandMemory.getCommandName().name() + " " + commandMemory.getFileName() + " " + commandMemory.getDownloadedBytes());

            loadService.continueLoading(fromClient, toClient, clientId, commandMemory);
        } else {
            toClient.writeUTF("NO_COMMAND");
        }
    }

    public void saveDownloadCommandInfo(String clientId, CommandName commandName, String fileName, Integer downloadedBytes, byte[] fileByteArray) {
        CommandMemory commandMemory = CommandMemory.builder()
                .fileName(fileName)
                .commandName(commandName)
                .downloadedBytes(downloadedBytes)
                .fileByteArray(fileByteArray)
                .isSaved(true)
                .build();
        memory.getMap().put(clientId, commandMemory);
    }
}

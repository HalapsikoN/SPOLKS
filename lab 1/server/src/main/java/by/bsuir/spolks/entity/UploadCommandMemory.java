package by.bsuir.spolks.entity;

import by.bsuir.spolks.command.CommandName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UploadCommandMemory {

    private static final UploadCommandMemory instance = new UploadCommandMemory();

    private HashMap<String, CommandMemory> map = new HashMap<>();

    public static UploadCommandMemory getInstance() {
        return instance;
    }

    public void saveDownloadCommandInfo(CommandName commandName, String fileName, Integer downloadedBytes, byte[] fileByteArray) {
        CommandMemory commandMemory=CommandMemory.builder()
                .fileName(fileName)
                .commandName(commandName)
                .downloadedBytes(downloadedBytes)
                .fileByteArray(fileByteArray)
                .isSaved(true)
                .build();
        map.put(fileName, commandMemory);
    }
}

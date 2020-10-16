package by.bsuir.spolks.entity;

import by.bsuir.spolks.command.CommandName;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class StoppedCommandMemory {

    private static final StoppedCommandMemory instance=new StoppedCommandMemory();

    @Setter
    private boolean isSaved;
    private CommandName commandName;
    private String fileName;
    private Integer downloadedBytes;
    private byte[] fileByteArray;

    public static StoppedCommandMemory getInstance() {
        return instance;
    }

    public void saveDownloadCommandInfo(CommandName commandName, String fileName, Integer downloadedBytes, byte[] fileByteArray) {
        this.commandName = commandName;
        this.fileName = fileName;
        this.downloadedBytes = downloadedBytes;
        this.fileByteArray=fileByteArray;
        this.isSaved=true;
    }
}

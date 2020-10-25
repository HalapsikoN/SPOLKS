package by.bsuir.spolks.entity;

import by.bsuir.spolks.command.CommandName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommandMemory {

    private boolean isSaved;
    private CommandName commandName;
    private String fileName;
    private Integer downloadedBytes;
    private byte[] fileByteArray;
}

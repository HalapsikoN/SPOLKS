package by.bsuir.spolks.entity;

import by.bsuir.spolks.command.CommandName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Data
@Builder
public class CommandMemory {

    private boolean isSaved;
    private CommandName commandName;
    private String fileName;
    private Integer downloadedBytes;
    private byte[] fileByteArray;
}

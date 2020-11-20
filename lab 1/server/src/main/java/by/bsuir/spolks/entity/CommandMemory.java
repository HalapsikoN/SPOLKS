package by.bsuir.spolks.entity;

import by.bsuir.spolks.command.tcp.CommandNameTCP;
import by.bsuir.spolks.command.udp.CommandNameUDP;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommandMemory {

    private boolean isSaved;
    private CommandNameTCP commandNameTCP;
    private CommandNameUDP commandNameUDP;
    private String fileName;
    private Integer downloadedBytes;
    private byte[] fileByteArray;
    private boolean isTCPSaved;
    private boolean isUDPSaved;
}

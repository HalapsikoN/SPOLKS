package by.bsuir.spolks.entity;

import by.bsuir.spolks.command.tcp.CommandNameTCP;
import by.bsuir.spolks.command.udp.CommandNameUDP;
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
public class StoppedDownloadFileMemory {

    private static final StoppedDownloadFileMemory instance=new StoppedDownloadFileMemory();

    @Setter
    private boolean isSaved;
    private CommandNameTCP commandNameTCP;
    private CommandNameUDP commandNameUDP;
    private String fileName;
    private Integer downloadedBytes;
    private byte[] fileByteArray;
    private boolean isTCPSaved;
    private boolean isUDPSaved;

    public static StoppedDownloadFileMemory getInstance() {
        return instance;
    }

    public void saveDownloadCommandInfo(CommandNameTCP commandNameTCP, String fileName, Integer downloadedBytes, byte[] fileByteArray) {
        this.commandNameTCP = commandNameTCP;
        this.fileName = fileName;
        this.downloadedBytes = downloadedBytes;
        this.fileByteArray=fileByteArray;
        this.isSaved=true;
        this.isTCPSaved=true;
        this.isUDPSaved=false;
    }

    public void saveDownloadCommandInfo(CommandNameUDP commandNameUDP, String fileName, Integer downloadedBytes, byte[] fileByteArray) {
        this.commandNameUDP = commandNameUDP;
        this.fileName = fileName;
        this.downloadedBytes = downloadedBytes;
        this.fileByteArray=fileByteArray;
        this.isSaved=true;
        this.isTCPSaved=false;
        this.isUDPSaved=true;
    }
}

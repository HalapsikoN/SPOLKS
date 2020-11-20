package by.bsuir.spolks.command.udp;

import by.bsuir.spolks.command.tcp.impl.DownloadCommandTCP;
import by.bsuir.spolks.command.tcp.impl.EchoCommandTCP;
import by.bsuir.spolks.command.tcp.impl.NoSuchCommandTCP;
import by.bsuir.spolks.command.tcp.impl.TimeCommandTCP;
import by.bsuir.spolks.command.tcp.impl.UploadCommandTCP;
import by.bsuir.spolks.command.udp.impl.DownloadCommandUDP;
import by.bsuir.spolks.command.udp.impl.EchoCommandUDP;
import by.bsuir.spolks.command.udp.impl.NoSuchCommandUDP;
import by.bsuir.spolks.command.udp.impl.TimeCommandUDP;
import by.bsuir.spolks.command.udp.impl.UploadCommandUDP;

import java.util.HashMap;
import java.util.Map;

public class CommandProviderUDP {

    private static final CommandProviderUDP instance = new CommandProviderUDP();
    private final Map<CommandNameUDP, CommandUDP> repository = new HashMap<>();

    private final NoSuchCommandUDP noSuchCommand = new NoSuchCommandUDP();

    private CommandProviderUDP() {
        repository.put(CommandNameUDP.ECHO, new EchoCommandUDP());
        repository.put(CommandNameUDP.TIME, new TimeCommandUDP());
        repository.put(CommandNameUDP.DOWNLOAD, new DownloadCommandUDP());
        repository.put(CommandNameUDP.UPLOAD, new UploadCommandUDP());
    }

    public static CommandProviderUDP getInstance() {
        return instance;
    }

    public CommandUDP getCommand(String name) {
        CommandNameUDP commandNameUDP;
        CommandUDP commandUDP;

        try {
            commandNameUDP = CommandNameUDP.valueOf(name.toUpperCase());
            commandUDP = repository.get(commandNameUDP);
        } catch (IllegalArgumentException | NullPointerException e) {
            commandUDP = noSuchCommand;
        }

        return commandUDP;
    }
}

package by.bsuir.spolks.command.tcp;

import by.bsuir.spolks.command.tcp.impl.DownloadCommandTCP;
import by.bsuir.spolks.command.tcp.impl.EchoCommandTCP;
import by.bsuir.spolks.command.tcp.impl.NoSuchCommandTCP;
import by.bsuir.spolks.command.tcp.impl.TimeCommandTCP;
import by.bsuir.spolks.command.tcp.impl.UploadCommandTCP;

import java.util.HashMap;
import java.util.Map;

public class CommandProviderTCP {

    private static final CommandProviderTCP instance = new CommandProviderTCP();
    private final Map<CommandNameTCP, CommandTCP> repository = new HashMap<>();

    private final NoSuchCommandTCP noSuchCommand = new NoSuchCommandTCP();

    private CommandProviderTCP() {
        repository.put(CommandNameTCP.ECHO, new EchoCommandTCP());
        repository.put(CommandNameTCP.TIME, new TimeCommandTCP());
        repository.put(CommandNameTCP.DOWNLOAD, new DownloadCommandTCP());
        repository.put(CommandNameTCP.UPLOAD, new UploadCommandTCP());
    }

    public static CommandProviderTCP getInstance() {
        return instance;
    }

    public CommandTCP getCommand(String name) {
        CommandNameTCP commandNameTCP;
        CommandTCP commandTCP;

        try {
            commandNameTCP = CommandNameTCP.valueOf(name.toUpperCase());
            commandTCP = repository.get(commandNameTCP);
        } catch (IllegalArgumentException | NullPointerException e) {
            commandTCP = noSuchCommand;
        }

        return commandTCP;
    }
}
